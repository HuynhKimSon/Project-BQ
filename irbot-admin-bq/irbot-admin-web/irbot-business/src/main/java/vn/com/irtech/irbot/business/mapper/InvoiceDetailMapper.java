package vn.com.irtech.irbot.business.mapper;

import java.util.List;
import vn.com.irtech.irbot.business.domain.InvoiceDetail;

/**
 * Mapping interface Hoa don chi tiet
 *
 * @author irtech
 * @date 2022-01-10
 */
public interface InvoiceDetailMapper 
{
    /**
     * Query Hoa don chi tiet
     *
     * @param id ID Hoa don chi tiet
     * @return Hoa don chi tiet
     */
    public InvoiceDetail selectInvoiceDetailById(Long id);

    /**
     * Query list Hoa don chi tiet
     *
     * @param invoiceDetail Hoa don chi tiet
     * Collection @return Hoa don chi tiet
     */
    public List<InvoiceDetail> selectInvoiceDetailList(InvoiceDetail invoiceDetail);

    /**
     * Added Hoa don chi tiet
     *
     * @param invoiceDetail Hoa don chi tiet
     * @return result
     */
    public int insertInvoiceDetail(InvoiceDetail invoiceDetail);

    /**
     * Update Hoa don chi tiet
     *
     * @param invoiceDetail Hoa don chi tiet
     * @return result
     */
    public int updateInvoiceDetail(InvoiceDetail invoiceDetail);

    /**
     * Delete Hoa don chi tiet
     *
     * @param id ID Hoa don chi tiet
     * @return result
     */
    public int deleteInvoiceDetailById(Long id);

    /**
     * Bulk delete Hoa don chi tiet
     *
     * @param id The ID of the data will be deleted
     * @return result
     */
    public int deleteInvoiceDetailByIds(String[] ids);
    
    public int deleteInvoiceDetailByInvoiceIds(String[] ids);
}
