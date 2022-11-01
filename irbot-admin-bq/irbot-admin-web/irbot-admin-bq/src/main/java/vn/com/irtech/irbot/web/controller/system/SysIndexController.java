package vn.com.irtech.irbot.web.controller.system;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.core.common.config.Global;
import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.domain.LoginUser;
import vn.com.irtech.core.common.text.Convert;
import vn.com.irtech.core.common.utils.CookieUtils;
import vn.com.irtech.core.common.utils.DateUtils;
import vn.com.irtech.core.common.utils.ServletUtils;
import vn.com.irtech.core.common.utils.StringUtils;
import vn.com.irtech.core.common.utils.bean.BeanUtils;
import vn.com.irtech.core.framework.util.ShiroUtils;
import vn.com.irtech.core.system.service.ISysConfigService;
import vn.com.irtech.core.system.service.ISysDictTypeService;
import vn.com.irtech.irbot.business.domain.ProcessBravo;
import vn.com.irtech.irbot.business.domain.ProcessMisa;
import vn.com.irtech.irbot.business.service.IProcessBravoService;
import vn.com.irtech.irbot.business.service.IProcessMisaService;
import vn.com.irtech.irbot.business.type.ProcessBravoStatus;
import vn.com.irtech.irbot.business.type.ProcessMisaStatus;
import vn.com.irtech.irbot.system.domain.SysMenu;
import vn.com.irtech.irbot.system.domain.SysUser;
import vn.com.irtech.irbot.system.service.ISysDeptService;
import vn.com.irtech.irbot.system.service.ISysMenuService;
import vn.com.irtech.irbot.system.service.ISysRoleService;
import vn.com.irtech.irbot.system.service.ISysUserService;

/**
 * Index controller
 * 
 * @author admin
 */
@Controller
public class SysIndexController extends BaseController {
	@Autowired
	private ISysMenuService menuService;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private ISysDictTypeService dictTypeService;

	@Autowired
	private ISysDeptService deptService;

	@Autowired
	private ISysUserService userService;

	@Autowired
	private ISysRoleService roleService;
	
	@Autowired
	private IProcessMisaService processMisaService; 
	
	@Autowired
	private IProcessBravoService processBravoService; 
	
	// System Home
	@GetMapping("/index")
	public String index(ModelMap mmap) {
		// Get identity information
		LoginUser loginUser = ShiroUtils.getLoginUser();
		SysUser user = null;
		if (loginUser instanceof SysUser) {
			user = (SysUser) loginUser;
		} else {
			user = new SysUser();
			BeanUtils.copyBeanProp(user, loginUser);
		}
		// Take out the menu based on the user id
		List<SysMenu> menus = menuService.selectMenusByUser(user);
		mmap.put("menus", menus);
		mmap.put("user", user);
		mmap.put("sideTheme", configService.selectConfigByKey("sys.index.sideTheme"));
		mmap.put("skinName", configService.selectConfigByKey("sys.index.skinName"));
		mmap.put("ignoreFooter", configService.selectConfigByKey("sys.index.ignoreFooter"));
		mmap.put("appName", Global.getName());
		mmap.put("appVersion", Global.getVersion());
		mmap.put("copyrightYear", Global.getCopyrightYear());
		mmap.put("demoEnabled", Global.isDemoEnabled());
		mmap.put("isDefaultModifyPwd", initPasswordIsModify(user.getPwdUpdateDate()));
		mmap.put("isPasswordExpired", passwordIsExpiration(user.getPwdUpdateDate()));
		mmap.put("minHeightScreen", configService.selectConfigByKey("sys.screen.height.min"));

		// ????????
//		String menuStyle = configService.selectConfigByKey("sys.index.menuStyle");
		// ???,?????????,???????
//        String indexStyle = ServletUtils.checkAgentIsMobile(ServletUtils.getRequest().getHeader("User-Agent")) ? "index" : menuStyle;

		// ??Cookie??????
		Cookie[] cookies = ServletUtils.getRequest().getCookies();
		for (Cookie cookie : cookies) {
			if (StringUtils.isNotEmpty(cookie.getName()) && "nav-style".equalsIgnoreCase(cookie.getName())) {
//                indexStyle = cookie.getValue();
				break;
			}
		}
		// String webIndex = "topnav".equalsIgnoreCase(indexStyle) ? "index-topnav" :
		// "index";
		return "index-topnav";
	}

	// switch theme
	@GetMapping("/system/switchSkin")
	public String switchSkin() {
		return "skin";
	}

	@GetMapping("/system/menuStyle/{style}")
	public void menuStyle(@PathVariable String style, HttpServletResponse response) {
		CookieUtils.setCookie(response, "nav-style", style);
	}

