package vn.com.irtech.irbot.business.service;

import java.util.List;
import vn.com.irtech.irbot.business.domain.ProcessBravo;

/**
 * Service interface Đồng bộ BRAVO
 *
 * @author irtech
 * @date 2022-01-24
 */
public interface IProcessBravoService {

	/**
	 * Query Đồng bộ BRAVO
	 *
	 * @param id ID Đồng bộ BRAVO
	 * @return Đồng bộ BRAVO
	 */
	public ProcessBravo selectProcessBravoById(Long id);

	/**
	 * Query list Đồng bộ BRAVO
	 *
	 * @param processBravo Đồng bộ BRAVO Collection @return Đồng bộ BRAVO
	 */
	public List<ProcessBravo> selectProcessBravoList(ProcessBravo processBravo);

	/**
	 * Added Đồng bộ BRAVO
	 *
	 * @param processBravo Đồng bộ BRAVO
	 * @return result
	 */
	public int insertProcessBravo(ProcessBravo processBravo);

	/**
	 * Update Đồng bộ BRAVO
	 *
	 * @param processBravo Đồng bộ BRAVO
	 * @return result
	 */
	public int updateProcessBravo(ProcessBravo processBravo);

	/**
	 * Xóa hàng loạt Đồng bộ BRAVO
	 *
	 * @param id ID của dữ liệu sẽ bị xóa
	 * @return result
	 */
	public int deleteProcessBravoByIds(String ids);

	/**
	 * Delete information Đồng bộ BRAVO
	 *
	 * @param id ID Đồng bộ BRAVO
	 * @return result
	 */
	public int deleteProcessBravoById(Long id);

	public int sync() throws Exception;
	
	public void retry(String invoiceIds) throws Exception;

}
