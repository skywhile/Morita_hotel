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
import com.morita.hotel.domain.Restaurant;
import com.morita.hotel.service.IRestaurantService;
import com.morita.system.general.utils.poi.ExcelUtil;
import com.morita.system.general.core.page.TableDataInfo;

/**
 * 餐厅Controller
 *
 * @author morita
 * @date 2025-09-19
 */
@RestController
@RequestMapping("/hotel/restaurant")
public class RestaurantController extends BaseController {
    @Autowired
    private IRestaurantService restaurantService;

    /**
     * 查询餐厅列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Restaurant restaurant) {
        startPage();
        List<Restaurant> list = restaurantService.selectRestaurantList(restaurant);
        return getDataTable(list);
    }

    /**
     * 导出餐厅列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Restaurant restaurant) {
        List<Restaurant> list = restaurantService.selectRestaurantList(restaurant);
        ExcelUtil<Restaurant> util = new ExcelUtil<Restaurant>(Restaurant. class);
        util.exportExcel(response, list, "餐厅数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Restaurant> util = new ExcelUtil<Restaurant>(Restaurant. class);
        util.importTemplateExcel(response, "餐厅数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Restaurant> util = new ExcelUtil<Restaurant>(Restaurant. class);
        InputStream inputStream = file.getInputStream();
        List<Restaurant> list = util.importExcel(inputStream);
        inputStream.close();
        int count = restaurantService.batchInsertRestaurant(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取餐厅详细信息
     */
    @GetMapping(value = "/{restaurantId}")
    public AjaxResult getInfo(@PathVariable("restaurantId") String restaurantId) {
        return success(restaurantService.selectRestaurantByRestaurantId(restaurantId));
    }

    /**
     * 新增餐厅
     */
    @PostMapping
    public AjaxResult add(@RequestBody Restaurant restaurant) {
        return toAjax(restaurantService.insertRestaurant(restaurant));
    }

    /**
     * 修改餐厅
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Restaurant restaurant) {
        return toAjax(restaurantService.updateRestaurant(restaurant));
    }

    /**
     * 删除餐厅
     */
    @DeleteMapping("/{restaurantIds}")
    public AjaxResult remove(@PathVariable String[] restaurantIds) {
        return toAjax(restaurantService.deleteRestaurantByRestaurantIds(restaurantIds));
    }
}
