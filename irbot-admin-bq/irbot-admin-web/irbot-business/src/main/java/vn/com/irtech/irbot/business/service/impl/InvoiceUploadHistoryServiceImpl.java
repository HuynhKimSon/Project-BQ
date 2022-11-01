package vn.com.irtech.irbot.business.service.impl;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.com.irtech.core.common.utils.DateUtils;
import vn.com.irtech.core.common.utils.file.FileUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.irbot.business.mapper.InvoiceUploadHistoryMapper;
import vn.com.irtech.irbot.business.domain.InvoiceUploadHistory;
import vn.com.irtech.irbot.business.service.IInvoiceUploadHistoryService;
import vn.com.irtech.core.common.config.Global;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.text.Convert;


/**
 * Lịch sử nhập hóa đơn Handling Service Business Layer
 *
 * @author irtech
 * @date 2022-01-26
 */
@Service
public class InvoiceUploadHistoryServiceImpl implements IInvoiceUploadHistoryService 
{
    @Autowired
    private InvoiceUploadHistoryMapper invoiceUploadHistoryMapper;

    /**
     * Query Lịch sử nhập hóa đơn
     *
     * @param id ID Lịch sử nhập hóa đơn
     * @return Lịch sử nhập hóa đơn
     */
    @Override
    public InvoiceUploadHistory selectInvoiceUploadHistoryById(Long id)
    {
        return invoiceUploadHistoryMapper.selectInvoiceUploadHistoryById(id);
    }


    /**
     * Query list Lịch sử nhập hóa đơn
     *
     * @param invoiceUploadHistory Lịch sử nhập hóa đơn
     * @return Lịch sử nhập hóa đơn
     */
    @Override
    public List<InvoiceUploadHistory> selectInvoiceUploadHistoryList(InvoiceUploadHistory invoiceUploadHistory)
    {
    	invoiceUploadHistory.getParams().put("orderByIdDesc", true);
        return invoiceUploadHistoryMapper.selectInvoiceUploadHistoryList(invoiceUploadHistory);
    }


    /**
     * Added Lịch sử nhập hóa đơn
     *
     * @param invoiceUploadHistory Lịch sử nhập hóa đơn
     * @return result
     */
    @Override
    public int insertInvoiceUploadHistory(InvoiceUploadHistory invoiceUploadHistory)
    {
        invoiceUploadHistory.setCreateTime(DateUtils.getNowDate());
        return invoiceUploadHistoryMapper.insertInvoiceUploadHistory(invoiceUploadHistory);
    }

    /**
     * Update Lịch sử nhập hóa đơn
     *
     * @param invoiceUploadHistory Lịch sử nhập hóa đơn
     * @return result
     */
    @Override
    public int updateInvoiceUploadHistory(InvoiceUploadHistory invoiceUploadHistory)
    {
        invoiceUploadHistory.setUpdateTime(DateUtils.getNowDate());
        return invoiceUploadHistoryMapper.updateInvoiceUploadHistory(invoiceUploadHistory);
    }

    /**
     * Delete object Lịch sử nhập hóa đơn
     *
     * @param id ID of the data to be deleted
     * @return result
     */
    @Override
    public int deleteInvoiceUploadHistoryByIds(String ids)
    {
        return invoiceUploadHistoryMapper.deleteInvoiceUploadHistoryByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete information Lịch sử nhập hóa đơn
     *
     * @param id ID Lịch sử nhập hóa đơn
     * @return result
     */
    @Override
    public int deleteInvoiceUploadHistoryById(Long id)
    {
        return invoiceUploadHistoryMapper.deleteInvoiceUploadHistoryById(id);
    }


	@Override
	public AjaxResult downloadInvoiceFile(Long id) {
		InvoiceUploadHistory history = invoiceUploadHistoryMapper.selectInvoiceUploadHistoryById(id);
		if (history == null) {
			return AjaxResult.error("Invoice does not exist!");
		}
		
		String localPath = Global.getBasePath();
		String filePath = history.getFilePath();
		String fileName = history.getFileName();
		
		if (!FileUtils.fileExist(localPath + filePath + "/" + fileName)) {
			return AjaxResult.error("File does not exist!");
		}
		
		return AjaxResult.success(filePath + "/" + fileName);
	}
}
