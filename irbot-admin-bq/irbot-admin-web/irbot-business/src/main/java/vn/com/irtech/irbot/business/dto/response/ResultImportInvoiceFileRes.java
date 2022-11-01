package vn.com.irtech.irbot.business.dto.response;

import java.io.Serializable;
import java.util.List;

import vn.com.irtech.irbot.business.dto.InvoiceImport;

public class ResultImportInvoiceFileRes implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<InvoiceImport> listInvoice;

	private List<String> errors;

	public List<InvoiceImport> getListInvoice() {
		return listInvoice;
	}

	public void setListInvoice(List<InvoiceImport> listInvoice) {
		this.listInvoice = listInvoice;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

}
