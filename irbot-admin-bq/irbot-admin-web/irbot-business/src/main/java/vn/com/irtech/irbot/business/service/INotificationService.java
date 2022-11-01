package vn.com.irtech.irbot.business.service;

import java.util.List;

import vn.com.irtech.irbot.business.domain.Notification;

public interface INotificationService {
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
	 * @param notification Thong bao Collection @return Thong bao
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
	 * Xóa hàng loạt Thong bao
	 *
	 * @param id ID của dữ liệu sẽ bị xóa
	 * @return result
	 */
	public int deleteNotificationByIds(String ids);

	/**
	 * Delete information Thong bao
	 *
	 * @param id ID Thong bao
	 * @return result
	 */
	public int deleteNotificationById(Long id);
	
	public int viewAllNotification();
	
	public int removeAll();
}
