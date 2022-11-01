package vn.com.irtech.irbot.business.dto.request;

import java.io.Serializable;
import java.util.List;

import vn.com.irtech.irbot.business.dto.ProcessMisaConfig;

public class RobotSyncMisaReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long processId;
	private String syncId;
	private String invoiceDate;
	private String custName;
	private String custAddress;
	private String taxCode;
	private String buyerName;
	private Long total;
	private Integer processQty;
	private Long invoiceId;
	private String taxId;
	private boolean isSurplus;

	private ProcessMisaConfig config;

	private List<RobotSyncMisaReqDetail> processOrder;

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

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustAddress() {
		return custAddress;
	}

	public void setCustAddress(String custAddress) {
		this.custAddress = custAddress;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
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

	public ProcessMisaConfig getConfig() {
		return config;
	}

	public void setConfig(ProcessMisaConfig config) {
		this.config = config;
	}

	public List<RobotSyncMisaReqDetail> getProcessOrder() {
		return processOrder;
	}

	public void setProcessOrder(List<RobotSyncMisaReqDetail> processOrder) {
		this.processOrder = processOrder;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public boolean isSurplus() {
		return isSurplus;
	}

	public void setSurplus(boolean isSurplus) {
		this.isSurplus = isSurplus;
	}

}
