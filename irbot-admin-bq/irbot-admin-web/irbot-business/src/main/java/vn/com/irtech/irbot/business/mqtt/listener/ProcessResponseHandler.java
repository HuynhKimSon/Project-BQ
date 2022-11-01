package vn.com.irtech.irbot.business.mqtt.listener;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import vn.com.irtech.irbot.business.domain.Invoice;
import vn.com.irtech.irbot.business.domain.Notification;
import vn.com.irtech.irbot.business.domain.ProcessBravo;
import vn.com.irtech.irbot.business.domain.ProcessMisa;
import vn.com.irtech.irbot.business.dto.response.RobotProcessDataRes;
import vn.com.irtech.irbot.business.dto.response.RobotProcessRes;
import vn.com.irtech.irbot.business.mapper.InvoiceMapper;
import vn.com.irtech.irbot.business.mapper.NotificationMapper;
import vn.com.irtech.irbot.business.mapper.ProcessBravoMapper;
import vn.com.irtech.irbot.business.mapper.ProcessMisaMapper;
import vn.com.irtech.irbot.business.type.NotificationType;
import vn.com.irtech.irbot.business.type.ProcessBravoStatus;
import vn.com.irtech.irbot.business.type.ProcessMisaStatus;
import vn.com.irtech.irbot.business.type.SyncType;

