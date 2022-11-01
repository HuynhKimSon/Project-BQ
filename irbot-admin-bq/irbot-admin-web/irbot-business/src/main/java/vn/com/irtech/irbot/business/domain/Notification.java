package vn.com.irtech.irbot.business.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.core.common.annotation.Excel;
import vn.com.irtech.core.common.domain.BaseEntity;

public class Notification extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Id */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String title;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String content;

    /** 0-Thong bao 1-Canh bao */
    @Excel(name = "0-Thong bao 1-Canh bao")
    private Integer type;

    /** 0-Chua xem 1-Da xem  */
    @Excel(name = "0-Chua xem 1-Da xem ")
    private String seenFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setType(Integer type) 
    {
        this.type = type;
    }

    public Integer getType() 
    {
        return type;
    }
    public void setSeenFlag(String seenFlag) 
    {
        this.seenFlag = seenFlag;
    }

    public String getSeenFlag() 
    {
        return seenFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("content", getContent())
            .append("type", getType())
            .append("seenFlag", getSeenFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}