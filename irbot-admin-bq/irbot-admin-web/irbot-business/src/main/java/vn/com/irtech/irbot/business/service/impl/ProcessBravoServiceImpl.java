package vn.com.irtech.irbot.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import vn.com.irtech.core.common.text.Convert;
import vn.com.irtech.core.common.utils.DateUtils;
import vn.com.irtech.core.system.service.ISysConfigService;
import vn.com.irtech.irbot.business.domain.Invoice;
import vn.com.irtech.irbot.business.domain.InvoiceDetail;
import vn.com.irtech.irbot.business.domain.ProcessBravo;
import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.dto.ProcessBravoConfig;
import vn.com.irtech.irbot.business.dto.request.RobotSyncBravoReq;
import vn.com.irtech.irbot.business.dto.request.RobotSyncBravoReqDetail;
import vn.com.irtech.irbot.business.mapper.InvoiceDetailMapper;
import vn.com.irtech.irbot.business.mapper.InvoiceMapper;
import vn.com.irtech.irbot.business.mapper.ProcessBravoMapper;
import vn.com.irtech.irbot.business.mapper.RobotMapper;
import vn.com.irtech.irbot.business.mqtt.MqttService;
import vn.com.irtech.irbot.business.service.IProcessBravoService;
import vn.com.irtech.irbot.business.type.InvoiceType;
import vn.com.irtech.irbot.business.type.ProcessBravoStatus;
import vn.com.irtech.irbot.business.type.RobotServiceType;
import vn.com.irtech.irbot.business.type.RobotStatusType;
import vn.com.irtech.irbot.business.type.SyncType;

/**
 * Đồng bộ BRAVO Handling Service Business Layer
 *
 * @author irtech
 * @date 2022-01-24
 */
@Service
public class ProcessBravoServiceImpl implements IProcessBravoService {

	private static final Logger logger = LoggerFactory.getLogger(ProcessBravoServiceImpl.class);

	@Autowired
	private ProcessBravoMapper processBravoMapper;

	@Autowired
	private RobotMapper robotMapper;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private InvoiceDetailMapper invoiceDetailMapper;

	@Autowired
	private InvoiceMapper invoiceMapper;

	@Autowired
	private MqttService mqttService;

	/**
	 * Query Đồng bộ BRAVO
	 *
	 * @param id ID Đồng bộ BRAVO
	 * @return Đồng bộ BRAVO
	 */
	@Override
	public ProcessBravo selectProcessBravoById(Long id) {
		return processBravoMapper.selectProcessBravoById(id);
	}

	/**
	 * Query list Đồng bộ BRAVO
	 *
	 * @param processBravo Đồng bộ BRAVO
	 * @return Đồng bộ BRAVO
	 */
	@Override
	public List<ProcessBravo> selectProcessBravoList(ProcessBravo processBravo) {
		return processBravoMapper.selectProcessBravoList(processBravo);
	}

	/**
	 * Added Đồng bộ BRAVO
	 *
	 * @param processBravo Đồng bộ BRAVO
	 * @return result
	 */
	@Override
	public int insertProcessBravo(ProcessBravo processBravo) {
		processBravo.setCreateTime(DateUtils.getNowDate());
		return processBravoMapper.insertProcessBravo(processBravo);
	}

	/**
	 * Update Đồng bộ BRAVO
	 *
	 * @param processBravo Đồng bộ BRAVO
	 * @return result
	 */
	@Override
	public int updateProcessBravo(ProcessBravo processBravo) {
		processBravo.setUpdateTime(DateUtils.getNowDate());
		return processBravoMapper.updateProcessBravo(processBravo);
	}

	/**
	 * Delete object Đồng bộ BRAVO
	 *
	 * @param id ID of the data to be deleted
	 * @return result
	 */
	@Override
	public int deleteProcessBravoByIds(String ids) {
		return processBravoMapper.deleteProcessBravoByIds(Convert.toStrArray(ids));
	}

