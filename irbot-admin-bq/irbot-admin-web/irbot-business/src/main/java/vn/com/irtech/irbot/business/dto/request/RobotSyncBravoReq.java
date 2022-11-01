package vn.com.irtech.irbot.business.dto.request;

import java.io.Serializable;
import java.util.List;

import vn.com.irtech.irbot.business.dto.ProcessBravoConfig;

public class RobotSyncBravoReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long processId;

	private String syncId;

	private String invoiceDate;

	private String invoiceNo;

	private String custId;

	private String buyerName;

	private String description;

	private String taxId;

	private Long total;

	private Integer processQty;

	private ProcessBravoConfig config;

	private List<RobotSyncBravoReqDetail> processOrder;

	private Long invoiceId;

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public String getSyncId() {
		return syncId;
	}

	public void setSyncId(String syncId) {
		this.syncId = syncId;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Integer getProcessQty() {
		return processQty;
	}

	public void setProcessQty(Integer processQty) {
		this.processQty = processQty;
	}

	public ProcessBravoConfig getConfig() {
		return config;
	}

	public void setConfig(ProcessBravoConfig config) {
		this.config = config;
	}

	public List<RobotSyncBravoReqDetail> getProcessOrder() {
		return processOrder;
	}

	public void setProcessOrder(List<RobotSyncBravoReqDetail> processOrder) {
		this.processOrder = processOrder;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

}
