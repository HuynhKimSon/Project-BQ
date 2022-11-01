package vn.com.irtech.irbot.business.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.core.common.annotation.Excel;
import vn.com.irtech.core.common.domain.BaseEntity;

/**
 * Object Hoa don chi tiet invoice_detail
 *
 * @author irtech
 * @date 2022-01-10
 */
public class InvoiceDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Id */
    private Long id;

    /** Invoice Id */
    @Excel(name = "Invoice Id")
    private Long invoiceId;

    /** Ten hang hoa */
    @Excel(name = "Ten hang hoa")
    private String productName;

    /** Don vi tinh */
    @Excel(name = "Don vi tinh")
    private String unit;

    /** Số lượng */
    @Excel(name = "Số lượng")
    private Integer qty;

    /** Thanh tien */
    @Excel(name = "Thanh tien")
    private Long amount;

    /** Don gia */
    @Excel(name = "Don gia")
    private Long price;

    /** Phan tram thue GTGT */
    @Excel(name = "Phan tram thue GTGT")
    private Integer taxPercent;

    /** Tien thue GTGT */
    @Excel(name = "Tien thue GTGT")
    private Long taxAmount;

    /** Ma kenh doi tuong */
    @Excel(name = "Ma kenh doi tuong")
    private String channelCode;

    /** Ma code */
    @Excel(name = "Ma code")
    private String code;

    /** Ma kho */
    @Excel(name = "Ma kho")
    private String storeCode;

    /** 0-Khong co KM 1-Co KM */
    @Excel(name = "0-Khong co KM 1-Co KM")
    private String promotion;

    /** Delete flag (0 existence 2 deletion) */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setInvoiceId(Long invoiceId) 
    {
        this.invoiceId = invoiceId;
    }

    public Long getInvoiceId() 
    {
        return invoiceId;
    }
    public void setProductName(String productName) 
    {
        this.productName = productName;
    }

    public String getProductName() 
    {
        return productName;
    }
    public void setUnit(String unit) 
    {
        this.unit = unit;
    }

    public String getUnit() 
    {
        return unit;
    }
    public void setQty(Integer qty) 
    {
        this.qty = qty;
    }

    public Integer getQty() 
    {
        return qty;
    }
    public void setAmount(Long amount) 
    {
        this.amount = amount;
    }

    public Long getAmount() 
    {
        return amount;
    }
    public void setPrice(Long price) 
    {
        this.price = price;
    }

    public Long getPrice() 
    {
        return price;
    }
    public void setTaxPercent(Integer taxPercent) 
    {
        this.taxPercent = taxPercent;
    }

    public Integer getTaxPercent() 
    {
        return taxPercent;
    }
    public void setTaxAmount(Long taxAmount) 
    {
        this.taxAmount = taxAmount;
    }

    public Long getTaxAmount() 
    {
        return taxAmount;
    }

    public void setChannelCode(String channelCode) 
    {
        this.channelCode = channelCode;
    }

    public String getChannelCode() 
    {
        return channelCode;
    }
    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }
    public void setStoreCode(String storeCode) 
    {
        this.storeCode = storeCode;
    }

    public String getStoreCode() 
    {
        return storeCode;
    }
    public void setPromotion(String promotion) 
    {
        this.promotion = promotion;
    }

    public String getPromotion() 
    {
        return promotion;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }
    
    public Long calcTotalAmount() {
    	return amount + taxAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("invoiceId", getInvoiceId())
            .append("productName", getProductName())
            .append("unit", getUnit())
            .append("qty", getQty())
            .append("amount", getAmount())
            .append("price", getPrice())
            .append("taxPercent", getTaxPercent())
            .append("taxAmount", getTaxAmount())
            .append("channelCode", getChannelCode())
            .append("code", getCode())
            .append("storeCode", getStoreCode())
            .append("promotion", getPromotion())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
