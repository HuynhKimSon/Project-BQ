package vn.com.irtech.irbot.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.irtech.core.common.text.Convert;
import vn.com.irtech.core.common.utils.DateUtils;
import vn.com.irtech.irbot.business.domain.Notification;
import vn.com.irtech.irbot.business.mapper.NotificationMapper;
import vn.com.irtech.irbot.business.service.INotificationService;

@Service
public class NotificationServiceImpl implements INotificationService 
{
    @Autowired
    private NotificationMapper notificationMapper;

    /**
     * Query Thong bao
     *
     * @param id ID Thong bao
     * @return Thong bao
     */
    @Override
    public Notification selectNotificationById(Long id)
    {
        return notificationMapper.selectNotificationById(id);
    }


    /**
     * Query list Thong bao
     *
     * @param notification Thong bao
     * @return Thong bao
     */
    @Override
    public List<Notification> selectNotificationList(Notification notification)
    {
        return notificationMapper.selectNotificationList(notification);
    }


    /**
     * Added Thong bao
     *
     * @param notification Thong bao
     * @return result
     */
    @Override
    public int insertNotification(Notification notification)
    {
        notification.setCreateTime(DateUtils.getNowDate());
        return notificationMapper.insertNotification(notification);
    }

    /**
     * Update Thong bao
     *
     * @param notification Thong bao
     * @return result
     */
    @Override
    public int updateNotification(Notification notification)
    {
        notification.setUpdateTime(DateUtils.getNowDate());
        return notificationMapper.updateNotification(notification);
    }

    /**
     * Delete object Thong bao
     *
     * @param id ID of the data to be deleted
     * @return result
     */
    @Override
    public int deleteNotificationByIds(String ids)
    {
        return notificationMapper.deleteNotificationByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete information Thong bao
     *
     * @param id ID Thong bao
     * @return result
     */
    @Override
    public int deleteNotificationById(Long id)
    {
        return notificationMapper.deleteNotificationById(id);
    }


	@Override
	@Transactional
	public int viewAllNotification() {
		return notificationMapper.viewAllNotification();
	}


	@Override
	public int removeAll() {
		return notificationMapper.removeAll();
	}
}