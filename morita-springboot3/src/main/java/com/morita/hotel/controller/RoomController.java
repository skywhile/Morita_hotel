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
import com.morita.hotel.domain.Room;
import com.morita.hotel.service.IRoomService;
import com.morita.system.general.utils.poi.ExcelUtil;
import com.morita.system.general.core.page.TableDataInfo;

/**
 * 房间Controller
 *
 * @author morita
 * @date 2025-09-15
 */
@RestController
@RequestMapping("/hotel/room")
public class RoomController extends BaseController {
    @Autowired
    private IRoomService roomService;

    /**
     * 查询房间列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Room room) {
        startPage();
        List<Room> list = roomService.selectRoomList(room);
        return getDataTable(list);
    }

    /**
     * 导出房间列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Room room) {
        List<Room> list = roomService.selectRoomList(room);
        ExcelUtil<Room> util = new ExcelUtil<Room>(Room. class);
        util.exportExcel(response, list, "房间数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Room> util = new ExcelUtil<Room>(Room. class);
        util.importTemplateExcel(response, "房间数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        try {
            ExcelUtil<Room> util = new ExcelUtil<Room>(Room. class);
            List<Room> list;
            try (InputStream inputStream = file.getInputStream()) {
                list = util.importExcel(inputStream);
            }
            int count = roomService.batchInsertRoom(list);
            return AjaxResult.success("导入成功" + count + "条信息！");
        } catch (RuntimeException e) {
            //捕获分类不存在的异常
            return error(e.getMessage());
        } catch (Exception e) {
            //捕获其他异常
            return error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 获取房间详细信息
     */
    @GetMapping(value = "/{roomId}")
    public AjaxResult getInfo(@PathVariable("roomId") String roomId) {
        return success(roomService.selectRoomByRoomId(roomId));
    }

    /**
     * 新增房间
     */
    @PostMapping
    public AjaxResult add(@RequestBody Room room) {
        return toAjax(roomService.insertRoom(room));
    }

    /**
     * 修改房间
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Room room) {
        return toAjax(roomService.updateRoom(room));
    }

    /**
     * 删除房间
     */
    @DeleteMapping("/{roomIds}")
    public AjaxResult remove(@PathVariable String[] roomIds) {
        return toAjax(roomService.deleteRoomByRoomIds(roomIds));
    }

    /**
     * 根据房间类型ID查询房间号列表
     */
    @GetMapping("/selectRoomNumberListByCategoryId/{categoryId}")
    public AjaxResult selectRoomNumberListByCategoryId(@PathVariable("categoryId") String categoryId) {
        return success(roomService.selectRoomNumberListByCategoryId(categoryId));
    }

}
