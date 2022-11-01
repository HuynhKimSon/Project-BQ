package vn.com.irtech.irbot.web.controller.business;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import vn.com.irtech.core.common.annotation.Log;
import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.enums.BusinessType;
import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.core.common.utils.DateUtils;
import vn.com.irtech.core.framework.util.ShiroUtils;
import vn.com.irtech.irbot.business.domain.Invoice;
import vn.com.irtech.irbot.business.domain.InvoiceDetail;
import vn.com.irtech.irbot.business.dto.InvoiceInfo;
import vn.com.irtech.irbot.business.service.IInvoiceDetailService;
import vn.com.irtech.irbot.business.service.IInvoiceService;
import vn.com.irtech.irbot.business.service.IProcessBravoService;
import vn.com.irtech.irbot.business.service.IProcessMisaService;
import vn.com.irtech.irbot.business.type.InvoiceType;
import vn.com.irtech.irbot.business.type.ModeRunType;

/**
 * Quan ly hoa donController
 * 
 * @author irtech
 * @date 2022-01-10
 */
@Controller
@RequestMapping("/business/invoice")
public class InvoiceController extends BaseController {
	private String prefix = "business/invoice";

	@Autowired
	private IInvoiceService invoiceService;

	@Autowired
	private IInvoiceDetailService invoiceDetailService;

	@Autowired
	private IProcessMisaService processMisaService;

	@Autowired
	private IProcessBravoService processBravoService;

	@RequiresPermissions("business:invoice:view")
	@GetMapping()
	public String invoice() {
		return prefix + "/invoice";
	}

	/**
	 * Query list Quan ly hoa don
	 */
	@RequiresPermissions("business:invoice:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Invoice invoice) {
		startPage();
		List<InvoiceInfo> list = invoiceService.selectInvoiceInfoList(invoice);
		return getDataTable(list);
	}

	/**
	 * Edit Quan ly hoa don
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		Invoice invoiceSelect = new Invoice();
		invoiceSelect.setId(id);
		Invoice invoice = new Invoice();
		List<InvoiceInfo> list = invoiceService.selectInvoiceInfoList(invoiceSelect);
		if (CollectionUtils.isNotEmpty(list)) {
			invoice = list.get(0);
		}
		mmap.put("invoice", invoice);

		InvoiceDetail invoiceDetail = new InvoiceDetail();
		invoiceDetail.setInvoiceId(id);

		List<InvoiceDetail> detail = invoiceDetailService.selectInvoiceDetailList(invoiceDetail);
		mmap.put("details", detail);

		return prefix + "/edit";
	}

	/**
	 * Edit and save Quan ly hoa don
	 */
	@RequiresPermissions("business:invoice:edit")
	@Log(title = "Quan ly hoa don", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Invoice invoice) {
		return toAjax(invoiceService.updateInvoice(invoice));
	}

	/**
	 * Remove Quan ly hoa don
	 */
	@RequiresPermissions("business:invoice:remove")
	@Log(title = "Quan ly hoa don", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(invoiceService.deleteInvoiceByIds(ids));
	}

	/**
	 * Lấy chi tiết hóa đơn
	 */
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") Long id, ModelMap mmap) {
		Invoice invoice = invoiceService.selectInvoiceById(id);
		mmap.put("invoice", invoice);

		InvoiceDetail invoiceDetail = new InvoiceDetail();
		invoiceDetail.setInvoiceId(id);

		List<InvoiceDetail> detail = invoiceDetailService.selectInvoiceDetailList(invoiceDetail);
		mmap.put("details", detail);

		return prefix + "/detail";
	}

	@GetMapping("/import")
	public String invoiceImport() {
		return prefix + "/import";
	}

	@GetMapping("/upload")
	public String upload(ModelMap mmap) {
		return prefix + "/upload";
	}

	/**
	 * upload excel file
	 * 
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/upload")
	@ResponseBody
	public AjaxResult importInv(@RequestParam("file") MultipartFile file,
			@RequestParam("invoiceDate") String invoiceDate, @RequestParam("invoiceType") Integer invoiceType,
			@RequestParam("startHour") Integer startHour, @RequestParam("endHour") Integer endHour,
			@RequestParam("modeRun") Integer modeRun) throws Exception {
		try {
			Date invDate = null;
			ModeRunType modeRunType = ModeRunType.fromValue(modeRun);
			InvoiceType invType = InvoiceType.fromValue(invoiceType);

			if (modeRunType == null || invType == null) {
				return AjaxResult.error("Giá trị input không hợp lệ!");
			}

			if (ModeRunType.SCHEDULE == modeRunType) {
				invDate = DateUtils.dateTime("yyyy-MM-dd", invoiceDate);
			}

			// get the current user name
			String userName = ShiroUtils.getLoginName();

			AjaxResult ajaxResult = invoiceService.uploadInvoiceFile(file, invType, userName, invDate, startHour,
					endHour, modeRunType);
			return ajaxResult;
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error(e.getMessage());
		}
	}

	@PostMapping("/checkDataSendRobot")
	@ResponseBody
	public AjaxResult checkDataSendRobot(@RequestParam("uploadId") Long uploadId) throws Exception {
		try {
			Boolean result = invoiceService.checkDataSendRobot(uploadId);
			AjaxResult ajaxResult = AjaxResult.success();
			ajaxResult.put("isDiff", result);
			return ajaxResult;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return AjaxResult.error(e.getMessage());
		}
	}

	@PostMapping("/sendRobot")
	@ResponseBody
	public AjaxResult sendRobot(@RequestParam("uploadId") Long uploadId) throws Exception {
		try {

			int result = invoiceService.sendRobotProcess(uploadId);
			if (result == 0) {
				throw new Exception("Dữ liệu làm lệnh trống!");
			}

			AjaxResult ajaxResult = AjaxResult.success();
			ajaxResult.put("result", result);
			return ajaxResult;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return AjaxResult.error(e.getMessage());
		}
	}

	@PostMapping("/retry-bravo")
	@ResponseBody
	public AjaxResult retryBravo(String ids) {
		try {
			processBravoService.retry(ids);
			AjaxResult ajaxResult = AjaxResult.success();
			return ajaxResult;
		} catch (Exception e) {
			logger.error(">>>>>> Error: " + e.getMessage());
			return AjaxResult.error(e.getMessage());
		}
	}

	@PostMapping("/retry-misa")
	@ResponseBody
	public AjaxResult retryMisa(String ids) {
		try {
			processMisaService.retry(ids);
			AjaxResult ajaxResult = AjaxResult.success();
			return ajaxResult;
		} catch (Exception e) {
			logger.error(">>>>>> Error: " + e.getMessage());
			return AjaxResult.error(e.getMessage());
		}
	}
	
	@PostMapping("/randomIndex")
	@ResponseBody
	public AjaxResult randomIndex(@RequestParam("uploadId") Long uploadId) throws Exception {
		try {

			long result = invoiceService.randomIndexInvoice(uploadId);
			AjaxResult ajaxResult = AjaxResult.success();
			ajaxResult.put("uploadId", result);
			return ajaxResult;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return AjaxResult.error(e.getMessage());
		}
	}
	
	@PostMapping("/mergeInvoice")
	@ResponseBody
	public AjaxResult mergeInv(String ids) {
		try {
			invoiceService.mergeInv(ids);
			AjaxResult ajaxResult = AjaxResult.success();
			return ajaxResult;
		} catch (Exception e) {
			logger.error(">>>>>> Error: " + e.getMessage());
			return AjaxResult.error(e.getMessage());
		}
	}
}
