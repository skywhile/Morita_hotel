package com.morita.hotel.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.morita.system.domain.SysUser;
import com.morita.system.general.utils.DateUtils;
import com.morita.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.morita.hotel.mapper.OrderMapper;
import com.morita.hotel.domain.Order;
import com.morita.hotel.service.IOrderService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static com.morita.system.general.utils.SecurityUtils.getUserId;

/**
 * 订单Service业务层处理
 *
 * @author morita
 * @date 2025-09-16
 */
@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private ISysUserService userService;

    /**
     * 查询订单
     *
     * @param orderId 订单主键
     * @return 订单
     */
    @Override
    public Order selectOrderByOrderId(String orderId) {
        return orderMapper.selectOrderByOrderId(orderId);
    }

    /**
     * 查询订单列表
     *
     * @param order 订单
     * @return 订单
     */
    @Override
    public List<Order> selectOrderList(Order order) {
        return orderMapper.selectOrderList(order);
    }

    /**
     * 新增订单
     *
     * @param order 订单
     * @return 结果
     */
    @Override
    @Transactional
    public int insertOrder(Order order) {
        //当前操作用户的用户ID
        Long loginUserId = getUserId();

        //查询用户支付前的余额
        BigDecimal oldBalance = userService.selectUserById(loginUserId).getBalance();

        //如果余额小于总价, 不能预订
        if (oldBalance.compareTo(order.getTotalPrice()) < 0) {
            throw new RuntimeException("您的余额不足, 请充值后预订");
        } else {
            //余额充足, 进行扣款
            SysUser user = new SysUser();
            user.setUserId(loginUserId);
            user.setBalance(oldBalance.subtract(order.getTotalPrice()));
            userService.updateUser(user);
        }

        order.setCreateTime(DateUtils.getNowDate());
        order.setUserId(loginUserId);

        //获取当前日期时间
        LocalDateTime now = LocalDateTime.now();
        //定义格式器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        //格式化为字符串
        String formatDateTime = now.format(formatter);
        order.setOrderId("HD" + formatDateTime + loginUserId);

        return orderMapper.insertOrder(order);
    }

    /**
     * 批量新增订单
     *
     * @param orders 订单List
     * @return 结果
     */
    @Override
    public int batchInsertOrder(List<Order> orders) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(orders)) {
            try {
                for (int i = 0; i < orders.size(); i++) {
                    int row = orderMapper.insertOrder(orders.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i > 0 && i % 100 == 0) || i == orders.size() - 1;
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
     * 修改订单
     *
     * @param order 订单
     * @return 结果
     */
    @Override
    public int updateOrder(Order order) {
        return orderMapper.updateOrder(order);
    }

    /**
     * 批量删除订单
     *
     * @param orderIds 需要删除的订单主键
     * @return 结果
     */
    @Override
    public int deleteOrderByOrderIds(String[] orderIds) {
        return orderMapper.deleteOrderByOrderIds(orderIds);
    }

    /**
     * 删除订单信息
     *
     * @param orderId 订单主键
     * @return 结果
     */
    @Override
    public int deleteOrderByOrderId(String orderId) {
        return orderMapper.deleteOrderByOrderId(orderId);
    }
}
