package com.morita.hotel.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.morita.hotel.mapper.RoomCategoryMapper;
import com.morita.system.general.utils.DateUtils;
import com.morita.system.general.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.morita.hotel.mapper.RoomMapper;
import com.morita.hotel.domain.Room;
import com.morita.hotel.service.IRoomService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 房间Service业务层处理
 *
 * @author morita
 * @date 2025-09-15
 */
@Service
public class RoomServiceImpl implements IRoomService {
    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private RoomCategoryMapper roomCategoryMapper;

    /**
     * 查询房间
     *
     * @param roomId 房间主键
     * @return 房间
     */
    @Override
    public Room selectRoomByRoomId(String roomId) {
        return roomMapper.selectRoomByRoomId(roomId);
    }

    /**
     * 查询房间列表
     *
     * @param room 房间
     * @return 房间
     */
    @Override
    public List<Room> selectRoomList(Room room) {
        return roomMapper.selectRoomList(room);
    }

    /**
     * 新增房间
     *
     * @param room 房间
     * @return 结果
     */
    @Override
    public int insertRoom(Room room) {
        room.setCreateTime(DateUtils.getNowDate());
        room.setRoomId(IdUtils.fastSimpleUUID());
        return roomMapper.insertRoom(room);
    }

    /**
     * 批量新增房间
     *
     * @param rooms 房间List
     * @return 结果
     */
    @Override
    public int batchInsertRoom(List<Room> rooms) {
        List<String> roomNames = rooms.stream()
                .map(Room::getName)
                .toList();

        //根据单个或多个房间名称批量查询分类ID
        Map<String, Map<String, Object>> categoryMap = roomCategoryMapper.selectCategoryIdsByNames(roomNames);

        //检查所有房间的分类
        for (Room room : rooms) {
            Map<String, Object> categoryInfo = categoryMap.get(room.getName());
            if (categoryInfo == null || categoryInfo.get("category_id") == null) {
                throw new RuntimeException("房间" + room.getName() + " 的分类不存在, 请检查Excel导入文件");
            }
        }

        //所有分类检查通过后, 开始批量插入
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        try {
            //设置roomId和categoryId
            for (int i = 0; i < rooms.size(); i++) {
                Room room = rooms.get(i);
                Map<String, Object> categoryInfo = categoryMap.get(room.getName());
                String categoryId = (String) categoryInfo.get("category_id");
                room.setRoomId(IdUtils.fastSimpleUUID());
                room.setCategoryId(categoryId);
                roomMapper.insertRoom(rooms.get(i));
                // 防止内存溢出，每100次提交一次,并清除缓存
                boolean bool = (i > 0 && i % 100 == 0) || i == rooms.size() - 1;
                if (bool) {
                    sqlSession.commit();
                    sqlSession.clearCache();
                }
                count = i + 1;
            }
        } catch (Exception e) {
            // 没有提交的数据可以回滚
            sqlSession.rollback();
            // 抛出异常, 让接口层能够捕获
            throw e;
        } finally {
            sqlSession.close();
        }
        return count;
    }

    /**
     * 修改房间
     *
     * @param room 房间
     * @return 结果
     */
    @Override
    public int updateRoom(Room room) {
        return roomMapper.updateRoom(room);
    }

    /**
     * 批量删除房间
     *
     * @param roomIds 需要删除的房间主键
     * @return 结果
     */
    @Override
    public int deleteRoomByRoomIds(String[] roomIds) {
        return roomMapper.deleteRoomByRoomIds(roomIds);
    }

    /**
     * 删除房间信息
     *
     * @param roomId 房间主键
     * @return 结果
     */
    @Override
    public int deleteRoomByRoomId(String roomId) {
        return roomMapper.deleteRoomByRoomId(roomId);
    }

    /**
     * 根据房间类型ID查询房间号列表
     * @param categoryId
     * @return
     */
    @Override
    public List<Room> selectRoomNumberListByCategoryId(String categoryId) {
        return roomMapper.selectRoomNumberListByCategoryId(categoryId);
    }
}
