package com.morita.hotel.service.impl;

import java.util.List;

import com.morita.hotel.domain.Order;
import com.morita.hotel.domain.Room;
import com.morita.hotel.mapper.CheckinMapper;
import com.morita.hotel.service.IOrderService;
import com.morita.hotel.service.IRoomService;
import com.morita.system.general.utils.DateUtils;
import com.morita.system.general.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.morita.hotel.mapper.CheckoutMapper;
import com.morita.hotel.domain.Checkout;
import com.morita.hotel.service.ICheckoutService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 退房记录Service业务层处理
 *
 * @author morita
 * @date 2025-09-18
 */
@Service
public class CheckoutServiceImpl implements ICheckoutService {
    @Autowired
    private CheckoutMapper checkoutMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private CheckinMapper checkinMapper;

    @Autowired
    private IRoomService roomService;

    @Autowired
    private IOrderService orderService;

    /**
     * 查询退房记录
     *
     * @param checkoutId 退房记录主键
     * @return 退房记录
     */
    @Override
    public Checkout selectCheckoutByCheckoutId(String checkoutId) {
        return checkoutMapper.selectCheckoutByCheckoutId(checkoutId);
    }

    /**
     * 查询退房记录列表
     *
     * @param checkout 退房记录
     * @return 退房记录
     */
    @Override
    public List<Checkout> selectCheckoutList(Checkout checkout) {
        return checkoutMapper.selectCheckoutList(checkout);
    }

    /**
     * 新增退房记录
     *
     * @param checkout 退房记录
     * @return 结果
     */
    @Override
    public int insertCheckout(Checkout checkout) {
        checkout.setCreateTime(DateUtils.getNowDate());
        checkout.setCheckoutId(IdUtils.fastSimpleUUID());

        //根据订单ID查询房间ID
        String roomId = checkinMapper.selectRoomIdByOrderId(checkout.getOrderId());

        //将房间状态修改为空闲
        Room room = new Room();
        room.setRoomId(roomId);
        room.setStatus("空闲");
        roomService.updateRoom(room);

        //将订单状态修改为已完成
        Order order = new Order();
        order.setOrderId(checkout.getOrderId());
        order.setStatus("已完成");
        orderService.updateOrder(order);

        //新增一条退房记录
        return checkoutMapper.insertCheckout(checkout);
    }

    /**
     * 批量新增退房记录
     *
     * @param checkouts 退房记录List
     * @return 结果
     */
    @Override
    public int batchInsertCheckout(List<Checkout> checkouts) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(checkouts)) {
            try {
                for (int i = 0; i < checkouts.size(); i++) {
                    int row = checkoutMapper.insertCheckout(checkouts.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i > 0 && i % 100 == 0) || i == checkouts.size() - 1;
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
     * 修改退房记录
     *
     * @param checkout 退房记录
     * @return 结果
     */
    @Override
    public int updateCheckout(Checkout checkout) {
        return checkoutMapper.updateCheckout(checkout);
    }

    /**
     * 批量删除退房记录
     *
     * @param checkoutIds 需要删除的退房记录主键
     * @return 结果
     */
    @Override
    public int deleteCheckoutByCheckoutIds(String[] checkoutIds) {
        return checkoutMapper.deleteCheckoutByCheckoutIds(checkoutIds);
    }

    /**
     * 删除退房记录信息
     *
     * @param checkoutId 退房记录主键
     * @return 结果
     */
    @Override
    public int deleteCheckoutByCheckoutId(String checkoutId) {
        return checkoutMapper.deleteCheckoutByCheckoutId(checkoutId);
    }
}
