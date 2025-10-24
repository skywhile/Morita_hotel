package com.morita.hotel.controller;

import com.morita.hotel.domain.Room;
import com.morita.hotel.service.IRoomService;
import com.morita.system.domain.SysUser;
import com.morita.system.general.core.domain.AjaxResult;
import com.morita.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * 酒店管理系统首页仪表盘Controller
 */
@RestController
@RequestMapping("/hotel/char")
public class CarController {
    @Autowired
    private IRoomService roomService;

    @Autowired
    private ISysUserService userService;

    /**
     * 获取首页统计数据 (房间总数, 入住数, 总用户数)
     */
    @GetMapping("/stats")
    public AjaxResult getStats() {
        //获取所有房间
        List<Room> allRooms = roomService.selectRoomList(new Room());

        //计算统计数据
        int totalRooms = allRooms.size();
        int occupiedRooms = 0;
        int availableRooms = 0;

        //根据房间状态进行统计
        for (Room room : allRooms) {
            if ("已入住".equals(room.getStatus())) {
                occupiedRooms++;
            } else if ("空闲".equals(room.getStatus())) {
                availableRooms++;
            }
        }

        //用户总数
        int totalUsers = userService.selectUserList(new SysUser()).size();

        HashMap<Object, Object> stats = new HashMap<>();
        stats.put("totalRooms", totalRooms);
        stats.put("occupiedRooms", occupiedRooms);
        stats.put("availableRooms", availableRooms);
        stats.put("totalUsers", totalUsers);

        return AjaxResult.success(stats);
    }

    /**
     * 获取客房状态统计数据
     */
    @GetMapping("/roomStatus")
    public AjaxResult getRoomStatusStat() {
        // 获取所有房间
        List<Room> allRooms = roomService.selectRoomList(new Room());
        int occupiedCount = 0; //已入住的
        int availableCount = 0; //空闲的

        //根据房间状态进行统计
        for (Room room : allRooms) {
            if ("已入住".equals(room.getStatus())) {
                occupiedCount++;
            } else {
                availableCount++;
            }
        }

        HashMap<Object, Object> result = new HashMap<>();
        result.put("occupied", occupiedCount);
        result.put("available", availableCount);


        return AjaxResult.success(result);
    }

    /**
     * 获取房型分布数据
     */
    @GetMapping("/roomType")
    public AjaxResult getRoomType() {
        //获取所有房间
        List<Room> allRooms = roomService.selectRoomList(new Room());
        HashMap<String, Integer> typeCountMap = new HashMap<>();

        //统计各房型的数量
        for (Room room : allRooms) {
            String typeName = room.getName();
            if (typeName != null) {
                typeCountMap.put(typeName, typeCountMap.getOrDefault(typeName, 0) + 1);
            }
        }
        return AjaxResult.success(typeCountMap);
    }

}
