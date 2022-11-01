package vn.com.irtech.irbot.business.mapper;

import java.util.List;
import vn.com.irtech.irbot.business.domain.ProcessMisa;

/**
 * Mapping interface Đồng bộ MISA
 *
 * @author irtech
 * @date 2022-01-18
 */
public interface ProcessMisaMapper 
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
     * Delete Đồng bộ MISA
     *
     * @param id ID Đồng bộ MISA
     * @return result
     */
    public int deleteProcessMisaById(Long id);

    /**
     * Bulk delete Đồng bộ MISA
     *
     * @param id The ID of the data will be deleted
     * @return result
     */
    public int deleteProcessMisaByIds(String[] ids);
    
    public int deleteProcessMisaByInvoiceIds(String[] ids);
}