@Component
public class ProcessResponseHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(ProcessResponseHandler.class);

	@Autowired
	@Qualifier("threadPoolTaskExecutor")
	private TaskExecutor executor;

	@Autowired
	private ProcessMisaMapper processMisaMapper;

	@Autowired
	private ProcessBravoMapper processBravoMapper;

	@Autowired
	private NotificationMapper notificationMapper;

	@Autowired
	private InvoiceMapper invoiceMapper;

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					processMessage(topic, message);
				} catch (Exception e) {
					logger.error("Error while process mq message", e);
					e.printStackTrace();
				}
			}
		});
	}

	@Transactional
	private void processMessage(String topic, MqttMessage message) throws Exception {
		String messageContent = new String(message.getPayload(), StandardCharsets.UTF_8);
		logger.info(">>>> Receive message topic: {}, content {}", topic, messageContent);
		RobotProcessRes response = null;
		try {
			response = new Gson().fromJson(messageContent, RobotProcessRes.class);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		if (response.getSyncId() == null) {
			return;
		}

		Long processId = response.getProcessId();
		String syncId = response.getSyncId();
		String result = response.getResult();
		String errMsg = response.getErrorMsg();
		String robotUuid = topic.split("/")[2];
		RobotProcessDataRes data = response.getData();

		if (SyncType.MISA.value().equals(syncId)) {
			updateMisaProcessStatus(robotUuid, messageContent, processId, result, errMsg, data);
			return;
		}

		if (SyncType.BRAVO.value().equals(syncId)) {
			updateBravoProcessStatus(robotUuid, messageContent, processId, result, errMsg);
			return;
		}
	}

	private void updateMisaProcessStatus(String robotUuid, String messageContent, Long processId, String result,
			String errorMsg, RobotProcessDataRes data) {
		// Get process by Id
		ProcessMisa process = processMisaMapper.selectProcessMisaById(processId);
		if (process == null) {
			logger.warn("Misa Process not exist: {}", processId);
			return;
		}
		String errMsg = null;
		String invoiceNo = null;
		ProcessMisaStatus processStatus = null;
		switch (result.toUpperCase()) {
		case "SUCCESS":
			processStatus = ProcessMisaStatus.SUCCESS;
			invoiceNo = data.getInvoiceNo();
			break;
		case "PROCESSING":
			processStatus = ProcessMisaStatus.PROCESSING;
			break;
		case "FAIL":
			processStatus = ProcessMisaStatus.FAIL;
			errMsg = errorMsg;
			break;
		default:
			return;
		}

		ProcessMisa processUpdate = new ProcessMisa();
		processUpdate.setId(processId);
		processUpdate.setRobotUuid(robotUuid);
		processUpdate.setDataResponse(messageContent);
		processUpdate.setInvoiceNo(invoiceNo);
		if (processStatus != ProcessMisaStatus.PROCESSING) {
			processUpdate.setEndDate(new Date());
		}
		processUpdate.setStatus(processStatus.value());
		processUpdate.setError(errMsg);
		processMisaMapper.updateProcessMisa(processUpdate);

		// Update invoice status
		Invoice invoiceUpdate = new Invoice();
		invoiceUpdate.setId(process.getInvoiceId());
		invoiceUpdate.setStatusMisa(processStatus.value());

		if (processStatus == ProcessMisaStatus.SUCCESS) {
			// Update invoice_no
			invoiceUpdate.setInvoiceNo(invoiceNo);

			// Insert data to table process_bravo
			ProcessBravo processBravoSelect = new ProcessBravo();
			processBravoSelect.setInvoiceId(process.getInvoiceId());
			List<ProcessBravo> processList = processBravoMapper.selectProcessBravoList(processBravoSelect);
			if (CollectionUtils.isEmpty(processList)) {
				ProcessBravo processBravoInsert = new ProcessBravo();
				processBravoInsert.setInvoiceId(process.getInvoiceId());
				processBravoInsert.setInvoiceType(process.getInvoiceType());
				processBravoInsert.setTaxCode(process.getTaxCode());
				processBravoInsert.setCustomerName(process.getCustomerName());
				processBravoInsert.setCustomerName(process.getCustomerName());
				processBravoInsert.setInvoiceDate(process.getInvoiceDate());
				processBravoInsert.setInvoiceNo(invoiceNo);
				processBravoInsert.setStatus(ProcessBravoStatus.NOTSEND.value());
				processBravoMapper.insertProcessBravo(processBravoInsert);
			}
		}
		invoiceMapper.updateInvoice(invoiceUpdate);

		// Push notification
		// Push notification
		if (processStatus != ProcessMisaStatus.PROCESSING) {
			if (processStatus == ProcessMisaStatus.FAIL) {
				pushNotify(NotificationType.WARNING, "");
			}
		}
	}

	private void updateBravoProcessStatus(String robotUuid, String messageContent, Long processId, String result,
			String errorMsg) {
		// Get process by Id
		ProcessBravo process = processBravoMapper.selectProcessBravoById(processId);
		if (process == null) {
			logger.warn("Misa Process not exist: {}", processId);
			return;
		}
		String errMsg = null;
		ProcessBravoStatus processStatus = null;
		switch (result.toUpperCase()) {
		case "SUCCESS":
			processStatus = ProcessBravoStatus.SUCCESS;
			break;
		case "PROCESSING":
			processStatus = ProcessBravoStatus.PROCESSING;
			break;
		case "FAIL":
			processStatus = ProcessBravoStatus.FAIL;
			errMsg = errorMsg;
			break;
		default:
			return;
		}

		ProcessBravo processUpdate = new ProcessBravo();
		processUpdate.setId(processId);
		processUpdate.setDataResponse(messageContent);
		processUpdate.setRobotUuid(robotUuid);
		if (processStatus != ProcessBravoStatus.PROCESSING) {
			processUpdate.setEndDate(new Date());
		}
		processUpdate.setStatus(processStatus.value());
		processUpdate.setError(errMsg);
		processBravoMapper.updateProcessBravo(processUpdate);

		// Update invoice status
		Invoice invoiceUpdate = new Invoice();
		invoiceUpdate.setId(process.getInvoiceId());
		invoiceUpdate.setStatusBravo(processStatus.value());
		invoiceMapper.updateInvoice(invoiceUpdate);

		// Push notification
		if (processStatus != ProcessBravoStatus.PROCESSING) {
			String invNo = process.getInvoiceNo();
			if (processStatus == ProcessBravoStatus.SUCCESS) {
				pushNotify(NotificationType.INFO, invNo);
			} else if (processStatus == ProcessBravoStatus.FAIL) {
				pushNotify(NotificationType.WARNING, invNo);
			}
		}
	}

	private void pushNotify(NotificationType type, String invNo) {
		Notification notification = new Notification();
		if (NotificationType.INFO == type) {
			notification.setTitle("Đồng bộ hóa đơn thành công " + invNo);
			notification.setType(NotificationType.INFO.value());
		} else {
			notification.setTitle("Đồng bộ hóa đơn thất bại " + invNo);
			notification.setType(NotificationType.WARNING.value());
		}
		notificationMapper.insertNotification(notification);
	}
}
