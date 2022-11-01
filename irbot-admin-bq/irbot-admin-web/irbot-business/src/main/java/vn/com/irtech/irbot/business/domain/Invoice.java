package vn.com.irtech.irbot.business.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import vn.com.irtech.core.common.annotation.Excel;
import vn.com.irtech.core.common.domain.BaseEntity;

/**
 * Object Quan ly hoa don invoice
 *
 * @author irtech
 * @date 2022-01-10
 */
public class Invoice extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** Invoice Id */
	private Long id;

	/** 0-Khach le online 1-Khach le cua hang 2-Khach sy */
	@Excel(name = "0-Khach le online 1-Khach le cua hang 2-Khach sy")
	private Integer type;

	/** Ma so thue */
	@Excel(name = "Ma so thue")
	private String taxCode;

	/** Ten khach hang */
	@Excel(name = "Ten khach hang")
	private String customerName;

	/** Dia chi */
	@Excel(name = "Dia chi")
	private String address;

	/** Nguoi mua hang */
	@Excel(name = "Nguoi mua hang")
	private String buyer;

	/** Ngay hoa don */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "Ngay hoa don", width = 30, dateFormat = "yyyy-MM-dd")
	private Date invoiceDate;

	/** 0-Chay lap lich 1-Chay ngay */
	@Excel(name = "0-Chay lap lich 1-Chay ngay")
	private Integer modeRun;

	/** Ngay du dinh xuat hoa don */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "Ngay du dinh xuat hoa don", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date planStartDate;

	/** 0-Chưa gui lam lenh 1-Da gui lam lenh */
	@Excel(name = "Trang thai")
	private Integer status;

	/** 0-Chưa làm 1-Đã gởi 2-Đang làm 3-Thất bại 4-Thành công */
	@Excel(name = "Trang thai Misa")
	private Integer statusMisa;

	/** 0-Chưa làm 1-Đã gởi 2-Đang làm 3-Thất bại 4-Thành công */
	@Excel(name = "Trang thai Bravo")
	private Integer statusBravo;

	/** so hoa don tao tren MISA */
	@Excel(name = "so hoa don tao tren MISA")
	private String invoiceNo;

	/** Delete flag (0 existence 2 deletion) */
	private String delFlag;

	private Long invoiceUploadId;
	
	private Integer randomIndex;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getType() {
		return type;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public Integer getModeRun() {
		return modeRun;
	}

	public void setModeRun(Integer modeRun) {
		this.modeRun = modeRun;
	}

	public Date getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatusMisa() {
		return statusMisa;
	}

	public void setStatusMisa(Integer statusMisa) {
		this.statusMisa = statusMisa;
	}

	public Integer getStatusBravo() {
		return statusBravo;
	}

	public void setStatusBravo(Integer statusBravo) {
		this.statusBravo = statusBravo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public Long getInvoiceUploadId() {
		return invoiceUploadId;
	}

	public void setInvoiceUploadId(Long invoiceUploadId) {
		this.invoiceUploadId = invoiceUploadId;
	}

	public Integer getRandomIndex() {
		return randomIndex;
	}

	public void setRandomIndex(Integer randomIndex) {
		this.randomIndex = randomIndex;
	}
}
