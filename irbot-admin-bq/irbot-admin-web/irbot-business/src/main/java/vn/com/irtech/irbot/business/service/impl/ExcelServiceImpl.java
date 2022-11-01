package vn.com.irtech.irbot.business.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import vn.com.irtech.core.common.text.Convert;
import vn.com.irtech.core.common.utils.DateUtils;
import vn.com.irtech.core.common.utils.StringUtils;
import vn.com.irtech.irbot.business.domain.Invoice;
import vn.com.irtech.irbot.business.domain.InvoiceDetail;
import vn.com.irtech.irbot.business.dto.InvoiceImport;
import vn.com.irtech.irbot.business.dto.response.ResultImportInvoiceFileRes;
import vn.com.irtech.irbot.business.service.IExcelService;
import vn.com.irtech.irbot.business.type.InvoiceType;

@Service
public class ExcelServiceImpl implements IExcelService {

	private static final Logger log = LoggerFactory.getLogger(ExcelServiceImpl.class);

	@Override
	public ResultImportInvoiceFileRes importInvoiceFile(InputStream is, InvoiceType invoiceType) {
		ResultImportInvoiceFileRes result = new ResultImportInvoiceFileRes();
		List<String> errors = new ArrayList<String>();
		Workbook wb = null;
		Sheet sheet = null;
		List<InvoiceImport> list = new ArrayList<InvoiceImport>();
		try {
			wb = WorkbookFactory.create(is);
			sheet = wb.getSheetAt(0);

			if (sheet == null) {
				log.error("sheet not exist");
				return null;
			}

			int rows = sheet.getPhysicalNumberOfRows();

			if (rows <= 1) {
				log.error("Data empty");
				return null;
			}

			String currentInvoiceNo = "";
			InvoiceImport invoiceImport = null;
			
			for (int i = 1; i <= rows; i++) {
				// The data is taken from the second row, and the default first row is the
				// header.
				Row row = sheet.getRow(i);
				// Check if the current line is empty
				if (isRowEmpty(row)) {
					continue;
				}

				String invoiceNo = getCellValue(row, 0).toString();
				if (StringUtils.isBlank(invoiceNo)) {
					continue;
				}
				// kiem tra so thu tu hoa don kieu so hay khong
				if (!NumberUtils.isParsable(invoiceNo)) {
					errors.add("Dòng [" + (i+1) + "]: Số thứ tự hóa đơn không hợp lệ!");
				}
				
				if (!invoiceNo.equals(currentInvoiceNo)) {
					invoiceImport = new InvoiceImport();
					invoiceImport.setType(invoiceType.value());

					String taxCode = getCellValue(row, 4).toString();
					invoiceImport.setTaxCode(taxCode);

					String customerName = getCellValue(row, 2).toString();
					invoiceImport.setCustomerName(customerName);

					String address = getCellValue(row, 3).toString();
					invoiceImport.setAddress(address);

					String buyer = getCellValue(row, 5).toString();
					if (StringUtils.isBlank(buyer)) {
						buyer = customerName;
					}
					invoiceImport.setBuyer(buyer);
			
					String invoiceDateStr = getCellValue(row, 1).toString();
					if (isValidDate(invoiceDateStr)) {
						Date invoiceDate = DateUtils.dateTime("dd/MM/yyyy", invoiceDateStr);
						invoiceImport.setInvoiceDate(invoiceDate);
					}
					
					// Kiem tra du lieu hop le
					List<String> listErrorInv = checkInvoice(i+1, invoiceImport);
					if (CollectionUtils.isEmpty(listErrorInv)) {
						list.add(invoiceImport);
					} else {
						errors.addAll(listErrorInv);
					}

					currentInvoiceNo = invoiceNo;
					
				}

				InvoiceDetail invoiceDetail = new InvoiceDetail();

				String productName = getCellValue(row, 6).toString();
				invoiceDetail.setProductName(productName);

				String unit = getCellValue(row, 7).toString();
				invoiceDetail.setUnit(unit);

				Integer qty = Convert.toInt(getCellValue(row, 8).toString());
				invoiceDetail.setQty(qty);
				
				String priceStr = getCellValue(row, 9).toString();
				if (NumberUtils.isParsable(priceStr)) {
					Long price = Math.round(Convert.toDouble(priceStr));
					invoiceDetail.setPrice(price);
				}
				
				String amountStr = getCellValue(row, 10).toString();
				if (NumberUtils.isParsable(amountStr)) {
					Long amount = Math.round(Convert.toDouble(amountStr));
					invoiceDetail.setAmount(amount);
				}

				Integer taxPercent = Convert.toInt(getCellValue(row, 11).toString());
				invoiceDetail.setTaxPercent(taxPercent);
				
				String taxAmountStr = getCellValue(row, 12).toString();
				if (NumberUtils.isParsable(taxAmountStr)) {
					Long taxAmount = Math.round(Convert.toDouble(taxAmountStr));
					invoiceDetail.setTaxAmount(taxAmount);
				}
				
//				String totalAmountStr = getCellValue(row, 13).toString();
//				if (NumberUtils.isParsable(totalAmountStr)) {
//					Long totalAmount = Math.round(Convert.toDouble(totalAmountStr));
//					invoiceDetail.setTotalAmount(totalAmount);
//				}

				String channelCode = getCellValue(row, 14).toString();
				if (StringUtils.isBlank(channelCode)) {
					channelCode = invoiceImport.getDetails().get(0).getChannelCode();
				}
				invoiceDetail.setChannelCode(channelCode);

				String code = getCellValue(row, 15).toString();
				invoiceDetail.setCode(code);

				String storeCode = getCellValue(row, 16).toString();
				invoiceDetail.setStoreCode(storeCode);

				String promotion = getCellValue(row, 17).toString();
				if (StringUtils.isBlank(promotion)) {
					promotion = "0";
				}
				invoiceDetail.setPromotion(promotion);

				// Kiem tra du lieu hop le
				List<String> listErrorInvDetail = checkInvoiceDetail(i+1, invoiceDetail);
				if (CollectionUtils.isEmpty(listErrorInvDetail)) {
					if (CollectionUtils.isEmpty(invoiceImport.getDetails())) {
						invoiceImport.setDetails(new ArrayList<InvoiceDetail>());
					}

					invoiceImport.getDetails().add(invoiceDetail);
				} else {
					errors.addAll(listErrorInvDetail);
				}
			}

			if (CollectionUtils.isNotEmpty(list)) {
				// Xóa các hóa đơn mà không có chi tiết
				List<InvoiceImport> invoiceRemoveList = new ArrayList<InvoiceImport>();
				for (InvoiceImport invoice : list) {
					if (CollectionUtils.isEmpty(invoice.getDetails())) {
						invoiceRemoveList.add(invoice);
					}
				}
				list.removeAll(invoiceRemoveList);
			}

			result.setErrors(errors);
			result.setListInvoice(list);
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Import invoice exception: {}", e.getMessage());
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return null;
	}

	public Object getCellValue(Row row, int column) {
		if (row == null) {
			return row;
		}
		Object val = "";
		try {
			Cell cell = row.getCell(column);
			if (StringUtils.isNotNull(cell)) {
				if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {
					val = cell.getNumericCellValue();
					if (DateUtil.isCellDateFormatted(cell)) {
						val = DateUtil.getJavaDate((Double) val); // POI Excel 日期格式转换
					} else {
						if ((Double) val % 1 != 0) {
							val = new BigDecimal(val.toString());
						} else {
							val = new DecimalFormat("0").format(val);
						}
					}
				} else if (cell.getCellType() == CellType.STRING) {
					String celVal = cell.getStringCellValue();
					val = (celVal == null) ? null : celVal.trim();
				} else if (cell.getCellType() == CellType.BOOLEAN) {
					val = cell.getBooleanCellValue();
				} else if (cell.getCellType() == CellType.ERROR) {
					val = cell.getErrorCellValue();
				}

			}
		} catch (Exception e) {
			return val;
		}
		return val;
	}

	private boolean isRowEmpty(Row row) {
		if (row == null) {
			return true;
		}
		for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			if (cell != null && cell.getCellType() != CellType.BLANK) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Whether invoice data is valid or not
	 * 
	 * @param invoice
	 * @return
	 */
	private List<String> checkInvoice(int rowNum, Invoice invoice) {
		List<String> errors = new ArrayList<String>();
		String prefix = "Dòng [" + rowNum + "] : ";

		if (invoice.getInvoiceDate() == null) {
			errors.add(prefix + "Ngày hóa đơn không hợp lệ!");
		}

		if (StringUtils.isBlank(invoice.getCustomerName())) {
			errors.add(prefix + "Tên khách hàng không hợp lệ!");
		}
		
		if (!StringUtils.isBlank(invoice.getTaxCode()) && (invoice.getTaxCode().length() < 10)) {
			errors.add(prefix + "Mã số thuế không hợp lệ!");
		}

		if (StringUtils.isBlank(invoice.getAddress())) {
			errors.add(prefix + "Địa chỉ không hợp lệ!");
		}

		return errors;
	}

	/**
	 * Whether invoice detail data is valid or not
	 * 
	 * @param invoiceDetail
	 * @return
	 */
	private List<String> checkInvoiceDetail(int rowNum, InvoiceDetail invoiceDetail) {
		List<String> errors = new ArrayList<String>();
		String prefix = "Dòng [" + rowNum + "] : ";

		if (StringUtils.isBlank(invoiceDetail.getProductName())) {
			errors.add(prefix + "Tên hàng hóa/dịch vụ (*) không hợp lệ!");
		}

		if (StringUtils.isBlank(invoiceDetail.getUnit()) || NumberUtils.isParsable(invoiceDetail.getUnit())) {
			errors.add(prefix + "ĐVT không hợp lệ!");
		}

		if ((invoiceDetail.getQty() == null)) {
			errors.add(prefix + "Số lượng không hợp lệ!");
		}

		if (invoiceDetail.getPrice() == null) {
			errors.add(prefix + "Đơn giá không hợp lệ!");
		}

		if (invoiceDetail.getAmount() == null) {
			errors.add(prefix + "Thành tiền không hợp lệ!");
		}

		if (invoiceDetail.getTaxPercent() == null) {
			errors.add(prefix + "Thuế suất GTGT (%) không hợp lệ!");
		}

		if (invoiceDetail.getTaxAmount() == null) {
			errors.add(prefix + "Tiền thuế GTGT không hợp lệ!");
		}

//		if (invoiceDetail.getTotalAmount() == null) {
//			errors.add(prefix + "Tổng thanh toán không hợp lệ!");
//		}
		
		if (StringUtils.isBlank(invoiceDetail.getChannelCode())) {
			errors.add(prefix + "Mã đối tượng không hợp lệ!");
		}

		if (StringUtils.isBlank(invoiceDetail.getCode())) {
			errors.add(prefix + "Mã barcode không hợp lệ!");
		}

		if (StringUtils.isBlank(invoiceDetail.getStoreCode())) {
			errors.add(prefix + "Mã kho không hợp lệ!");
		}

		return errors;
	}

	private boolean isValidDate(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			dateFormat.parse(date);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
