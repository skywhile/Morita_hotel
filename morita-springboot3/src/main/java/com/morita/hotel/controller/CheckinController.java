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
import com.morita.hotel.domain.Checkin;
import com.morita.hotel.service.ICheckinService;
import com.morita.system.general.utils.poi.ExcelUtil;
import com.morita.system.general.core.page.TableDataInfo;

/**
 * 入住登记Controller
 *
 * @author morita
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/hotel/checkin")
public class CheckinController extends BaseController {
    @Autowired
    private ICheckinService checkinService;

    /**
     * 查询入住登记列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Checkin checkin) {
        startPage();
        List<Checkin> list = checkinService.selectCheckinList(checkin);
        return getDataTable(list);
    }

    /**
     * 导出入住登记列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Checkin checkin) {
        List<Checkin> list = checkinService.selectCheckinList(checkin);
        ExcelUtil<Checkin> util = new ExcelUtil<Checkin>(Checkin. class);
        util.exportExcel(response, list, "入住登记数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Checkin> util = new ExcelUtil<Checkin>(Checkin. class);
        util.importTemplateExcel(response, "入住登记数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Checkin> util = new ExcelUtil<Checkin>(Checkin. class);
        InputStream inputStream = file.getInputStream();
        List<Checkin> list = util.importExcel(inputStream);
        inputStream.close();
        int count = checkinService.batchInsertCheckin(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取入住登记详细信息
     */
    @GetMapping(value = "/{checkinId}")
    public AjaxResult getInfo(@PathVariable("checkinId") String checkinId) {
        return success(checkinService.selectCheckinByCheckinId(checkinId));
    }

    /**
     * 新增入住登记
     */
    @PostMapping
    public AjaxResult add(@RequestBody Checkin checkin) {
        return toAjax(checkinService.insertCheckin(checkin));
    }

    /**
     * 修改入住登记
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Checkin checkin) {
        return toAjax(checkinService.updateCheckin(checkin));
    }

    /**
     * 删除入住登记
     */
    @DeleteMapping("/{checkinIds}")
    public AjaxResult remove(@PathVariable String[] checkinIds) {
        return toAjax(checkinService.deleteCheckinByCheckinIds(checkinIds));
    }
}
