package com.morita.hotel.domain;

import com.morita.system.general.annotation.Excel;
import com.morita.system.general.core.domain.BaseEntity;
import lombok.*;

/**
 * 退房记录对象 checkout
 *
 * @author morita
 * @date 2025-09-18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Checkout extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 退房ID */
    private String checkoutId;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderId;


}
