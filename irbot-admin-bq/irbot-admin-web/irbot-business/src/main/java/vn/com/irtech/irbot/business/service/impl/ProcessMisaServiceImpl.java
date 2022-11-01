package vn.com.irtech.irbot.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import vn.com.irtech.irbot.business.domain.ProcessMisa;
import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.dto.InvoiceInfo;
import vn.com.irtech.irbot.business.dto.ProcessMisaConfig;
import vn.com.irtech.irbot.business.dto.request.RobotSyncMisaReq;
import vn.com.irtech.irbot.business.dto.request.RobotSyncMisaReqDetail;
import vn.com.irtech.irbot.business.mapper.InvoiceDetailMapper;
import vn.com.irtech.irbot.business.mapper.InvoiceMapper;
import vn.com.irtech.irbot.business.mapper.ProcessMisaMapper;
import vn.com.irtech.irbot.business.mapper.RobotMapper;
import vn.com.irtech.irbot.business.mqtt.MqttService;
import vn.com.irtech.irbot.business.service.IProcessMisaService;
import vn.com.irtech.irbot.business.type.InvoiceType;
import vn.com.irtech.irbot.business.type.ModeRunType;
import vn.com.irtech.irbot.business.type.ProcessMisaStatus;
import vn.com.irtech.irbot.business.type.RobotServiceType;
import vn.com.irtech.irbot.business.type.RobotStatusType;
import vn.com.irtech.irbot.business.type.SyncType;

/**
 * Đồng bộ MISA Handling Service Business Layer
 *
 * @author irtech
 * @date 2022-01-18
 */
@Service
public class ProcessMisaServiceImpl implements IProcessMisaService {
	private static final Logger logger = LoggerFactory.getLogger(ProcessMisaServiceImpl.class);

	@Autowired
	private ProcessMisaMapper processMisaMapper;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private InvoiceMapper invoiceMapper;

	@Autowired
	private InvoiceDetailMapper invoiceDetailMapper;

	@Autowired
	private RobotMapper robotMapper;

	@Autowired
	private MqttService mqttService;

	/**
	 * Query Đồng bộ MISA
	 *
	 * @param id ID Đồng bộ MISA
	 * @return Đồng bộ MISA
	 */
	@Override
	public ProcessMisa selectProcessMisaById(Long id) {
		return processMisaMapper.selectProcessMisaById(id);
	}

	/**
	 * Query list Đồng bộ MISA
	 *
	 * @param processMisa Đồng bộ MISA
	 * @return Đồng bộ MISA
	 */
	@Override
	public List<ProcessMisa> selectProcessMisaList(ProcessMisa processMisa) {
		return processMisaMapper.selectProcessMisaList(processMisa);
	}

	/**
	 * Added Đồng bộ MISA
	 *
	 * @param processMisa Đồng bộ MISA
	 * @return result
	 */
	@Override
	public int insertProcessMisa(ProcessMisa processMisa) {
		processMisa.setCreateTime(DateUtils.getNowDate());
		return processMisaMapper.insertProcessMisa(processMisa);
	}

	/**
	 * Update Đồng bộ MISA
	 *
	 * @param processMisa Đồng bộ MISA
	 * @return result
	 */
	@Override
	public int updateProcessMisa(ProcessMisa processMisa) {
		processMisa.setUpdateTime(DateUtils.getNowDate());
		return processMisaMapper.updateProcessMisa(processMisa);
	}

	/**
	 * Delete object Đồng bộ MISA
	 *
	 * @param id ID of the data to be deleted
	 * @return result
	 */
	@Override
	public int deleteProcessMisaByIds(String ids) {
		return processMisaMapper.deleteProcessMisaByIds(Convert.toStrArray(ids));
	}

	/**
	 * Delete information Đồng bộ MISA
	 *
	 * @param id ID Đồng bộ MISA
	 * @return result
	 */
	@Override
	public int deleteProcessMisaById(Long id) {
		return processMisaMapper.deleteProcessMisaById(id);
	}

