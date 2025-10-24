package com.morita.hotel.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
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
import com.morita.hotel.domain.RoomCategory;
import com.morita.hotel.service.IRoomCategoryService;
import com.morita.system.general.utils.poi.ExcelUtil;
import com.morita.system.general.core.page.TableDataInfo;

/**
 * 房间分类Controller
 *
 * @author morita
 * @date 2025-09-14
 */
@RestController
@RequestMapping("/hotel/category")
public class RoomCategoryController extends BaseController {
    @Autowired
    private IRoomCategoryService roomCategoryService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询房间分类列表
     */
    @GetMapping("/list")
    public TableDataInfo list(RoomCategory roomCategory) {
        String key = "roomCategory";
        List<RoomCategory> list = (List) redisTemplate.opsForValue().get(key);
        if (list != null && list.size() > 0){
            // 存在缓存,直接返回
            return getDataTable(list);
        }
        startPage();
        list = roomCategoryService.selectRoomCategoryList(roomCategory);
        redisTemplate.opsForValue().set(key,list);

        return getDataTable(list);
    }

    /**
     * 导出房间分类列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, RoomCategory roomCategory) {
        List<RoomCategory> list = roomCategoryService.selectRoomCategoryList(roomCategory);
        ExcelUtil<RoomCategory> util = new ExcelUtil<RoomCategory>(RoomCategory. class);
        util.exportExcel(response, list, "房间分类数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<RoomCategory> util = new ExcelUtil<RoomCategory>(RoomCategory. class);
        util.importTemplateExcel(response, "房间分类数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<RoomCategory> util = new ExcelUtil<RoomCategory>(RoomCategory. class);
        InputStream inputStream = file.getInputStream();
        List<RoomCategory> list = util.importExcel(inputStream);
        inputStream.close();
        int count = roomCategoryService.batchInsertRoomCategory(list);
        String key = "roomCategory";
        redisTemplate.delete(key);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取房间分类详细信息
     */
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") String categoryId) {
        return success(roomCategoryService.selectRoomCategoryByCategoryId(categoryId));
    }

    /**
     * 新增房间分类
     */
    @PostMapping
    public AjaxResult add(@RequestBody RoomCategory roomCategory) {
        int insertRoomCategory = roomCategoryService.insertRoomCategory(roomCategory);
        String key = "roomCategory";
        redisTemplate.delete(key);
        return toAjax(insertRoomCategory);
    }

    /**
     * 修改房间分类
     */
    @PutMapping
    public AjaxResult edit(@RequestBody RoomCategory roomCategory) {
        String key = "roomCategory";
        int updateRoomCategory = roomCategoryService.updateRoomCategory(roomCategory);
        redisTemplate.delete(key);
        return toAjax(updateRoomCategory);
    }

    /**
     * 删除房间分类
     */
    @DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable String[] categoryIds) {
        int deleteRoomCategoryByCategoryIds = roomCategoryService.deleteRoomCategoryByCategoryIds(categoryIds);
        String key = "roomCategory";
        redisTemplate.delete(key);
        return toAjax(deleteRoomCategoryByCategoryIds);
    }

    /**
     * 不分页查询房间类型列表
     */
    @GetMapping("/selectCategoryAllList")
    public AjaxResult selectCategoryAllList() {
        String key = "roomCategory";
        List<RoomCategory> list = (List) redisTemplate.opsForValue().get(key);
        if (list != null && list.size() > 0){
            // 存在缓存,直接返回
            return success(list);
        }
        list = roomCategoryService.selectCategoryAllList();
        redisTemplate.opsForValue().set(key,list);
        return success(list);
    }

}
