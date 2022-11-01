package vn.com.irtech.irbot.business.mapper;

import java.util.List;

import vn.com.irtech.irbot.business.domain.ProcessBravo;

/**
 * Mapping interface Đồng bộ BRAVO
 *
 * @author irtech
 * @date 2022-01-24
 */
public interface ProcessBravoMapper 
{
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
     * @param processBravo Đồng bộ BRAVO
     * Collection @return Đồng bộ BRAVO
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
     * Delete Đồng bộ BRAVO
     *
     * @param id ID Đồng bộ BRAVO
     * @return result
     */
    public int deleteProcessBravoById(Long id);

    /**
     * Bulk delete Đồng bộ BRAVO
     *
     * @param id The ID of the data will be deleted
     * @return result
     */
    public int deleteProcessBravoByIds(String[] ids);
    
    public int deleteProcessBravoByInvoiceIds(String[] ids);
    
}
