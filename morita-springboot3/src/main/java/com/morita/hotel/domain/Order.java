package com.morita.hotel.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.morita.system.general.annotation.Excel;
import com.morita.system.general.core.domain.BaseEntity;
import lombok.*;

/**
 * 订单对象 order
 *
 * @author morita
 * @date 2025-09-16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单号 */
    private String orderId;

    /** 房间类型ID */
    @Excel(name = "房间类型ID")
    private String categoryId;

    /** 入住日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入住日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkInDate;

    /** 退房日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "退房日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkOutDate;

    /** 天数 */
    @Excel(name = "天数")
    private Long nights;

    /** 房间数 */
    @Excel(name = "房间数")
    private Long rooms;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal unitPrice;

    /** 总价 */
    @Excel(name = "总价")
    private BigDecimal totalPrice;

    /** 订单状态 */
    @Excel(name = "订单状态")
    private String status;

    /** 订单创建用户ID */
    @Excel(name = "订单创建用户ID")
    private Long userId;

    //房间类型名称
    private String name;

    //房间描述
    private String description;

    //房间图片
    private String image;

    //创建人用户名
    private String userName;

}
