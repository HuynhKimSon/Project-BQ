package vn.com.irtech.irbot.business.service;

import java.util.List;
import vn.com.irtech.irbot.business.domain.InvoiceDetail;

/**
 * Service interface Hoa don chi tiet
 *
 * @author irtech
 * @date 2022-01-10
 */
public interface IInvoiceDetailService 
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
     * Xóa hàng loạt Hoa don chi tiet
     *
     * @param id ID của dữ liệu sẽ bị xóa
     * @return result
     */
    public int deleteInvoiceDetailByIds(String ids);

    /**
     * Delete information Hoa don chi tiet
     *
     * @param id ID Hoa don chi tiet
     * @return result
     */
    public int deleteInvoiceDetailById(Long id);
}
