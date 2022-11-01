package vn.com.irtech.irbot.business.dto.request;

import java.io.Serializable;

public class RobotSyncMisaReqDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean isGift;
	private String productName;
	private String unit;
	private Integer quantity;
	private Long unitPrice;
	private Long taxDue;
	private Long subTotal;

	public boolean isGift() {
		return isGift;
	}

	public void setGift(boolean isGift) {
		this.isGift = isGift;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Long unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Long getTaxDue() {
		return taxDue;
	}

	public void setTaxDue(Long taxDue) {
		this.taxDue = taxDue;
	}

	public Long getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Long subTotal) {
		this.subTotal = subTotal;
	}

}
