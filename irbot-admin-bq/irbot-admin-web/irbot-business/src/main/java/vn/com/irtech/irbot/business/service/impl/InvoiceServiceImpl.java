package vn.com.irtech.irbot.business.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import vn.com.irtech.core.common.config.Global;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.text.Convert;
import vn.com.irtech.core.common.utils.DateUtils;
import vn.com.irtech.core.common.utils.bean.BeanUtils;
import vn.com.irtech.core.common.utils.file.FileUtils;
import vn.com.irtech.irbot.business.domain.Invoice;
import vn.com.irtech.irbot.business.domain.InvoiceDetail;
import vn.com.irtech.irbot.business.domain.InvoiceUploadHistory;
import vn.com.irtech.irbot.business.domain.ProcessMisa;
import vn.com.irtech.irbot.business.dto.InvoiceImport;
import vn.com.irtech.irbot.business.dto.InvoiceInfo;
import vn.com.irtech.irbot.business.dto.InvoiceSplitItem;
import vn.com.irtech.irbot.business.dto.response.ResultImportInvoiceFileRes;
import vn.com.irtech.irbot.business.mapper.InvoiceDetailMapper;
import vn.com.irtech.irbot.business.mapper.InvoiceMapper;
import vn.com.irtech.irbot.business.mapper.InvoiceUploadHistoryMapper;
import vn.com.irtech.irbot.business.mapper.ProcessBravoMapper;
import vn.com.irtech.irbot.business.mapper.ProcessMisaMapper;
import vn.com.irtech.irbot.business.service.IExcelService;
import vn.com.irtech.irbot.business.service.IInvoiceService;
import vn.com.irtech.irbot.business.type.InvoiceStatus;
import vn.com.irtech.irbot.business.type.InvoiceType;
import vn.com.irtech.irbot.business.type.ModeRunType;
import vn.com.irtech.irbot.business.type.ProcessBravoStatus;
import vn.com.irtech.irbot.business.type.ProcessMisaStatus;

/**
 * Quan ly hoa don Handling Service Business Layer
 *
 * @author irtech
 * @date 2022-01-10
 */
@Service
public class InvoiceServiceImpl implements IInvoiceService {

	private static final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

	@Autowired
	private InvoiceMapper invoiceMapper;

	@Autowired
	private InvoiceDetailMapper invoiceDetailMapper;

	@Autowired
	private IExcelService excelService;

	@Autowired
	private InvoiceUploadHistoryMapper invoiceUploadHistoryMapper;

	@Autowired
	private ProcessMisaMapper processMisaMapper;

	@Autowired
	private ProcessBravoMapper processBravoMapper;

	/**
	 * Query Quan ly hoa don
	 *
	 * @param id ID Quan ly hoa don
	 * @return Quan ly hoa don
	 */
	@Override
	public Invoice selectInvoiceById(Long id) {
		return invoiceMapper.selectInvoiceById(id);
	}

	/**
	 * Query list Quan ly hoa don
	 *
	 * @param invoice Quan ly hoa don
	 * @return Quan ly hoa don
	 */
	@Override
	public List<Invoice> selectInvoiceList(Invoice invoice) {
		return invoiceMapper.selectInvoiceList(invoice);
	}

	/**
	 * Added Quan ly hoa don
	 *
	 * @param invoice Quan ly hoa don
	 * @return result
	 */
	@Override
	public int insertInvoice(Invoice invoice) {
		invoice.setCreateTime(DateUtils.getNowDate());
		return invoiceMapper.insertInvoice(invoice);
	}

	/**
	 * Update Quan ly hoa don
	 *
	 * @param invoice Quan ly hoa don
	 * @return result
	 */
	@Override
	public int updateInvoice(Invoice invoice) {
		invoice.setUpdateTime(DateUtils.getNowDate());
		return invoiceMapper.updateInvoice(invoice);
	}

	/**
	 * Delete object Quan ly hoa don
	 *
	 * @param id ID of the data to be deleted
	 * @return result
	 */
	@Override
	@Transactional
	public int deleteInvoiceByIds(String ids) {
		String idArr[] = Convert.toStrArray(ids);
		// delete detail invoice
		invoiceDetailMapper.deleteInvoiceDetailByInvoiceIds(idArr);

		// delete process history
		processMisaMapper.deleteProcessMisaByInvoiceIds(idArr);

		processBravoMapper.deleteProcessBravoByInvoiceIds(idArr);

		return invoiceMapper.deleteInvoiceByIds(idArr);
	}

	/**
	 * Delete information Quan ly hoa don
	 *
	 * @param id ID Quan ly hoa don
	 * @return result
	 */
	@Override
	public int deleteInvoiceById(Long id) {

		return invoiceMapper.deleteInvoiceById(id);
	}