	@Override
	public void retry(String invoiceIds) throws Exception {
		// Get a robot available
		List<Robot> robots = robotMapper.selectRobotByService(RobotServiceType.SYNC_MISA.value(),
				RobotStatusType.AVAILABLE.value());
		// Case if have not any robot available
		if (CollectionUtils.isEmpty(robots)) {
			throw new Exception("Không tìm thấy robot khả dụng để làm lệnh!");
		}

		Long[] idInvoiceArr = Convert.toLongArray(invoiceIds);
		Date now = new Date();
		List<RobotSyncMisaReq> robotRequestList = new ArrayList<RobotSyncMisaReq>();
		for (Long invoiceId : idInvoiceArr) {
			Invoice invoice = invoiceMapper.selectInvoiceById(invoiceId);
			if (invoice == null) {
				logger.info("Invoice not exist : {}" + invoiceId);
				throw new Exception("Data không tồn tại, vui lòng thử lại sau!");
			}
			
			List<InvoiceInfo> invoiceInfoList = invoiceMapper.selectInvoiceInfoList(invoice);
			// Tinh tien chech lech
			for (InvoiceInfo invoiceInfo : invoiceInfoList) {
				invoiceInfo.setDiff(invoiceInfo.getTotalAmount() - invoiceInfo.getTotalAmountExactly());
			}
			
			ProcessMisa processSelect = new ProcessMisa();
			processSelect.setInvoiceId(invoiceId);
			List<ProcessMisa> processList = processMisaMapper.selectProcessMisaList(processSelect);
			ProcessMisa process = null;
			if (CollectionUtils.isNotEmpty(processList)) {
				process = processList.get(0);
				ProcessMisa processUpdate = new ProcessMisa();
				processUpdate.setId(process.getId());
				processUpdate.getParams().put("resetEndDate", true);
				processUpdate.setStatus(ProcessMisaStatus.SENT.value());
				processUpdate.getParams().put("resetError", true);
				processUpdate.setStartDate(now);
				processMisaMapper.updateProcessMisa(processUpdate);

			} else {
				process = new ProcessMisa();
				process.setInvoiceId(invoiceId);
				process.setInvoiceType(invoice.getType());
				process.setTaxCode(invoice.getTaxCode());
				process.setCustomerName(invoice.getCustomerName());
				process.setInvoiceDate(invoice.getInvoiceDate());
				process.setStatus(ProcessMisaStatus.SENT.value());
				if (ModeRunType.IMMEDIATELY.value().equals(invoice.getModeRun())) {
					process.setPlanStartDate(now);
				} else {
					process.setPlanStartDate(invoice.getPlanStartDate());
				}
				process.setStartDate(now);
				processMisaMapper.insertProcessMisa(process);
			}
			
			Invoice invoiceUpdate = new Invoice();
			invoiceUpdate.setId(invoiceId);
			invoiceUpdate.setStatusMisa(ProcessMisaStatus.SENT.value());
			invoiceMapper.updateInvoice(invoiceUpdate);

			RobotSyncMisaReq robotSyncMisaReq = processMisaReq(process.getId(), invoice, invoiceInfoList.get(0).getDiff());
			if (robotSyncMisaReq == null) {
				throw new Exception("Dữ liệu không hợp lệ, vui lòng thử lại sau!");
			}
			robotRequestList.add(robotSyncMisaReq);
		}

		if (CollectionUtils.isNotEmpty(robotRequestList)) {
			for (RobotSyncMisaReq process : robotRequestList) {
				requestRobot(process);
			}
		}
	}