	@GetMapping("/system/main")
	public String main(ModelMap mmap) {
		mmap.put("version", Global.getVersion());
		
		//default statistic by month
		Date now = new Date();
		String fromDate = DateUtils.parseDateToStr("yyyy-MM-01", now);
		String toDate = DateUtils.parseDateToStr("yyyy-MM-dd", now);
		mmap.put("statisticData", statistic(fromDate, toDate));
		
		return "main";
		// return configService.selectConfigByKey("sys.index.template");
	}
	
	public Map<String, Integer> statistic(String fromDate, String toDate) {
		
		int misaDoing = countProcessMisaByStatus(ProcessMisaStatus.SENT.value()) + countProcessMisaByStatus(ProcessMisaStatus.PROCESSING.value());
		int misaFail = countProcessMisaByStatus(ProcessMisaStatus.FAIL.value());
		int misaSuccess = countProcessMisaByStatus(ProcessMisaStatus.SUCCESS.value());
		int misaDone = misaFail + misaSuccess;
		
		int bravoDoing = countProcessBravoByStatus(ProcessBravoStatus.SENT.value()) + countProcessBravoByStatus(ProcessBravoStatus.PROCESSING.value());
		int bravoFail = countProcessBravoByStatus(ProcessBravoStatus.FAIL.value());
		int bravoSuccess = countProcessBravoByStatus(ProcessBravoStatus.SUCCESS.value());
		int bravoDone = bravoFail + bravoSuccess;
		
		
		int totalDone = misaDone + bravoDone;
		int totalDoing = misaDoing + bravoDoing;
		int totalFail = misaFail + bravoFail;
		int totalSuccess = misaSuccess + bravoSuccess;
		
		Map<String, Integer> statisticData = new HashMap<String, Integer>();	
		
		statisticData.put("misaDone", misaDone);
		statisticData.put("misaDoing", misaDoing);
		statisticData.put("misaFail", misaFail);
		statisticData.put("misaSuccess", misaSuccess);
		
		statisticData.put("bravoDone", bravoDone);
		statisticData.put("bravoDoing", bravoDoing);
		statisticData.put("bravoFail", bravoFail);
		statisticData.put("bravoSuccess", bravoSuccess);
		
		statisticData.put("totalDone", totalDone);
		statisticData.put("totalDoing", totalDoing);
		statisticData.put("totalSuccess", totalSuccess);
		statisticData.put("totalFail", totalFail);
		
		return statisticData;
		
	}
	
	@PostMapping("/system/statistic/{type}")
	@ResponseBody
	public AjaxResult statistic(@PathVariable("type") Integer type) {
		Date now = new Date();
		String toDate = DateUtils.parseDateToStr("yyyy-MM-dd", now);
		// statistic by day
		if (type == 1) {
			String fromDate = DateUtils.parseDateToStr("yyyy-MM-dd", now);
			return AjaxResult.success(statistic(fromDate, toDate));
		}

		// statistic by month
		if (type == 2) {
			String fromDate = DateUtils.parseDateToStr("yyyy-MM-01", now);
			return AjaxResult.success(statistic(fromDate, toDate));
		}

		// statistic by year
		if (type == 3) {
			String fromDate = DateUtils.parseDateToStr("yyyy-01-01", now);
			return AjaxResult.success(statistic(fromDate, toDate));
		}
		return AjaxResult.success();
	}
	
	
	public boolean initPasswordIsModify(Date pwdUpdateDate) {
		Integer initPasswordModify = Convert.toInt(configService.selectConfigByKey("sys.account.initPasswordModify"));
		return initPasswordModify != null && initPasswordModify == 1 && pwdUpdateDate == null;
	}

	public boolean passwordIsExpiration(Date pwdUpdateDate) {
		Integer passwordValidateDays = Convert
				.toInt(configService.selectConfigByKey("sys.account.passwordValidateDays"));
		if (passwordValidateDays != null && passwordValidateDays > 0) {
			if (StringUtils.isNull(pwdUpdateDate)) {
				return true;
			}
			Date nowDate = DateUtils.getNowDate();
			return DateUtils.differentDaysByMillisecond(nowDate, pwdUpdateDate) > passwordValidateDays;
		}
		return false;
	}
	
	public int countProcessMisaByStatus(Integer status) {
		ProcessMisa processMisa = new ProcessMisa();
		processMisa.setStatus(status);
		List<ProcessMisa> list = processMisaService.selectProcessMisaList(processMisa);
		
		return list.size();
	}
	
	public int countProcessBravoByStatus(Integer status) {
		ProcessBravo processBravo = new ProcessBravo();
		processBravo.setStatus(status);
		List<ProcessBravo> list = processBravoService.selectProcessBravoList(processBravo);
		
		return list.size();
	}
}
