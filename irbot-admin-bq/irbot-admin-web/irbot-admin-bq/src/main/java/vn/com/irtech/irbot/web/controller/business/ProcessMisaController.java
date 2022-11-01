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
import vn.com.irtech.irbot.business.domain.ProcessMisa;
import vn.com.irtech.irbot.business.service.IProcessMisaService;
import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.utils.poi.ExcelUtil;
import vn.com.irtech.core.common.page.TableDataInfo;

/**
 * Đồng bộ MISAController
 * 
 * @author irtech
 * @date 2022-01-18
 */
@Controller
@RequestMapping("/business/misa")
public class ProcessMisaController extends BaseController {
	private String prefix = "business/misa";

	@Autowired
	private IProcessMisaService processMisaService;

	@RequiresPermissions("business:misa:view")
	@GetMapping()
	public String misa() {
		return prefix + "/misa";
	}

	/**
	 * Query list Đồng bộ MISA
	 */
	@RequiresPermissions("business:misa:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ProcessMisa processMisa) {
		startPage();
		List<ProcessMisa> list = processMisaService.selectProcessMisaList(processMisa);
		return getDataTable(list);
	}

	/**
	 * Export list Đồng bộ MISA
	 */
	@RequiresPermissions("business:misa:export")
	@Log(title = "Đồng bộ MISA", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(ProcessMisa processMisa) {
		List<ProcessMisa> list = processMisaService.selectProcessMisaList(processMisa);
		ExcelUtil<ProcessMisa> util = new ExcelUtil<ProcessMisa>(ProcessMisa.class);
		return util.exportExcel(list, "Đồng bộ MISAdữ liệu");
	}

	/**
	 * Added Đồng bộ MISA
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * Added save Đồng bộ MISA
	 */
	@RequiresPermissions("business:misa:add")
	@Log(title = "Đồng bộ MISA", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ProcessMisa processMisa) {
		return toAjax(processMisaService.insertProcessMisa(processMisa));
	}

	/**
	 * Edit Đồng bộ MISA
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		ProcessMisa processMisa = processMisaService.selectProcessMisaById(id);
		mmap.put("processMisa", processMisa);
		return prefix + "/edit";
	}

	/**
	 * detail by id of process misa
	 */
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") Long id, ModelMap mmap) {
		ProcessMisa processMisa = processMisaService.selectProcessMisaById(id);
		if (processMisa == null) {
			processMisa = new ProcessMisa();
		}
		mmap.put("processMisa", processMisa);
		return prefix + "/detail";
	}

	/**
	 * Edit and save Đồng bộ MISA
	 */
	@RequiresPermissions("business:misa:edit")
	@Log(title = "Đồng bộ MISA", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ProcessMisa processMisa) {
		return toAjax(processMisaService.updateProcessMisa(processMisa));
	}

	/**
	 * Remove Đồng bộ MISA
	 */
	@RequiresPermissions("business:misa:remove")
	@Log(title = "Đồng bộ MISA", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(processMisaService.deleteProcessMisaByIds(ids));
	}

}