	private RobotSyncMisaReq processMisaReq(Long processId, Invoice invoice, Long diff) {
		if (invoice == null) {
			return null;
		}

		// Get invoice detail
		InvoiceDetail invoiceDetailSelect = new InvoiceDetail();
		invoiceDetailSelect.setInvoiceId(invoice.getId());
		List<InvoiceDetail> invoiceDetailListBeforeMerge = invoiceDetailMapper
				.selectInvoiceDetailList(invoiceDetailSelect);

		if (CollectionUtils.isEmpty(invoiceDetailListBeforeMerge)) {
			return null;
		}

		List<InvoiceDetail> invoiceDetailList = new ArrayList<InvoiceDetail>(invoiceDetailListBeforeMerge);

		if (InvoiceType.SY.value().equals(invoice.getType())) {
			invoiceDetailList = mergeInvoiceDetail(invoiceDetailListBeforeMerge);
			if (CollectionUtils.isEmpty(invoiceDetailList)) {
				return null;
			}
		}

		RobotSyncMisaReq robotSyncMisaReq = new RobotSyncMisaReq();
		robotSyncMisaReq.setConfig(getProcessConfig());
		robotSyncMisaReq.setProcessId(processId);
		robotSyncMisaReq.setInvoiceId(invoice.getId());
		robotSyncMisaReq.setSyncId(SyncType.MISA.value());
		robotSyncMisaReq.setInvoiceDate(DateUtils.parseDateToStr("dd/MM/yyyy", invoice.getInvoiceDate()));
		robotSyncMisaReq.setCustName(invoice.getCustomerName());
		robotSyncMisaReq.setCustAddress(invoice.getAddress());
		robotSyncMisaReq.setTaxCode(invoice.getTaxCode());
		robotSyncMisaReq.setBuyerName(invoice.getBuyer());
		robotSyncMisaReq.setProcessQty(invoiceDetailList.size());
		robotSyncMisaReq.setTaxId(configService.selectConfigByKey("business.web.taxId"));
		//set diff
		if (diff != 0) {
			robotSyncMisaReq.setSurplus(true);
		} else {
			robotSyncMisaReq.setSurplus(false);
		}
		
		Long totalAmount = 0L;
		for (InvoiceDetail invoiceDetail : invoiceDetailList) {
			totalAmount += invoiceDetail.calcTotalAmount();
		}
		robotSyncMisaReq.setTotal(totalAmount);

		List<RobotSyncMisaReqDetail> processOrder = new ArrayList<RobotSyncMisaReqDetail>();
		for (InvoiceDetail invoiceDetail : invoiceDetailList) {
			RobotSyncMisaReqDetail processDetail = new RobotSyncMisaReqDetail();
			processDetail.setGift("1".equals(invoiceDetail.getPromotion()));
			processDetail.setProductName(invoiceDetail.getProductName());
			processDetail.setUnit(invoiceDetail.getUnit());
			processDetail.setQuantity(invoiceDetail.getQty());
			processDetail.setUnitPrice(invoiceDetail.getPrice());
			processDetail.setTaxDue(invoiceDetail.getTaxAmount());
			processDetail.setSubTotal(invoiceDetail.calcTotalAmount());
			processOrder.add(processDetail);
		}
		robotSyncMisaReq.setProcessOrder(processOrder);
		return robotSyncMisaReq;
	}

	private List<InvoiceDetail> mergeInvoiceDetail(List<InvoiceDetail> invoiceDetailList) {
		if (CollectionUtils.isEmpty(invoiceDetailList)) {
			return invoiceDetailList;
		}

		List<InvoiceDetail> cloneList = new ArrayList<InvoiceDetail>(invoiceDetailList);
		List<InvoiceDetail> result = new ArrayList<InvoiceDetail>();
		for (int i = 0; i < cloneList.size(); i++) {
			InvoiceDetail invoiceDetail = cloneList.get(i);

			if (invoiceDetail.getCode().length() > 8) {
				String prefixCode = invoiceDetail.getCode().substring(0, 8);
				String productName = invoiceDetail.getProductName();
				Long price = invoiceDetail.getPrice();
				for (int j = i + 1; j < cloneList.size(); j++) {
					InvoiceDetail invoiceDetailCheck = cloneList.get(j);
					if (invoiceDetailCheck.getCode().startsWith(prefixCode)) {
						//Kiem tra ten san pham va don gia co giong nhau khong
						if (!invoiceDetailCheck.getProductName().equals(productName) || !invoiceDetailCheck.getPrice().equals(price)) {
							return null;
						}
						// So luong
						Integer newQty = invoiceDetail.getQty() + invoiceDetailCheck.getQty();
						invoiceDetail.setQty(newQty);

						// Tien thue
						Long newTaxAmount = invoiceDetail.getTaxAmount() + invoiceDetailCheck.getTaxAmount();
						invoiceDetail.setTaxAmount(newTaxAmount);

						// Thanh tien
						Long newAmount = invoiceDetail.getAmount() + invoiceDetailCheck.getAmount();
						invoiceDetail.setAmount(newAmount);
						

						cloneList.remove(j);
						--j;
					}
				}
			}

			result.add(invoiceDetail);
		}
		return result;
	}

