package com.morita.hotel.service;

import java.util.List;
import com.morita.hotel.domain.Checkin;

/**
 * 入住登记Service接口
 *
 * @author morita
 * @date 2025-09-18
 */
public interface ICheckinService
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
     * 批量新增入住登记
     *
     * @param checkins 入住登记List
     * @return 结果
     */
    public int batchInsertCheckin(List<Checkin> checkins);

    /**
     * 修改入住登记
     *
     * @param checkin 入住登记
     * @return 结果
     */
    public int updateCheckin(Checkin checkin);

    /**
     * 批量删除入住登记
     *
     * @param checkinIds 需要删除的入住登记主键集合
     * @return 结果
     */
    public int deleteCheckinByCheckinIds(String[] checkinIds);

    /**
     * 删除入住登记信息
     *
     * @param checkinId 入住登记主键
     * @return 结果
     */
    public int deleteCheckinByCheckinId(String checkinId);
}
