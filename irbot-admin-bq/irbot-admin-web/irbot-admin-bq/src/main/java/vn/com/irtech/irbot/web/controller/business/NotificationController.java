package vn.com.irtech.irbot.web.controller.business;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.irbot.business.domain.Invoice;
import vn.com.irtech.irbot.business.domain.Notification;
import vn.com.irtech.irbot.business.dto.InvoiceInfo;
import vn.com.irtech.irbot.business.service.INotificationService;

@Controller
@RequestMapping("/business/notification")
public class NotificationController extends BaseController {

	private String prefix = "business/notification";

	@GetMapping()
	public String notification() {
		return prefix + "/notification";
	}

	@Autowired
	private INotificationService notificationService;

	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Notification notification) {
		startPage();
		List<Notification> list = notificationService.selectNotificationList(notification);
		return getDataTable(list);
	}

	@GetMapping("/list-info")
	@ResponseBody
	public AjaxResult listInfo(Notification notification) {
		startPage();
		AjaxResult ajaxResult = AjaxResult.success();
		List<Notification> list = notificationService.selectNotificationList(notification);
		ajaxResult.put("listNotify", list);
		Long total = new PageInfo(list).getTotal();
		ajaxResult.put("total", total);

		// Update seen flag
		notificationService.viewAllNotification();

		return ajaxResult;
	}

	@GetMapping("/amount")
	@ResponseBody
	public AjaxResult getAmount() {
		AjaxResult result = AjaxResult.success();
		Notification notificationSelect = new Notification();
		notificationSelect.setSeenFlag("0");
		List<Notification> notifyList = notificationService.selectNotificationList(notificationSelect);
		if (CollectionUtils.isNotEmpty(notifyList)) {
			result.put("amount", notifyList.size());
		} else {
			result.put("amount", 0);
		}
		return result;
	}

	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(notificationService.deleteNotificationByIds(ids));
	}

	@PostMapping("/removeAll")
	@ResponseBody
	public AjaxResult removeAll() {
		AjaxResult result = AjaxResult.success();
		notificationService.removeAll();
		return result;
	}
}