	/**
	 * Delete information Đồng bộ BRAVO
	 *
	 * @param id ID Đồng bộ BRAVO
	 * @return result
	 */
	@Override
	public int deleteProcessBravoById(Long id) {
		return processBravoMapper.deleteProcessBravoById(id);
	}

	@Override
	@Transactional
	public int sync() throws Exception {
		int result = 0;
		Date now = new Date();
		try {
			// get list process bravo với status = 0 (chua lam)
			ProcessBravo processBravoSelect = new ProcessBravo();
			processBravoSelect.setStatus(ProcessBravoStatus.NOTSEND.value());
			List<ProcessBravo> processList = processBravoMapper.selectProcessBravoList(processBravoSelect);

			// check if empty
			if (CollectionUtils.isEmpty(processList)) {
				logger.info("Không có data!");
				logger.info("Sync Bravo - Finish !");
				return 0;
			}

			// Find robot available
			List<Robot> robots = robotMapper.selectRobotByService(RobotServiceType.SYNC_BRAVO.value(),
					RobotStatusType.AVAILABLE.value());

			// Case if have not any robot available
			if (CollectionUtils.isEmpty(robots)) {
				logger.info("Không tìm thấy robot khả dụng để làm lệnh!");
				logger.info("Sync Bravo - Finish !");
				throw new Exception("Không tìm thấy robot khả dụng để làm lệnh!");
			}

			List<RobotSyncBravoReq> robotRequestList = new ArrayList<RobotSyncBravoReq>();
			for (ProcessBravo process : processList) {
				Invoice invoice = invoiceMapper.selectInvoiceById(process.getInvoiceId());
				if (invoice == null) {
					logger.info("Invoice not exist : {}" + process.getInvoiceId());
					continue;
				}

				ProcessBravo processUpdate = new ProcessBravo();
				processUpdate.setId(process.getId());
				processUpdate.getParams().put("resetEndDate", true);
				processUpdate.setStatus(ProcessBravoStatus.SENT.value());
				processUpdate.getParams().put("resetError", true);
				processUpdate.setStartDate(now);
				processBravoMapper.updateProcessBravo(processUpdate);

				Invoice invoiceUpdate = new Invoice();
				invoiceUpdate.setId(process.getInvoiceId());
				invoiceUpdate.setStatusBravo(ProcessBravoStatus.SENT.value());
				invoiceMapper.updateInvoice(invoiceUpdate);

				RobotSyncBravoReq robotSyncBravoReq = processBravoReq(process.getId(), invoice);
				robotRequestList.add(robotSyncBravoReq);
			}

			if (CollectionUtils.isNotEmpty(robotRequestList)) {
				for (RobotSyncBravoReq process : robotRequestList) {
					requestRobot(process);
				}
			}
			logger.info("Sync Bravo - Finish !");
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(">>>>>> Error: " + e.getMessage());
			logger.info("Sync Bravo - Finish !");
			throw e;
		}

	}

