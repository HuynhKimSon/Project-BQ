package vn.com.irtech.irbot.business.mapper;

import java.util.List;

import vn.com.irtech.irbot.business.domain.Invoice;
import vn.com.irtech.irbot.business.dto.InvoiceInfo;

/**
 * Mapping interface Quan ly hoa don
 *
 * @author irtech
 * @date 2022-01-10
 */
public interface InvoiceMapper {
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
	 * Delete Quan ly hoa don
	 *
	 * @param id ID Quan ly hoa don
	 * @return result
	 */
	public int deleteInvoiceById(Long id);

	/**
	 * Bulk delete Quan ly hoa don
	 *
	 * @param id The ID of the data will be deleted
	 * @return result
	 */
	public int deleteInvoiceByIds(String[] ids);
}
