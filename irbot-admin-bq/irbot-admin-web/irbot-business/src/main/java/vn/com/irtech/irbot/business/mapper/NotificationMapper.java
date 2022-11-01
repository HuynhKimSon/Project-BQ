package vn.com.irtech.irbot.business.mapper;

import java.util.List;

import vn.com.irtech.irbot.business.domain.Notification;

public interface NotificationMapper {
	/**
     * Query Thong bao
     *
     * @param id ID Thong bao
     * @return Thong bao
     */
    public Notification selectNotificationById(Long id);

    /**
     * Query list Thong bao
     *
     * @param notification Thong bao
     * Collection @return Thong bao
     */
    public List<Notification> selectNotificationList(Notification notification);

    /**
     * Added Thong bao
     *
     * @param notification Thong bao
     * @return result
     */
    public int insertNotification(Notification notification);

    /**
     * Update Thong bao
     *
     * @param notification Thong bao
     * @return result
     */
    public int updateNotification(Notification notification);

    /**
     * Delete Thong bao
     *
     * @param id ID Thong bao
     * @return result
     */
    public int deleteNotificationById(Long id);

    /**
     * Bulk delete Thong bao
     *
     * @param id The ID of the data will be deleted
     * @return result
     */
    public int deleteNotificationByIds(String[] ids);
    
    public int viewAllNotification();
    
    public int removeAll();
}
