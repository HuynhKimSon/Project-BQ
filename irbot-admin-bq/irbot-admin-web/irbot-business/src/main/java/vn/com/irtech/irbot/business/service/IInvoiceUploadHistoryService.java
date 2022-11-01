package vn.com.irtech.irbot.business.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.irbot.business.domain.InvoiceUploadHistory;

/**
 * Service interface Lịch sử nhập hóa đơn
 *
 * @author irtech
 * @date 2022-01-26
 */
public interface IInvoiceUploadHistoryService 
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
     * Xóa hàng loạt Lịch sử nhập hóa đơn
     *
     * @param id ID của dữ liệu sẽ bị xóa
     * @return result
     */
    public int deleteInvoiceUploadHistoryByIds(String ids);

    /**
     * Delete information Lịch sử nhập hóa đơn
     *
     * @param id ID Lịch sử nhập hóa đơn
     * @return result
     */
    public int deleteInvoiceUploadHistoryById(Long id);
    
    /**
     * Download invoice file
     *
     * @param id ID Lịch sử nhập hóa đơn
     * @return result
     */
    public AjaxResult downloadInvoiceFile(Long id);
}