	private RobotSyncBravoReq processBravoReq(Long processId, Invoice invoice) {
		if (invoice == null) {
			return null;
		}

		// Get invoice detail
		InvoiceDetail invoiceDetailSelect = new InvoiceDetail();
		invoiceDetailSelect.setInvoiceId(invoice.getId());
		List<InvoiceDetail> invoiceDetailList = invoiceDetailMapper.selectInvoiceDetailList(invoiceDetailSelect);

		if (CollectionUtils.isEmpty(invoiceDetailList)) {
			return null;
		}

		RobotSyncBravoReq robotSyncBravoReq = new RobotSyncBravoReq();
		robotSyncBravoReq.setConfig(getProcessConfig());
		robotSyncBravoReq.setProcessId(processId);
		robotSyncBravoReq.setSyncId(SyncType.BRAVO.value());
		robotSyncBravoReq.setInvoiceDate(DateUtils.parseDateToStr("dd/MM/yyyy", invoice.getInvoiceDate()));
		robotSyncBravoReq.setInvoiceNo(invoice.getInvoiceNo());
		String custId = invoiceDetailList.get(0).getChannelCode();
		robotSyncBravoReq.setCustId(custId);
		robotSyncBravoReq.setBuyerName(invoice.getBuyer());
		//get invoice type
		String invoiceType = "";
		if (InvoiceType.LE_ONLINE.value().equals(invoice.getType())) {
			invoiceType = "online";
		} else if (InvoiceType.LE_STORE.value().equals(invoice.getType())) {
			invoiceType = "lẻ";
		} else if (InvoiceType.SY.value().equals(invoice.getType())) {
			invoiceType = "sỉ";
		}
		String description = "Xuất bán " + invoiceType + " " + invoice.getBuyer() + " HĐ số " + invoice.getInvoiceNo() + " ngày " + DateUtils.parseDateToStr("dd/MM/yyyy", invoice.getInvoiceDate());
		robotSyncBravoReq.setDescription(description);
		robotSyncBravoReq.setTaxId(configService.selectConfigByKey("business.web.taxId"));
		Long totalAmount = 0L;
		for (InvoiceDetail invoiceDetail : invoiceDetailList) {
			totalAmount += invoiceDetail.calcTotalAmount();
		}
		robotSyncBravoReq.setTotal(totalAmount);
		robotSyncBravoReq.setProcessQty(invoiceDetailList.size());
		List<RobotSyncBravoReqDetail> processOrder = new ArrayList<RobotSyncBravoReqDetail>();
		for (InvoiceDetail invoiceDetail : invoiceDetailList) {
			RobotSyncBravoReqDetail processDetail = new RobotSyncBravoReqDetail();
			processDetail.setProductId(invoiceDetail.getCode());
			processDetail.setQuantity(invoiceDetail.getQty());
			processDetail.setUnitPrice(invoiceDetail.getPrice());
			processDetail.setWareHouse(invoiceDetail.getStoreCode());
			processOrder.add(processDetail);
		}
		robotSyncBravoReq.setProcessOrder(processOrder);
		return robotSyncBravoReq;
	}

	private void requestRobot(RobotSyncBravoReq process) {
		// Get a robot available
		List<Robot> robots = robotMapper.selectRobotByService(RobotServiceType.SYNC_BRAVO.value(),
				RobotStatusType.AVAILABLE.value());
		// Case if have not any robot available
		if (CollectionUtils.isEmpty(robots)) {
			String errMsg = "Không tìm thấy robot khả dụng để làm lệnh!";
			ProcessBravo processUpdate = new ProcessBravo();
			processUpdate.setId(process.getProcessId());
			processUpdate.setStatus(ProcessBravoStatus.FAIL.value());
			processUpdate.setEndDate(new Date());
			processUpdate.setError(errMsg);
			processBravoMapper.updateProcessBravo(processUpdate);

			Invoice invoiceUpdate = new Invoice();
			invoiceUpdate.setId(process.getInvoiceId());
			invoiceUpdate.setStatusBravo(ProcessBravoStatus.FAIL.value());
			invoiceMapper.updateInvoice(invoiceUpdate);
			return;
		}
		Robot robot = robots.get(0);

		// Push Mqtt
		String message = new Gson().toJson(process);

		ProcessBravo processUpdate = new ProcessBravo();
		processUpdate.setId(process.getProcessId());
		processUpdate.setDataRequest(message);
		processUpdate.setRobotUuid(robot.getUuid());
		processBravoMapper.updateProcessBravo(processUpdate);

		String topic = MqttService.TOPIC_ROBOT_BASE + "/" + robot.getUuid() + "/process/request";
		logger.info("Send Mqtt topic {} with message {}", topic, message);
		try {
			mqttService.publish(topic, new MqttMessage(message.getBytes("UTF-8")));
		} catch (Exception e) {
			processUpdate = new ProcessBravo();
			processUpdate.setId(process.getProcessId());
			processUpdate.setStatus(ProcessBravoStatus.FAIL.value());
			processUpdate.setEndDate(new Date());
			processUpdate.setError(e.getMessage());
			processBravoMapper.updateProcessBravo(processUpdate);

			Invoice invoiceUpdate = new Invoice();
			invoiceUpdate.setId(process.getInvoiceId());
			invoiceUpdate.setStatusBravo(ProcessBravoStatus.FAIL.value());
			invoiceMapper.updateInvoice(invoiceUpdate);
			logger.error("Error when try sending mqtt message: " + e);
		}
	}

