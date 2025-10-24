package com.morita.hotel.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.morita.system.general.core.controller.BaseController;
import com.morita.system.general.core.domain.AjaxResult;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;
import com.morita.hotel.domain.Checkout;
import com.morita.hotel.service.ICheckoutService;
import com.morita.system.general.utils.poi.ExcelUtil;
import com.morita.system.general.core.page.TableDataInfo;

/**
 * 退房记录Controller
 *
 * @author morita
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/hotel/checkout")
public class CheckoutController extends BaseController {
    @Autowired
    private ICheckoutService checkoutService;

    /**
     * 查询退房记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Checkout checkout) {
        startPage();
        List<Checkout> list = checkoutService.selectCheckoutList(checkout);
        return getDataTable(list);
    }

    /**
     * 导出退房记录列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Checkout checkout) {
        List<Checkout> list = checkoutService.selectCheckoutList(checkout);
        ExcelUtil<Checkout> util = new ExcelUtil<Checkout>(Checkout. class);
        util.exportExcel(response, list, "退房记录数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Checkout> util = new ExcelUtil<Checkout>(Checkout. class);
        util.importTemplateExcel(response, "退房记录数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Checkout> util = new ExcelUtil<Checkout>(Checkout. class);
        InputStream inputStream = file.getInputStream();
        List<Checkout> list = util.importExcel(inputStream);
        inputStream.close();
        int count = checkoutService.batchInsertCheckout(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取退房记录详细信息
     */
    @GetMapping(value = "/{checkoutId}")
    public AjaxResult getInfo(@PathVariable("checkoutId") String checkoutId) {
        return success(checkoutService.selectCheckoutByCheckoutId(checkoutId));
    }

    /**
     * 新增退房记录
     */
    @PostMapping
    public AjaxResult add(@RequestBody Checkout checkout) {
        return toAjax(checkoutService.insertCheckout(checkout));
    }

    /**
     * 修改退房记录
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Checkout checkout) {
        return toAjax(checkoutService.updateCheckout(checkout));
    }

    /**
     * 删除退房记录
     */
    @DeleteMapping("/{checkoutIds}")
    public AjaxResult remove(@PathVariable String[] checkoutIds) {
        return toAjax(checkoutService.deleteCheckoutByCheckoutIds(checkoutIds));
    }
}