	private ProcessMisaConfig getProcessConfig() {
		ProcessMisaConfig processMisaConfig = new ProcessMisaConfig();
		processMisaConfig.setUsername(configService.selectConfigByKey("business.web.misa.username"));
		processMisaConfig.setPassword(configService.selectConfigByKey("business.web.misa.password"));
		processMisaConfig.setUrl(configService.selectConfigByKey("business.web.url"));
		processMisaConfig.setTaxCode(configService.selectConfigByKey("business.web.taxCode"));
		return processMisaConfig;
	}

	private void requestRobot(RobotSyncMisaReq process) {
		// Get a robot available
		List<Robot> robots = robotMapper.selectRobotByService(RobotServiceType.SYNC_MISA.value(),
				RobotStatusType.AVAILABLE.value());
		// Case if have not any robot available
		if (CollectionUtils.isEmpty(robots)) {
			String errMsg = "Không tìm thấy robot khả dụng để làm lệnh!";
			ProcessMisa processMisaUpdate = new ProcessMisa();
			processMisaUpdate.setId(process.getProcessId());
			processMisaUpdate.setStatus(ProcessMisaStatus.FAIL.value());
			processMisaUpdate.setEndDate(new Date());
			processMisaUpdate.setError(errMsg);
			processMisaMapper.updateProcessMisa(processMisaUpdate);

			Invoice invoiceUpdate = new Invoice();
			invoiceUpdate.setId(process.getInvoiceId());
			invoiceUpdate.setStatusMisa(ProcessMisaStatus.FAIL.value());
			invoiceMapper.updateInvoice(invoiceUpdate);
			return;
		}
		Robot robot = robots.get(0);
		
		requestRobot(robot.getUuid(), process);
	}
	
	private void requestRobot(String robotUuid, RobotSyncMisaReq process) {
		// Push Mqtt
		String message = new Gson().toJson(process);
		
		ProcessMisa processMisaUpdate = new ProcessMisa();
		processMisaUpdate.setId(process.getProcessId());
		
		process.getConfig().setPassword("********");
		process.getConfig().setUsername("******");
		process.getConfig().setTaxCode("***");
		
		String messageSecurity = new Gson().toJson(process);

		processMisaUpdate.setDataRequest(messageSecurity);
		processMisaUpdate.setRobotUuid(robotUuid);
		processMisaMapper.updateProcessMisa(processMisaUpdate);

		String topic = MqttService.TOPIC_ROBOT_BASE + "/" + robotUuid + "/process/request";
		logger.info("Send Mqtt topic {} with message {}", topic, message);
		try {
			mqttService.publish(topic, new MqttMessage(message.getBytes("UTF-8")));
		} catch (Exception e) {
			processMisaUpdate = new ProcessMisa();
			processMisaUpdate.setId(process.getProcessId());
			processMisaUpdate.setStatus(ProcessMisaStatus.FAIL.value());
			processMisaUpdate.setEndDate(new Date());
			processMisaUpdate.setError(e.getMessage());
			processMisaMapper.updateProcessMisa(processMisaUpdate);

			Invoice invoiceUpdate = new Invoice();
			invoiceUpdate.setId(process.getInvoiceId());
			invoiceUpdate.setStatusMisa(ProcessMisaStatus.FAIL.value());
			invoiceMapper.updateInvoice(invoiceUpdate);
			logger.error("Error when try sending mqtt message: " + e);
		}
	}

