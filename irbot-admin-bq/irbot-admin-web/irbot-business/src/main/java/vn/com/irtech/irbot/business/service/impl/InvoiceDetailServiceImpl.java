package vn.com.irtech.irbot.business.service.impl;

import java.util.List;
import vn.com.irtech.core.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.irbot.business.mapper.InvoiceDetailMapper;
import vn.com.irtech.irbot.business.domain.InvoiceDetail;
import vn.com.irtech.irbot.business.service.IInvoiceDetailService;
import vn.com.irtech.core.common.text.Convert;


/**
 * Hoa don chi tiet Handling Service Business Layer
 *
 * @author irtech
 * @date 2022-01-10
 */
@Service
public class InvoiceDetailServiceImpl implements IInvoiceDetailService 
{
    @Autowired
    private InvoiceDetailMapper invoiceDetailMapper;

    /**
     * Query Hoa don chi tiet
     *
     * @param id ID Hoa don chi tiet
     * @return Hoa don chi tiet
     */
    @Override
    public InvoiceDetail selectInvoiceDetailById(Long id)
    {
        return invoiceDetailMapper.selectInvoiceDetailById(id);
    }


    /**
     * Query list Hoa don chi tiet
     *
     * @param invoiceDetail Hoa don chi tiet
     * @return Hoa don chi tiet
     */
    @Override
    public List<InvoiceDetail> selectInvoiceDetailList(InvoiceDetail invoiceDetail)
    {
        return invoiceDetailMapper.selectInvoiceDetailList(invoiceDetail);
    }


    /**
     * Added Hoa don chi tiet
     *
     * @param invoiceDetail Hoa don chi tiet
     * @return result
     */
    @Override
    public int insertInvoiceDetail(InvoiceDetail invoiceDetail)
    {
        invoiceDetail.setCreateTime(DateUtils.getNowDate());
        return invoiceDetailMapper.insertInvoiceDetail(invoiceDetail);
    }

    /**
     * Update Hoa don chi tiet
     *
     * @param invoiceDetail Hoa don chi tiet
     * @return result
     */
    @Override
    public int updateInvoiceDetail(InvoiceDetail invoiceDetail)
    {
        invoiceDetail.setUpdateTime(DateUtils.getNowDate());
        return invoiceDetailMapper.updateInvoiceDetail(invoiceDetail);
    }

    /**
     * Delete object Hoa don chi tiet
     *
     * @param id ID of the data to be deleted
     * @return result
     */
    @Override
    public int deleteInvoiceDetailByIds(String ids)
    {
        return invoiceDetailMapper.deleteInvoiceDetailByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete information Hoa don chi tiet
     *
     * @param id ID Hoa don chi tiet
     * @return result
     */
    @Override
    public int deleteInvoiceDetailById(Long id)
    {
        return invoiceDetailMapper.deleteInvoiceDetailById(id);
    }
}
