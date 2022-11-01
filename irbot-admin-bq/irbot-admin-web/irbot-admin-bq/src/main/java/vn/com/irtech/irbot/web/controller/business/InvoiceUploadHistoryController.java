package vn.com.irtech.irbot.web.controller.business;

import java.io.IOException;
import java.nio.channels.NonWritableChannelException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.OctetStreamData;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;

import vn.com.irtech.core.common.annotation.Log;
import vn.com.irtech.core.common.enums.BusinessType;
import vn.com.irtech.irbot.business.domain.InvoiceUploadHistory;
import vn.com.irtech.irbot.business.service.IInvoiceUploadHistoryService;
import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.utils.poi.ExcelUtil;
import vn.com.irtech.core.common.page.TableDataInfo;

import vn.com.irtech.core.common.config.ServerConfig;

import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

/**
 * Lịch sử nhập hóa đơnController
 * 
 * @author irtech
 * @date 2022-01-26
 */
@Controller
@RequestMapping("/business/invoice/history")
public class InvoiceUploadHistoryController extends BaseController {
	private String prefix = "business/invoice";

	@Autowired
	private IInvoiceUploadHistoryService invoiceUploadHistoryService;

	@Autowired
	private ServerConfig sever;

	@RequiresPermissions("business:history:view")
	@GetMapping("/history")
	public String history(ModelMap mmap) {
		mmap.put("domain", sever.getUrl());
		return prefix + "/upload-history";
	}

	/**
	 * Query list Lịch sử nhập hóa đơn
	 */
	@RequiresPermissions("business:history:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(InvoiceUploadHistory invoiceUploadHistory) {
		startPage();
		List<InvoiceUploadHistory> list = invoiceUploadHistoryService
				.selectInvoiceUploadHistoryList(invoiceUploadHistory);
		return getDataTable(list);
	}

	/**
	 * Export list Lịch sử nhập hóa đơn
	 */
	@RequiresPermissions("business:history:export")
	@Log(title = "Lịch sử nhập hóa đơn", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(InvoiceUploadHistory invoiceUploadHistory) {
		List<InvoiceUploadHistory> list = invoiceUploadHistoryService
				.selectInvoiceUploadHistoryList(invoiceUploadHistory);
		ExcelUtil<InvoiceUploadHistory> util = new ExcelUtil<InvoiceUploadHistory>(InvoiceUploadHistory.class);
		return util.exportExcel(list, "Lịch sử nhập hóa đơndữ liệu");
	}

	/**
	 * Added Lịch sử nhập hóa đơn
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * Added save Lịch sử nhập hóa đơn
	 */
	@RequiresPermissions("business:history:add")
	@Log(title = "Lịch sử nhập hóa đơn", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(InvoiceUploadHistory invoiceUploadHistory) {
		return toAjax(invoiceUploadHistoryService.insertInvoiceUploadHistory(invoiceUploadHistory));
	}

	/**
	 * Edit Lịch sử nhập hóa đơn
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		InvoiceUploadHistory invoiceUploadHistory = invoiceUploadHistoryService.selectInvoiceUploadHistoryById(id);
		mmap.put("invoiceUploadHistory", invoiceUploadHistory);
		return prefix + "/edit";
	}

	/**
	 * Edit and save Lịch sử nhập hóa đơn
	 */
	@RequiresPermissions("business:history:edit")
	@Log(title = "Lịch sử nhập hóa đơn", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(InvoiceUploadHistory invoiceUploadHistory) {
		return toAjax(invoiceUploadHistoryService.updateInvoiceUploadHistory(invoiceUploadHistory));
	}

	/**
	 * Remove Lịch sử nhập hóa đơn
	 */
	@RequiresPermissions("business:history:remove")
	@Log(title = "Lịch sử nhập hóa đơn", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(invoiceUploadHistoryService.deleteInvoiceUploadHistoryByIds(ids));
	}

	@PostMapping("download_file/{id}")
	@ResponseBody
	public AjaxResult downloadFile(@PathVariable("id") Long id) throws IOException {
		return invoiceUploadHistoryService.downloadInvoiceFile(id);
	}
}
