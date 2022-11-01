package vn.com.irtech.irbot.web.controller.business;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.com.irtech.core.common.annotation.Log;
import vn.com.irtech.core.common.enums.BusinessType;
import vn.com.irtech.irbot.business.domain.ProcessBravo;
import vn.com.irtech.irbot.business.service.IProcessBravoService;
import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.utils.poi.ExcelUtil;
import vn.com.irtech.core.common.page.TableDataInfo;

/**
 * Đồng bộ BRAVOController
 * 
 * @author irtech
 * @date 2022-01-24
 */
@Controller
@RequestMapping("/business/bravo")
public class ProcessBravoController extends BaseController {
	private String prefix = "business/bravo";

	@Autowired
	private IProcessBravoService processBravoService;

	@RequiresPermissions("business:bravo:view")
	@GetMapping()
	public String bravo() {
		return prefix + "/bravo";
	}

	/**
	 * Query list Đồng bộ BRAVO
	 */
	@RequiresPermissions("business:bravo:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ProcessBravo processBravo) {
		startPage();
		List<ProcessBravo> list = processBravoService.selectProcessBravoList(processBravo);
		return getDataTable(list);
	}

	/**
	 * Export list Đồng bộ BRAVO
	 */
	@RequiresPermissions("business:bravo:export")
	@Log(title = "Đồng bộ BRAVO", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(ProcessBravo processBravo) {
		List<ProcessBravo> list = processBravoService.selectProcessBravoList(processBravo);
		ExcelUtil<ProcessBravo> util = new ExcelUtil<ProcessBravo>(ProcessBravo.class);
		return util.exportExcel(list, "Đồng bộ BRAVOdữ liệu");
	}

	/**
	 * Added Đồng bộ BRAVO
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * Added save Đồng bộ BRAVO
	 */
	@RequiresPermissions("business:bravo:add")
	@Log(title = "Đồng bộ BRAVO", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ProcessBravo processBravo) {
		return toAjax(processBravoService.insertProcessBravo(processBravo));
	}

	/**
	 * Edit Đồng bộ BRAVO
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		ProcessBravo processBravo = processBravoService.selectProcessBravoById(id);
		mmap.put("processBravo", processBravo);
		return prefix + "/edit";
	}

	/**
	 * Detail Đồng bộ BRAVO
	 */
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") Long id, ModelMap mmap) {
		ProcessBravo processBravo = processBravoService.selectProcessBravoById(id);
		mmap.put("processBravo", processBravo);
		return prefix + "/detail";
	}

	/**
	 * Edit and save Đồng bộ BRAVO
	 */
	@RequiresPermissions("business:bravo:edit")
	@Log(title = "Đồng bộ BRAVO", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ProcessBravo processBravo) {
		return toAjax(processBravoService.updateProcessBravo(processBravo));
	}

	/**
	 * Remove Đồng bộ BRAVO
	 */
	@RequiresPermissions("business:bravo:remove")
	@Log(title = "Đồng bộ BRAVO", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(processBravoService.deleteProcessBravoByIds(ids));
	}

}
