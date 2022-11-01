package vn.com.irtech.irbot.business.dto;

public class InvoiceSplitItem {

	/**
	 * Số sản phẩm/Hóa đơn
	 */
	private Integer totalProductPerInvoice;

	/**
	 * Số lượng hóa đơn tách
	 */
	private Integer maxInvoice;

	public InvoiceSplitItem() {
	}

	public InvoiceSplitItem(Integer totalProductPerInvoice, Integer maxInvoice) {
		this.totalProductPerInvoice = totalProductPerInvoice;
		this.maxInvoice = maxInvoice;
	}

	public Integer getTotalProductPerInvoice() {
		return totalProductPerInvoice;
	}

	public void setTotalProductPerInvoice(Integer totalProductPerInvoice) {
		this.totalProductPerInvoice = totalProductPerInvoice;
	}

	public Integer getMaxInvoice() {
		return maxInvoice;
	}

	public void setMaxInvoice(Integer maxInvoice) {
		this.maxInvoice = maxInvoice;
	}

}
