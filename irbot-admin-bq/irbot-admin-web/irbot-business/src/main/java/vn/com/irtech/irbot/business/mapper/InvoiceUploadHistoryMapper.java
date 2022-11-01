package vn.com.irtech.irbot.business.mapper;

import java.util.List;
import vn.com.irtech.irbot.business.domain.InvoiceUploadHistory;

/**
 * Mapping interface Lịch sử nhập hóa đơn
 *
 * @author irtech
 * @date 2022-01-26
 */
public interface InvoiceUploadHistoryMapper 
{
    /**
     * Query Lịch sử nhập hóa đơn
     *
     * @param id ID Lịch sử nhập hóa đơn
     * @return Lịch sử nhập hóa đơn
     */
    public InvoiceUploadHistory selectInvoiceUploadHistoryById(Long id);

    /**
     * Query list Lịch sử nhập hóa đơn
     *
     * @param invoiceUploadHistory Lịch sử nhập hóa đơn
     * Collection @return Lịch sử nhập hóa đơn
     */
    public List<InvoiceUploadHistory> selectInvoiceUploadHistoryList(InvoiceUploadHistory invoiceUploadHistory);

    /**
     * Added Lịch sử nhập hóa đơn
     *
     * @param invoiceUploadHistory Lịch sử nhập hóa đơn
     * @return result
     */
    public int insertInvoiceUploadHistory(InvoiceUploadHistory invoiceUploadHistory);

    /**
     * Update Lịch sử nhập hóa đơn
     *
     * @param invoiceUploadHistory Lịch sử nhập hóa đơn
     * @return result
     */
    public int updateInvoiceUploadHistory(InvoiceUploadHistory invoiceUploadHistory);

    /**
     * Delete Lịch sử nhập hóa đơn
     *
     * @param id ID Lịch sử nhập hóa đơn
     * @return result
     */
    public int deleteInvoiceUploadHistoryById(Long id);

    /**
     * Bulk delete Lịch sử nhập hóa đơn
     *
     * @param id The ID of the data will be deleted
     * @return result
     */
    public int deleteInvoiceUploadHistoryByIds(String[] ids);
}
