package com.morita.hotel.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.morita.hotel.domain.Checkin;

/**
 * 入住登记Mapper接口
 *
 * @author morita
 * @date 2025-09-18
 */
@Mapper
public interface CheckinMapper
{
    /**
     * 查询入住登记
     *
     * @param checkinId 入住登记主键
     * @return 入住登记
     */
    public Checkin selectCheckinByCheckinId(String checkinId);

    /**
     * 查询入住登记列表
     *
     * @param checkin 入住登记
     * @return 入住登记集合
     */
    public List<Checkin> selectCheckinList(Checkin checkin);

    /**
     * 新增入住登记
     *
     * @param checkin 入住登记
     * @return 结果
     */
    public int insertCheckin(Checkin checkin);

    /**
     * 修改入住登记
     *
     * @param checkin 入住登记
     * @return 结果
     */
    public int updateCheckin(Checkin checkin);

    /**
     * 删除入住登记
     *
     * @param checkinId 入住登记主键
     * @return 结果
     */
    public int deleteCheckinByCheckinId(String checkinId);

    /**
     * 批量删除入住登记
     *
     * @param checkinIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCheckinByCheckinIds(String[] checkinIds);

    /**
     * 根据订单ID查询房间ID
     */
    String selectRoomIdByOrderId(String orderId);

}
