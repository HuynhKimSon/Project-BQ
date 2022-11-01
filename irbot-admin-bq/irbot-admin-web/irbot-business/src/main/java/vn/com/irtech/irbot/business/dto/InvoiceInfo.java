package vn.com.irtech.irbot.business.dto;

import vn.com.irtech.irbot.business.domain.Invoice;

public class InvoiceInfo extends Invoice {

	private static final long serialVersionUID = 1L;

	// Tong so luong san pham
	private Integer totalProduct;

	// Tong thanh toan cua hoa don
	private Long totalAmount;

	// Tong thanh toan chinh xac cua hoa don
	private Long totalAmountExactly;

	private Long processMisaId;

	private Long processBravoId;

	// Tien chenh lech
	private Long diff;

	public Integer getTotalProduct() {
		return totalProduct;
	}

	public void setTotalProduct(Integer totalProduct) {
		this.totalProduct = totalProduct;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getProcessMisaId() {
		return processMisaId;
	}

	public void setProcessMisaId(Long processMisaId) {
		this.processMisaId = processMisaId;
	}

	public Long getProcessBravoId() {
		return processBravoId;
	}

	public void setProcessBravoId(Long processBravoId) {
		this.processBravoId = processBravoId;
	}

	public Long getDiff() {
		return diff;
	}

	public void setDiff(Long diff) {
		this.diff = diff;
	}

	public Long getTotalAmountExactly() {
		return totalAmountExactly;
	}

	public void setTotalAmountExactly(Long totalAmountExactly) {
		this.totalAmountExactly = totalAmountExactly;
	}

}
