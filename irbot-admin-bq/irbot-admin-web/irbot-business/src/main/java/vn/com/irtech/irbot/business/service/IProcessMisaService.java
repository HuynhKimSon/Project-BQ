package vn.com.irtech.irbot.business.service;

import java.util.List;
import vn.com.irtech.irbot.business.domain.ProcessMisa;

/**
 * Service interface Đồng bộ MISA
 *
 * @author irtech
 * @date 2022-01-18
 */
public interface IProcessMisaService 
{

    /**
     * Query Đồng bộ MISA
     *
     * @param id ID Đồng bộ MISA
     * @return Đồng bộ MISA
     */
    public ProcessMisa selectProcessMisaById(Long id);

    /**
     * Query list Đồng bộ MISA
     *
     * @param processMisa Đồng bộ MISA
     * Collection @return Đồng bộ MISA
     */
    public List<ProcessMisa> selectProcessMisaList(ProcessMisa processMisa);

    /**
     * Added Đồng bộ MISA
     *
     * @param processMisa Đồng bộ MISA
     * @return result
     */
    public int insertProcessMisa(ProcessMisa processMisa);

    /**
     * Update Đồng bộ MISA
     *
     * @param processMisa Đồng bộ MISA
     * @return result
     */
    public int updateProcessMisa(ProcessMisa processMisa);

    /**
     * Xóa hàng loạt Đồng bộ MISA
     *
     * @param id ID của dữ liệu sẽ bị xóa
     * @return result
     */
    public int deleteProcessMisaByIds(String ids);

    /**
     * Delete information Đồng bộ MISA
     *
     * @param id ID Đồng bộ MISA
     * @return result
     */
    public int deleteProcessMisaById(Long id);
    
    public void retry(String invoiceIds) throws Exception;
    
    public int sync() throws Exception;
}