	@Override
	@Transactional
	public AjaxResult uploadInvoiceFile(MultipartFile file, InvoiceType invoiceType, String userName, Date invDate,
			Integer startHour, Integer endHour, ModeRunType modeRun) throws Exception {

		ResultImportInvoiceFileRes resultImport = excelService.importInvoiceFile(file.getInputStream(), invoiceType);

		if (resultImport == null) {
			throw new Exception("File không có dữ liệu hoặc không đúng định dạng, vui lòng kiểm tra lại!");
		}

		List<InvoiceImport> invoiceImportList = resultImport.getListInvoice();

		// Truong hop hoa don khach sy, xu ly kiem tra hoa don hop le
		if (InvoiceType.SY == invoiceType) {
			List<String> errors = new ArrayList<String>();
			errors = validInvoice(invoiceImportList);
			resultImport.getErrors().addAll(errors);
		}

		// Truong hop la hoa don khach le cua hang, xu ly tach hoa don
		if (InvoiceType.LE_STORE == invoiceType) {
			invoiceImportList = processRetailStoreInvoice(invoiceImportList);
		}

		// Luu file upload vao file server: {basePath}/12354452/abc.xlsx
		// Local resource path
		String localPath = Global.getBasePath();
		// Get current timestamp
		String timeStamp = System.currentTimeMillis() + "";
		String fileName = file.getOriginalFilename();
		String filePath = "/upload/" + timeStamp;
		String fullFilePath = localPath + filePath + "/" + fileName;
		// Save file
		FileUtils.copyInputStreamToFile(file.getInputStream(), new File(fullFilePath));

		// Luu vao database, filepath: /12354452, filename: abc.xlsx
		// Insert vao table invoice_upload_history
		InvoiceUploadHistory invoiceUpload = new InvoiceUploadHistory();
		invoiceUpload.setInvoiceType(invoiceType.value());
		invoiceUpload.setFileName(fileName);
		invoiceUpload.setFilePath(filePath);
		invoiceUpload.setCreateBy(userName);
		invoiceUploadHistoryMapper.insertInvoiceUploadHistory(invoiceUpload);
		// Insert vao table invoice, invoice_detail
		for (InvoiceImport invoiceImport : invoiceImportList) {
			invoiceImport.setModeRun(modeRun.value());
			if (ModeRunType.SCHEDULE == modeRun) {
				// Random thoi gian xuat hoa don
				Date now = new Date();
				String currentDaySt = DateUtils.parseDateToStr("yyyy-MM-dd", now);
				String invDaySt = DateUtils.parseDateToStr("yyyy-MM-dd", invDate);
				if (currentDaySt.equals(invDaySt)) {
					startHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1;
				}

				int randomHour = RandomUtils.nextInt(startHour, endHour);
				invoiceImport.setPlanStartDate(
						DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", String.format("%s %s:00:00", invDaySt, randomHour)));
				invoiceImport.setRandomIndex(RandomUtils.nextInt(0, 9));
			} else {
				Date now = new Date();
				invoiceImport.setPlanStartDate(now);
				invoiceImport.setRandomIndex(0);
			}

			invoiceImport.setInvoiceUploadId(invoiceUpload.getId());
			invoiceImport.setType(invoiceType.value());
			invoiceImport.setCreateBy(userName);
			invoiceImport.setStatus(InvoiceStatus.NOTSEND.value());
			invoiceImport.setStatusMisa(ProcessMisaStatus.NOTSEND.value());
			invoiceImport.setStatusBravo(ProcessBravoStatus.NOTSEND.value());
			invoiceMapper.insertInvoice(invoiceImport);
			for (InvoiceDetail invoiceDetail : invoiceImport.getDetails()) {
				invoiceDetail.setInvoiceId(invoiceImport.getId());
				invoiceDetail.setTaxAmount(invoiceDetail.getTaxAmount());
//				invoiceDetail.setTotalAmount(invoiceDetail.getTotalAmount());
				invoiceDetailMapper.insertInvoiceDetail(invoiceDetail);
			}
		}

		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("uploadId", invoiceUpload.getId());
		ajaxResult.put("errors", resultImport.getErrors());

		return ajaxResult;
	}

	@Override
	public List<InvoiceInfo> selectInvoiceInfoList(Invoice invoice) {
		List<InvoiceInfo> invoiceInfoList = invoiceMapper.selectInvoiceInfoList(invoice);

		// Tinh tien chech lech
		for (InvoiceInfo invoiceInfo : invoiceInfoList) {
			invoiceInfo.setDiff(invoiceInfo.getTotalAmount() - invoiceInfo.getTotalAmountExactly());
		}

		return invoiceInfoList;
	}

	/**
	 * Tach hoa don khach le cua hang
	 * 
	 * @param invoiceList
	 */
	private List<InvoiceImport> processRetailStoreInvoice(List<InvoiceImport> invoiceList) {
		if (CollectionUtils.isEmpty(invoiceList)) {
			return invoiceList;
		}

		List<InvoiceImport> subInvoiceList = new ArrayList<InvoiceImport>();

		for (InvoiceImport invoiceImport : invoiceList) {
			// Tinh tong thanh toan
			Long totalAmount = invoiceImport.calcTotalAmount();
			if (totalAmount <= 0L) {
				subInvoiceList.add(invoiceImport);
				continue;
			}

			int numOfProducts = invoiceImport.getDetails().size();

			List<InvoiceSplitItem> invoiceSplitItemList = new ArrayList<InvoiceSplitItem>();
			if (totalAmount < (10 * 1000 * 1000)) {
				invoiceSplitItemList.add(new InvoiceSplitItem(2, (int) Math.round((0.3 * numOfProducts) / 2)));
			} else if (totalAmount < (20 * 1000 * 1000)) {
				invoiceSplitItemList.add(new InvoiceSplitItem(3, 2));
				invoiceSplitItemList.add(new InvoiceSplitItem(2, 4));
			} else if (totalAmount < (30 * 1000 * 1000)) {
				invoiceSplitItemList.add(new InvoiceSplitItem(3, 4));
				invoiceSplitItemList.add(new InvoiceSplitItem(2, 7));
			} else {
				invoiceSplitItemList.add(new InvoiceSplitItem(5, 1));
				invoiceSplitItemList.add(new InvoiceSplitItem(4, 1));
				invoiceSplitItemList.add(new InvoiceSplitItem(3, 4));
				invoiceSplitItemList.add(new InvoiceSplitItem(2, 6));
			}

			subInvoiceList.addAll(splitRetailStoreInvoice(invoiceImport, invoiceSplitItemList));
		}

		return subInvoiceList;
	}

	private List<InvoiceImport> splitRetailStoreInvoice(InvoiceImport invoice,
			List<InvoiceSplitItem> invoiceSplitItemList) {
		InvoiceImport cloneInvoice = new InvoiceImport();
		BeanUtils.copyBeanProp(cloneInvoice, invoice);
		List<InvoiceImport> result = new ArrayList<InvoiceImport>();

		for (InvoiceSplitItem invoiceSplitItem : invoiceSplitItemList) {
			for (int i = 1; i <= invoiceSplitItem.getMaxInvoice(); i++) {
				if (cloneInvoice.getDetails().size() >= invoiceSplitItem.getTotalProductPerInvoice()) {
					InvoiceImport inv = new InvoiceImport();
					BeanUtils.copyBeanProp(inv, cloneInvoice);
					inv.setDetails(new ArrayList<InvoiceDetail>());
					for (int j = 1; j <= invoiceSplitItem.getTotalProductPerInvoice(); j++) {
						inv.getDetails().add(cloneInvoice.getDetails().get(0));
						cloneInvoice.getDetails().remove(0);
					}
					result.add(inv);
				}
			}
		}

		if (CollectionUtils.isNotEmpty(cloneInvoice.getDetails())) {
			for (InvoiceDetail invDetail : cloneInvoice.getDetails()) {
				InvoiceImport inv = new InvoiceImport();
				BeanUtils.copyBeanProp(inv, cloneInvoice);
				inv.setDetails(new ArrayList<InvoiceDetail>());
				inv.getDetails().add(invDetail);
				result.add(inv);
			}
		}
		return result;
	}

	@Override
	@Transactional
	public int sendRobotProcess(Long uploadId) {
		// TODO Auto-generated method stub
		Invoice inv = new Invoice();
		inv.setInvoiceUploadId(uploadId);
		List<Invoice> listInv = invoiceMapper.selectInvoiceList(inv);

		if (CollectionUtils.isEmpty(listInv)) {
			return 0;
		}

		Date now = new Date();
		int result = 0;
		for (Invoice invoice : listInv) {
			ProcessMisa processMisa = new ProcessMisa();
			processMisa.setInvoiceId(invoice.getId());
			processMisa.setInvoiceType(invoice.getType());
			processMisa.setTaxCode(invoice.getTaxCode());
			processMisa.setCustomerName(invoice.getCustomerName());
			processMisa.setInvoiceDate(invoice.getInvoiceDate());
			processMisa.setStatus(ProcessMisaStatus.NOTSEND.value());
			if (invoice.getModeRun() == ModeRunType.SCHEDULE.value()) {
				processMisa.setPlanStartDate(invoice.getPlanStartDate());
			} else if (invoice.getModeRun() == ModeRunType.IMMEDIATELY.value()) {
				processMisa.setPlanStartDate(now);
			}
			processMisaMapper.insertProcessMisa(processMisa);

			Invoice invoiceUpdate = new Invoice();
			invoiceUpdate.setId(invoice.getId());
			invoiceUpdate.setStatus(InvoiceStatus.SENT.value());
			invoiceMapper.updateInvoice(invoiceUpdate);
			result++;
		}
		return result;
	}

	private List<String> validInvoice(List<InvoiceImport> invoiceList) {
		List<String> errors = new ArrayList<String>();

		for (InvoiceImport invoiceImport : invoiceList) {
			List<InvoiceDetail> InvDetailList = new ArrayList<InvoiceDetail>(invoiceImport.getDetails());
			for (int i = 0; i < InvDetailList.size(); i++) {
				InvoiceDetail invoiceDetail = InvDetailList.get(i);

				if (invoiceDetail.getCode().length() > 8) {
					String prefixCode = invoiceDetail.getCode().substring(0, 8);
					String productName = invoiceDetail.getProductName();
					Long price = invoiceDetail.getPrice();
					for (int j = i + 1; j < InvDetailList.size(); j++) {

						InvoiceDetail invoiceDetailCheck = InvDetailList.get(j);
						if (invoiceDetailCheck.getCode().startsWith(prefixCode)) {
							// Kiem tra ten san pham va don gia co giong nhau khong
							if (!invoiceDetailCheck.getProductName().equals(productName)) {
								errors.add("Sản phẩm: " + invoiceDetailCheck.getProductName()
										+ " không cùng tên để gộp mã!");
							}

							if (!invoiceDetailCheck.getPrice().equals(price)) {
								errors.add("Đơn giá: " + invoiceDetailCheck.getPrice() + " không đúng để gộp mã!");
							}
							InvDetailList.remove(j);
							--j;
						}
					}
				}
			}
		}
		return errors;
	}

	@Override
	public boolean checkDataSendRobot(Long uploadId) {
		// TODO Auto-generated method stub
		Boolean result = false;
		Invoice inv = new Invoice();
		inv.setInvoiceUploadId(uploadId);

		List<InvoiceInfo> invoiceInfoList = invoiceMapper.selectInvoiceInfoList(inv);
		// Tinh tien chech lech
		for (InvoiceInfo invoiceInfo : invoiceInfoList) {
			invoiceInfo.setDiff(invoiceInfo.getTotalAmount() - invoiceInfo.getTotalAmountExactly());
			if (invoiceInfo.getDiff() != 0) {
				result = true;
			}
		}

		return result;
	}

	@Override
	public Long randomIndexInvoice(Long uploadId) {
		// TODO Auto-generated method stub
		Invoice inv = new Invoice();
		inv.setInvoiceUploadId(uploadId);
	
		List<InvoiceInfo> invoiceInfoList = invoiceMapper.selectInvoiceInfoList(inv);
		
		for (InvoiceInfo invoiceInfo : invoiceInfoList) {
			invoiceInfo.setRandomIndex(RandomUtils.nextInt(0,9));
			invoiceMapper.updateInvoice(invoiceInfo);
		}
		
		return uploadId;
	}

	@Override
	@Transactional
	public void mergeInv(String ids) throws Exception {
		// Kiem tra trung khach hang moi cho merge
		Long[] idInvoiceArr = Convert.toLongArray(ids);
		
		Set<String> setToCheck = new HashSet<String>();
		for (int i = 0; i < idInvoiceArr.length; i++) {
			Invoice invoice = invoiceMapper.selectInvoiceById(idInvoiceArr[i]);
			String custName = invoice.getCustomerName();
			String buyer = invoice.getBuyer();
			setToCheck.add(custName + "-"  + buyer);

		}
		if (setToCheck.size() != 1) {
			throw new Exception("Tên khách hàng hoặc Người mua hàng không trùng nhau!");
		}
		
		for (int i = 1; i < idInvoiceArr.length; i++ ) {
			
			InvoiceDetail invoiceDetailSelect = new InvoiceDetail();
			invoiceDetailSelect.setInvoiceId(idInvoiceArr[i]);
			List<InvoiceDetail> invDetailList = invoiceDetailMapper.selectInvoiceDetailList(invoiceDetailSelect);
			if (CollectionUtils.isNotEmpty(invDetailList)) {
				for (InvoiceDetail invDetail :invDetailList) {
					InvoiceDetail invoiceDetailUpdate = new InvoiceDetail();
					invoiceDetailUpdate.setId(invDetail.getId());
					invoiceDetailUpdate.setInvoiceId(idInvoiceArr[0]);
					invoiceDetailMapper.updateInvoiceDetail(invoiceDetailUpdate);
				}
			}
			
			invoiceMapper.deleteInvoiceById(idInvoiceArr[i]);
		}
	}
}
