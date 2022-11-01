package vn.com.irtech.irbot.business.service;

import java.io.InputStream;

import vn.com.irtech.irbot.business.dto.response.ResultImportInvoiceFileRes;
import vn.com.irtech.irbot.business.type.InvoiceType;

public interface IExcelService {
	
	public ResultImportInvoiceFileRes importInvoiceFile(InputStream is, InvoiceType invoiceType);
	
}