	private ProcessBravoConfig getProcessConfig() {
		ProcessBravoConfig processConfig = new ProcessBravoConfig();
		processConfig.setUsername(configService.selectConfigByKey("business.web.bravo.username"));
		processConfig.setPassword(configService.selectConfigByKey("business.web.bravo.password"));
		return processConfig;
	}

	@Override
	@Transactional
	public void retry(String invoiceIds) throws Exception {
		// Get a robot available
		List<Robot> robots = robotMapper.selectRobotByService(RobotServiceType.SYNC_BRAVO.value(),
				RobotStatusType.AVAILABLE.value());
		// Case if have not any robot available
		if (CollectionUtils.isEmpty(robots)) {
			throw new Exception("Không tìm thấy robot khả dụng để làm lệnh!");
		}

		Long[] idInvoiceArr = Convert.toLongArray(invoiceIds);
		Date now = new Date();
		List<RobotSyncBravoReq> robotRequestList = new ArrayList<RobotSyncBravoReq>();
		for (Long invoiceId : idInvoiceArr) {
			Invoice invoice = invoiceMapper.selectInvoiceById(invoiceId);
			if (invoice == null) {
				logger.info("Invoice not exist : {}" + invoiceId);
				throw new Exception("Data không tồn tại, vui lòng thử lại sau!");
			}
			
			if (invoice.getInvoiceNo() == null) {
				logger.info("InvoiceNo not exist : {}" + invoiceId);
				throw new Exception("Số hóa đơn không tồn tại, vui lòng thử lại sau!");
			}
			
			ProcessBravo processSelect = new ProcessBravo();
			processSelect.setInvoiceId(invoiceId);
			List<ProcessBravo> processList = processBravoMapper.selectProcessBravoList(processSelect);
			ProcessBravo process = null;
			if (CollectionUtils.isNotEmpty(processList)) {
				process = processList.get(0);
				ProcessBravo processUpdate = new ProcessBravo();
				processUpdate.setId(process.getId());
				processUpdate.getParams().put("resetEndDate", true);
				processUpdate.setStatus(ProcessBravoStatus.SENT.value());
				processUpdate.getParams().put("resetError", true);
				processUpdate.setStartDate(now);
				processBravoMapper.updateProcessBravo(processUpdate);

			} else {
				process = new ProcessBravo();
				process.setInvoiceId(invoiceId);
				process.setInvoiceType(invoice.getType());
				process.setTaxCode(invoice.getTaxCode());
				process.setCustomerName(invoice.getCustomerName());
				process.setInvoiceDate(invoice.getInvoiceDate());
				process.setStatus(ProcessBravoStatus.SENT.value());
				process.setStartDate(now);
				processBravoMapper.insertProcessBravo(process);
			}

			Invoice invoiceUpdate = new Invoice();
			invoiceUpdate.setId(invoiceId);
			invoiceUpdate.setStatusBravo(ProcessBravoStatus.SENT.value());
			invoiceMapper.updateInvoice(invoiceUpdate);

			RobotSyncBravoReq robotSyncBravoReq = processBravoReq(process.getId(), invoice);
			robotRequestList.add(robotSyncBravoReq);
		}

		if (CollectionUtils.isNotEmpty(robotRequestList)) {
			for (RobotSyncBravoReq process : robotRequestList) {
				requestRobot(process);
			}
		}
	}
}
