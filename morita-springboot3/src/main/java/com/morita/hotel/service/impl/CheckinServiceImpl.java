package com.morita.hotel.service.impl;

import java.util.List;

import com.morita.hotel.domain.Order;
import com.morita.hotel.domain.Room;
import com.morita.hotel.service.IOrderService;
import com.morita.hotel.service.IRoomService;
import com.morita.system.general.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.morita.hotel.mapper.CheckinMapper;
import com.morita.hotel.domain.Checkin;
import com.morita.hotel.service.ICheckinService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 入住登记Service业务层处理
 *
 * @author morita
 * @date 2025-09-18
 */
@Service
public class CheckinServiceImpl implements ICheckinService {
    @Autowired
    private CheckinMapper checkinMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IRoomService roomService;

    /**
     * 查询入住登记
     *
     * @param checkinId 入住登记主键
     * @return 入住登记
     */
    @Override
    public Checkin selectCheckinByCheckinId(String checkinId) {
        return checkinMapper.selectCheckinByCheckinId(checkinId);
    }

    /**
     * 查询入住登记列表
     *
     * @param checkin 入住登记
     * @return 入住登记
     */
    @Override
    public List<Checkin> selectCheckinList(Checkin checkin) {
        return checkinMapper.selectCheckinList(checkin);
    }

    /**
     * 新增入住登记
     *
     * @param checkin 入住登记
     * @return 结果
     */
    @Override
    @Transactional
    public int insertCheckin(Checkin checkin) {
        checkin.setCheckinId(IdUtils.fastSimpleUUID());

        //1.订单状态变更为已入住
        Order order = new Order();
        order.setOrderId(checkin.getOrderId());
        order.setStatus("已入住");
        orderService.updateOrder(order);

        //2.房间状态变更为已入住
        Room room = new Room();
        room.setRoomId(checkin.getRoomId());
        room.setStatus("已入住");
        roomService.updateRoom(room);

        //3.增加一条入住登记记录
        return checkinMapper.insertCheckin(checkin);
    }

    /**
     * 批量新增入住登记
     *
     * @param checkins 入住登记List
     * @return 结果
     */
    @Override
    public int batchInsertCheckin(List<Checkin> checkins) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(checkins)) {
            try {
                for (int i = 0; i < checkins.size(); i++) {
                    int row = checkinMapper.insertCheckin(checkins.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i > 0 && i % 100 == 0) || i == checkins.size() - 1;
                    if (bool) {
                        sqlSession.commit();
                        sqlSession.clearCache();
                    }
                    count = i + 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 没有提交的数据可以回滚
                sqlSession.rollback();
            } finally {
                sqlSession.close();
                return count;
            }
        }
        return count;
    }

    /**
     * 修改入住登记
     *
     * @param checkin 入住登记
     * @return 结果
     */
    @Override
    public int updateCheckin(Checkin checkin) {
        return checkinMapper.updateCheckin(checkin);
    }

    /**
     * 批量删除入住登记
     *
     * @param checkinIds 需要删除的入住登记主键
     * @return 结果
     */
    @Override
    public int deleteCheckinByCheckinIds(String[] checkinIds) {
        return checkinMapper.deleteCheckinByCheckinIds(checkinIds);
    }

    /**
     * 删除入住登记信息
     *
     * @param checkinId 入住登记主键
     * @return 结果
     */
    @Override
    public int deleteCheckinByCheckinId(String checkinId) {
        return checkinMapper.deleteCheckinByCheckinId(checkinId);
    }
}
