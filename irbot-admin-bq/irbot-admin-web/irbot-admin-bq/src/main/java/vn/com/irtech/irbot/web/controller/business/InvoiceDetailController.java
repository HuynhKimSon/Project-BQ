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
import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.enums.BusinessType;
import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.irbot.business.domain.InvoiceDetail;
import vn.com.irtech.irbot.business.service.IInvoiceDetailService;
import vn.com.irtech.core.common.utils.poi.ExcelUtil;

/**
 * Hoa don chi tietController
 * 
 * @author irtech
 * @date 2022-01-10
 */
@Controller
@RequestMapping("/business/invoice-detail")
public class InvoiceDetailController extends BaseController
{
    private String prefix = "business/invoice";

    @Autowired
    private IInvoiceDetailService invoiceDetailService;

    @RequiresPermissions("business:detail:view")
    @GetMapping()
    public String detail()
    {
        return prefix + "/invoice-detail";
    }

    /**
     * Query list Hoa don chi tiet
     */
    @RequiresPermissions("business:detail:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(InvoiceDetail invoiceDetail)
    {
        startPage();
        List<InvoiceDetail> list = invoiceDetailService.selectInvoiceDetailList(invoiceDetail);
        return getDataTable(list);
    }

    /**
     * Export list Hoa don chi tiet
     */
    @RequiresPermissions("business:detail:export")
    @Log(title = "Hoa don chi tiet", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(InvoiceDetail invoiceDetail)
    {
        List<InvoiceDetail> list = invoiceDetailService.selectInvoiceDetailList(invoiceDetail);
        ExcelUtil<InvoiceDetail> util = new ExcelUtil<InvoiceDetail>(InvoiceDetail.class);
        return util.exportExcel(list, "Hoa don chi tietdữ liệu");
    }


    /**
     * Added Hoa don chi tiet
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Added save Hoa don chi tiet
     */
    @RequiresPermissions("business:detail:add")
    @Log(title = "Hoa don chi tiet", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(InvoiceDetail invoiceDetail)
    {
        return toAjax(invoiceDetailService.insertInvoiceDetail(invoiceDetail));
    }

    /**
     * Edit Hoa don chi tiet
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        InvoiceDetail invoiceDetail = invoiceDetailService.selectInvoiceDetailById(id);
        mmap.put("invoiceDetail", invoiceDetail);
        return prefix + "/edit-detail";
    }

    /**
     * Edit and save Hoa don chi tiet
     */
    @RequiresPermissions("business:detail:edit")
    @Log(title = "Hoa don chi tiet", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(InvoiceDetail invoiceDetail)
    {
        return toAjax(invoiceDetailService.updateInvoiceDetail(invoiceDetail));
    }

    /**
     * Remove Hoa don chi tiet
     */
    @RequiresPermissions("business:detail:remove")
    @Log(title = "Hoa don chi tiet", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(invoiceDetailService.deleteInvoiceDetailByIds(ids));
    }
    
    
}