	@Override
	@Transactional
	public int sync() throws Exception {
		int result = 0;
		Date now = new Date();
		try {
			// get list process bravo với status = 0 (chua lam)
			ProcessMisa processMisaSelect = new ProcessMisa();
			processMisaSelect.setStatus(ProcessMisaStatus.NOTSEND.value());
			processMisaSelect.getParams().put("planStartDate", DateUtils.parseDateToStr("yyyyMMdd", now));
			List<ProcessMisa> processList = processMisaMapper.selectProcessMisaList(processMisaSelect);

			// check if empty
			if (CollectionUtils.isEmpty(processList)) {
				logger.info("Không có data!");
				logger.info("Sync Misa - Finish !");
				return 0;
			}

			// Find robot available
			List<Robot> robots = robotMapper.selectRobotByService(RobotServiceType.SYNC_MISA.value(),
					RobotStatusType.AVAILABLE.value());

			// Case if have not any robot available
			if (CollectionUtils.isEmpty(robots)) {
				logger.info("Không tìm thấy robot khả dụng để làm lệnh!");
				logger.info("Sync Misa - Finish !");
				throw new Exception("Không tìm thấy robot khả dụng để làm lệnh!");
			}

			Map<String, RobotSyncMisaReq> robotRequestMap = new HashMap<String, RobotSyncMisaReq>();
			for (ProcessMisa process : processList) {
				// Sửa ngày 01/04/2022: Mỗi lần chỉ gửi 1 lệnh cho robot
				if (robots.size() == 0) {
					break;
				}
				
				if (!checkPlanStartDate(process.getPlanStartDate())) {
					continue;
				}
				Invoice invoice = invoiceMapper.selectInvoiceById(process.getInvoiceId());
				if (invoice == null) {
					logger.info("Invoice not exist : {}" + process.getInvoiceId());
					continue;
				}
				
				
				List<InvoiceInfo> invoiceInfoList = invoiceMapper.selectInvoiceInfoList(invoice);
				// Tinh tien chech lech
				for (InvoiceInfo invoiceInfo : invoiceInfoList) {
					invoiceInfo.setDiff(invoiceInfo.getTotalAmount() - invoiceInfo.getTotalAmountExactly());
				}

				ProcessMisa processUpdate = new ProcessMisa();
				processUpdate.setId(process.getId());
				processUpdate.getParams().put("resetEndDate", true);
				processUpdate.setStatus(ProcessMisaStatus.SENT.value());
				processUpdate.getParams().put("resetError", true);
				processUpdate.setStartDate(now);
				processMisaMapper.updateProcessMisa(processUpdate);

				Invoice invoiceUpdate = new Invoice();
				invoiceUpdate.setId(process.getInvoiceId());
				invoiceUpdate.setStatusMisa(ProcessMisaStatus.SENT.value());
				invoiceMapper.updateInvoice(invoiceUpdate);

				RobotSyncMisaReq robotSyncMisaReq = processMisaReq(process.getId(), invoice, invoiceInfoList.get(0).getDiff());
				
				Robot robot = robots.get(0);
				robots.remove(0);
				robotRequestMap.put(robot.getUuid(), robotSyncMisaReq);
			}

			if (robotRequestMap != null && robotRequestMap.size() > 0) {
				for (Map.Entry<String, RobotSyncMisaReq> entry : robotRequestMap.entrySet()) {
				    requestRobot(entry.getKey(), entry.getValue());
				}
			}
			logger.info("Sync Misa - Finish !");
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(">>>>>> Error: " + e.getMessage());
			logger.info("Sync Misa - Finish !");
			throw e;
		}
	}

	// kiểm tra ngày bắt đầu bằng ngày hiện tại và thời gian bắt đầu <= thời gian
	// hiện tại của hệ thống.
	private static boolean checkPlanStartDate(Date planStartDate) {
		Date now = DateUtils.getNowDate();
		return (DateUtils.isSameDay(planStartDate, now) && !(planStartDate.after(now)));
	}
}
