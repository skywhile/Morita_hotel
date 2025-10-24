package com.morita.hotel.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.morita.hotel.domain.Room;

/**
 * 房间Mapper接口
 *
 * @author morita
 * @date 2025-09-15
 */
@Mapper
public interface RoomMapper
{
    /**
     * 查询房间
     *
     * @param roomId 房间主键
     * @return 房间
     */
    public Room selectRoomByRoomId(String roomId);

    /**
     * 查询房间列表
     *
     * @param room 房间
     * @return 房间集合
     */
    public List<Room> selectRoomList(Room room);

    /**
     * 新增房间
     *
     * @param room 房间
     * @return 结果
     */
    public int insertRoom(Room room);

    /**
     * 修改房间
     *
     * @param room 房间
     * @return 结果
     */
    public int updateRoom(Room room);

    /**
     * 删除房间
     *
     * @param roomId 房间主键
     * @return 结果
     */
    public int deleteRoomByRoomId(String roomId);

    /**
     * 批量删除房间
     *
     * @param roomIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRoomByRoomIds(String[] roomIds);

    /**
     * 根据房间类型ID查询房间号列表
     * @param categoryId
     * @return
     */
    List<Room> selectRoomNumberListByCategoryId(String categoryId);
}
