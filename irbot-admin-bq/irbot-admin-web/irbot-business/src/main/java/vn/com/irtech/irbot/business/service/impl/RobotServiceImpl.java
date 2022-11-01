package vn.com.irtech.irbot.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.irtech.core.common.text.Convert;
import vn.com.irtech.core.common.utils.DateUtils;
import vn.com.irtech.irbot.business.domain.Notification;
import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.domain.RobotService;
import vn.com.irtech.irbot.business.dto.request.RobotCreateReq;
import vn.com.irtech.irbot.business.dto.request.RobotUpdateReq;
import vn.com.irtech.irbot.business.dto.response.RobotInfoRes;
import vn.com.irtech.irbot.business.mapper.NotificationMapper;
import vn.com.irtech.irbot.business.mapper.RobotMapper;
import vn.com.irtech.irbot.business.mapper.RobotServiceMapper;
import vn.com.irtech.irbot.business.service.IRobotService;
import vn.com.irtech.irbot.business.type.NotificationType;
import vn.com.irtech.irbot.business.type.RobotStatusType;

/**
 * Robot Handling Service Business Layer
 *
 * @author irtech
 * @date 2021-12-04
 */
@Service
public class RobotServiceImpl implements IRobotService {
	@Autowired
	private RobotMapper robotMapper;

	@Autowired
	private RobotServiceMapper robotServiceMapper;

	@Autowired
	private NotificationMapper notificationMapper;

	/**
	 * Query Robot
	 *
	 * @param id ID Robot
	 * @return Robot
	 */
	@Override
	public Robot selectRobotById(Long id) {
		return robotMapper.selectRobotById(id);
	}

	/**
	 * Query list Robot
	 *
	 * @param robot Robot
	 * @return Robot
	 */
	@Override
	public List<Robot> selectRobotList(Robot robot) {
		return robotMapper.selectRobotList(robot);
	}

	/**
	 * Added Robot
	 *
	 * @param robot Robot
	 * @return result
	 */
	@Override
	@Transactional
	public int insertRobot(RobotCreateReq request) {
		// Insert to table robot
		request.setCreateTime(DateUtils.getNowDate());
		int rowAffect = robotMapper.insertRobot(request);

		// Insert to table robot_service
		List<RobotService> robotServiceList = new ArrayList<RobotService>();
		if (request.getServices() != null && request.getServices().length > 0) {
			for (Long serviceId : request.getServices()) {
				RobotService robotService = new RobotService();
				robotService.setRobotId(request.getId());
				robotService.setServiceId(serviceId);
				robotServiceList.add(robotService);
			}
			robotServiceMapper.batchRobotService(robotServiceList);
		}

		return rowAffect;
	}

	/**
	 * Update Robot
	 *
	 * @param robot Robot
	 * @return result
	 */
	@Override
	@Transactional
	public int updateRobot(Robot robot) {
		// Push notification
		if (RobotStatusType.OFFLINE.value().equals(robot.getStatus()) || "1".equals(robot.getDisabled())) {
			Robot robotExist = robotMapper.selectRobotById(robot.getId());
			if (robotExist != null) {
				Notification notification = new Notification();
				notification.setTitle("Robot bị tắt " + robotExist.getUuid());
				notification.setType(NotificationType.WARNING.value());
				notificationMapper.insertNotification(notification);
			}
		}

		robot.setUpdateTime(DateUtils.getNowDate());
		return robotMapper.updateRobot(robot);
	}

	/**
	 * Update Robot
	 *
	 * @param robot Robot
	 * @return result
	 */
	@Override
	@Transactional
	public int updateRobotInfo(RobotUpdateReq request) {
		request.setUpdateTime(DateUtils.getNowDate());
		int rowAffect = updateRobot(request);

		robotServiceMapper.deleteRobotServiceByRobotId(request.getId());

		// Insert to table robot_service
		List<RobotService> robotServiceList = new ArrayList<RobotService>();
		if (request.getServices() != null && request.getServices().length > 0) {
			for (Long serviceId : request.getServices()) {
				RobotService robotService = new RobotService();
				robotService.setRobotId(request.getId());
				robotService.setServiceId(serviceId);
				robotServiceList.add(robotService);
			}
			robotServiceMapper.batchRobotService(robotServiceList);
		}

		return rowAffect;
	}

	/**
	 * Delete object Robot
	 *
	 * @param id ID of the data to be deleted
	 * @return result
	 */
	@Override
	@Transactional
	public int deleteRobotByIds(String ids) {
		String[] idArr = Convert.toStrArray(ids);

		int rowAffect = robotMapper.deleteRobotByIds(idArr);

		robotServiceMapper.deleteRobotService(idArr);

		return rowAffect;
	}

	/**
	 * Delete information Robot
	 *
	 * @param id ID Robot
	 * @return result
	 */
	@Override
	public int deleteRobotById(Long id) {
		return robotMapper.deleteRobotById(id);
	}

	@Override
	public List<RobotInfoRes> selectRobotInfoList(Robot robot) {
		List<Robot> robots = robotMapper.selectRobotList(robot);
		List<RobotInfoRes> response = new ArrayList<RobotInfoRes>();
		for (Robot rb : robots) {
			RobotInfoRes robotInfoRes = new RobotInfoRes(rb);
			// Get services that robot support
			List<vn.com.irtech.irbot.business.domain.Service> services = robotServiceMapper
					.selectServicesByRobotId(rb.getId());
			robotInfoRes.setServices(services);
			response.add(robotInfoRes);
		}
		return response;
	}

	@Override
	public RobotInfoRes selectRobotInfoById(Long id) {
		Robot robot = robotMapper.selectRobotById(id);
		RobotInfoRes robotInfoRes = new RobotInfoRes(robot);
		List<vn.com.irtech.irbot.business.domain.Service> services = robotServiceMapper
				.selectServicesByRobotId(robot.getId());
		robotInfoRes.setServices(services);
		return robotInfoRes;
	}

	@Override
	public int insertRobot(Robot robot) {
		return robotMapper.insertRobot(robot);
	}

	@Override
	public List<Robot> selectRobotNotPing(Integer minutesNotPing) {
		return robotMapper.selectRobotNotPing(minutesNotPing);
	}
}
