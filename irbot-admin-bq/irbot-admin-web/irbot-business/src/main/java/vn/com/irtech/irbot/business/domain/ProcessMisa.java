package vn.com.irtech.irbot.business.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.core.common.annotation.Excel;
import vn.com.irtech.core.common.domain.BaseEntity;

/**
 * Object Đồng bộ MISA process_misa
 *
 * @author irtech
 * @date 2022-01-18
 */
public class ProcessMisa extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** process_misa Id */
	private Long id;
	
	/** Invoice Id */
    @Excel(name = "Invoice Id")
    private Long invoiceId;

	/** 0-Khách lẻ online 1-Khách lẻ cửa hàng 2-Khách sỉ */
	@Excel(name = "0-Khách lẻ online 1-Khách lẻ cửa hàng 2-Khách sỉ")
	private Integer invoiceType;

	/** Mã số thuế */
	@Excel(name = "Mã số thuế")
	private String taxCode;

	/** Tên Khách Hàng */
	@Excel(name = "Tên Khách Hàng")
	private String customerName;

	/** Ngày hóa đơn */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "Ngày hóa đơn", width = 30, dateFormat = "yyyy-MM-dd")
	private Date invoiceDate;

	/** Ngay du dinh xuat hoa don */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "Ngay du dinh xuat hoa don", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Date planStartDate;

	/** Số hóa đơn */
	@Excel(name = "Số hóa đơn")
	private String invoiceNo;

	/** Delete flag (0 existence 1 deletion) */
	private String delFlag;

	/** 0-Chưa làm 1-Đã gởi 2-Đang làm 3-Thất bại 4-Thành công */
	@Excel(name = "0-Chưa làm 1-Đã gởi 2-Đang làm 3-Thất bại 4-Thành công")
	private Integer status;

	/** Lỗi */
	@Excel(name = "Lỗi")
	private String error;

	/** Data request */
	@Excel(name = "Data request")
	private String dataRequest;

	/** Data response */
	@Excel(name = "Data response")
	private String dataResponse;

	/** Robot UUID */
	@Excel(name = "Robot UUID")
	private String robotUuid;

	/** Start date */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "Start date", width = 30, dateFormat = "yyyy-MM-dd")
	private Date startDate;

	/** End date */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "End date", width = 30, dateFormat = "yyyy-MM-dd")
	private Date endDate;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Integer getInvoiceType() {
		return invoiceType;
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

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public Date getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
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

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setDataRequest(String dataRequest) {
		this.dataRequest = dataRequest;
	}

	public String getDataRequest() {
		return dataRequest;
	}

	public void setDataResponse(String dataResponse) {
		this.dataResponse = dataResponse;
	}

	public String getDataResponse() {
		return dataResponse;
	}

	public void setRobotUuid(String robotUuid) {
		this.robotUuid = robotUuid;
	}

	public String getRobotUuid() {
		return robotUuid;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}
	
	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId()).append("invoiceId", getInvoiceId())
				.append("invoiceType", getInvoiceType()).append("taxCode", getTaxCode())
				.append("customerName", getCustomerName()).append("invoiceDate", getInvoiceDate())
				.append("invoiceNo", getInvoiceNo()).append("delFlag", getDelFlag()).append("status", getStatus())
				.append("error", getError()).append("dataRequest", getDataRequest())
				.append("dataResponse", getDataResponse()).append("robotUuid", getRobotUuid())
				.append("startDate", getStartDate()).append("endDate", getEndDate()).append("createBy", getCreateBy())
				.append("createTime", getCreateTime()).append("updateBy", getUpdateBy())
				.append("updateTime", getUpdateTime()).toString();
	}
}
