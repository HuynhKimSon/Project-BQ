package vn.com.irtech.irbot.business.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.core.common.annotation.Excel;
import vn.com.irtech.core.common.domain.BaseEntity;

/**
 * Object Lịch sử nhập hóa đơn invoice_upload_history
 *
 * @author irtech
 * @date 2022-01-26
 */
public class InvoiceUploadHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** invoice_upload_history_id */
    private Long id;

    /** 0-Khách lẻ online 1-Khách lẻ cửa hàng 2-Khách sỉ */
    @Excel(name = "0-Khách lẻ online 1-Khách lẻ cửa hàng 2-Khách sỉ")
    private Integer invoiceType;

    /** Tên file */
    @Excel(name = "Tên file")
    private String fileName;

    /** Đường dẫn */
    @Excel(name = "Đường dẫn")
    private String filePath;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setInvoiceType(Integer invoiceType) 
    {
        this.invoiceType = invoiceType;
    }

    public Integer getInvoiceType() 
    {
        return invoiceType;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }
    public void setFilePath(String filePath) 
    {
        this.filePath = filePath;
    }

    public String getFilePath() 
    {
        return filePath;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("invoiceType", getInvoiceType())
            .append("fileName", getFileName())
            .append("filePath", getFilePath())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
