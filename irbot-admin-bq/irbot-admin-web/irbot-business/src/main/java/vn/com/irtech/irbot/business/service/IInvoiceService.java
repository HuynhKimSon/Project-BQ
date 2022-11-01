package vn.com.irtech.irbot.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.irbot.business.domain.Invoice;
import vn.com.irtech.irbot.business.dto.InvoiceInfo;
import vn.com.irtech.irbot.business.type.InvoiceType;
import vn.com.irtech.irbot.business.type.ModeRunType;

/**
 * Service interface Quan ly hoa don
 *
 * @author irtech
 * @date 2022-01-10
 */
public interface IInvoiceService {

	/**
	 * Query Quan ly hoa don
	 *
	 * @param id ID Quan ly hoa don
	 * @return Quan ly hoa don
	 */
	public Invoice selectInvoiceById(Long id);

	/**
	 * Query list Quan ly hoa don
	 *
	 * @param invoice Quan ly hoa don Collection @return Quan ly hoa don
	 */
	public List<Invoice> selectInvoiceList(Invoice invoice);

	public List<InvoiceInfo> selectInvoiceInfoList(Invoice invoice);

	/**
	 * Added Quan ly hoa don
	 *
	 * @param invoice Quan ly hoa don
	 * @return result
	 */
	public int insertInvoice(Invoice invoice);

	/**
	 * Update Quan ly hoa don
	 *
	 * @param invoice Quan ly hoa don
	 * @return result
	 */
	public int updateInvoice(Invoice invoice);

	/**
	 * Xóa hàng loạt Quan ly hoa don
	 *
	 * @param id ID của dữ liệu sẽ bị xóa
	 * @return result
	 */
	public int deleteInvoiceByIds(String ids);

	/**
	 * Delete information Quan ly hoa don
	 *
	 * @param id ID Quan ly hoa don
	 * @return result
	 */
	public int deleteInvoiceById(Long id);

	public AjaxResult uploadInvoiceFile(MultipartFile file, InvoiceType invoiceType, String userName, Date invDate,
			Integer startHour, Integer endHour, ModeRunType modeRun) throws Exception;

	public int sendRobotProcess(Long uploadId);

	public boolean checkDataSendRobot(Long uploadId);
	
	public Long randomIndexInvoice(Long uploadId);
	
	public void mergeInv(String ids) throws Exception;
}
