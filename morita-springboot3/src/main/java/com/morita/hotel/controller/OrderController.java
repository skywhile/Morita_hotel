package com.morita.hotel.controller;

import java.math.BigDecimal;
import java.util.List;

import com.morita.system.domain.SysUser;
import com.morita.system.service.ISysUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import com.morita.hotel.domain.Order;
import com.morita.hotel.service.IOrderService;
import com.morita.system.general.utils.poi.ExcelUtil;
import com.morita.system.general.core.page.TableDataInfo;

/**
 * 订单Controller
 *
 * @author morita
 * @date 2025-09-16
 */
@RestController
@RequestMapping("/hotel/order")
public class OrderController extends BaseController {
    @Autowired
    private IOrderService orderService;

    @Autowired
    private ISysUserService userService;

    /**
     * 查询订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Order order) {
        startPage();
        List<Order> list = orderService.selectOrderList(order);
        return getDataTable(list);
    }

    /**
     * 导出订单列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Order order) {
        List<Order> list = orderService.selectOrderList(order);
        ExcelUtil<Order> util = new ExcelUtil<Order>(Order.class);
        util.exportExcel(response, list, "订单数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Order> util = new ExcelUtil<Order>(Order.class);
        util.importTemplateExcel(response, "订单数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Order> util = new ExcelUtil<Order>(Order.class);
        InputStream inputStream = file.getInputStream();
        List<Order> list = util.importExcel(inputStream);
        inputStream.close();
        int count = orderService.batchInsertOrder(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取订单详细信息
     */
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") String orderId) {
        return success(orderService.selectOrderByOrderId(orderId));
    }

    /**
     * 新增订单
     */
    @PostMapping
    public AjaxResult add(@RequestBody Order order) {
        return toAjax(orderService.insertOrder(order));
    }

    /**
     * 修改订单
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Order order) {
        return toAjax(orderService.updateOrder(order));
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable String[] orderIds) {
        return toAjax(orderService.deleteOrderByOrderIds(orderIds));
    }

    /**
     * 充值
     */
    @PutMapping("/recharge/{amount}")
    public AjaxResult recharge(@PathVariable BigDecimal amount) {
        try {
            //参数校验
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                return error("充值金额必须大于0");
            }

            //当前用户ID
            Long userId = getUserId();
            //当前用户信息
            SysUser currentUser = userService.selectUserById(userId);

            if (currentUser == null) {
                return error("用户不存在");
            }

            //该用户充值前的余额
            BigDecimal oldBalance = currentUser.getBalance();

            //计算充值后的余额
            BigDecimal newBalance = oldBalance.add(amount);

            //更新用户信息
            SysUser user = new SysUser();
            user.setUserId(userId);
            user.setBalance(newBalance);
            //更新结果
            int result = userService.updateUser(user);

            if (result > 0) {
                //充值成功
                return AjaxResult.success("充值成功", newBalance);
            } else {
                //充值失败
                return error("充值失败");
            }

        } catch (Exception e) {
            return error("系统异常, 充值失败");
        }
    }

    /**
     * 取消订单
     */
    @Transactional
    @PutMapping("/cancel/{orderId}")
    public AjaxResult cancelOrder(@PathVariable String orderId) {
        //根据订单查询订单信息
        Order toOrder = orderService.selectOrderByOrderId(orderId);

        //订单所属的用户ID
        Long userId = toOrder.getUserId();

        //查询用户取消订单前的余额
        BigDecimal oldBalance = userService.selectUserById(userId).getBalance();

        //订单的总价
        BigDecimal totalPrice = toOrder.getTotalPrice();

        //1.对用户进行退款处理 (取消订单前的余额 + 订单的总价)
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setBalance(oldBalance.add(totalPrice));
        userService.updateUser(user);

        //2.将订单状态修改为已取消
        Order order = new Order();
        order.setOrderId(orderId);
        order.setStatus("已取消");

        return toAjax(orderService.updateOrder(order));
    }

}
