package vn.com.irtech.irbot.business.dto.request;

import java.io.Serializable;

public class RobotSyncBravoReqDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String productId;
	
	private Integer quantity;
	
	private Long unitPrice;
	
	private String wareHouse;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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

	public String getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(String wareHouse) {
		this.wareHouse = wareHouse;
	}

}
