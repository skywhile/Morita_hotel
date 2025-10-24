package com.morita.hotel.domain;

import java.math.BigDecimal;
import com.morita.system.general.annotation.Excel;
import com.morita.system.general.core.domain.BaseEntity;
import lombok.*;
/**
 * 菜单对象 menu
 *
 * @author morita
 * @date 2025-09-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 菜单 */
    private Long menuId;

    /** 餐厅ID */
    @Excel(name = "餐厅ID")
    private String restaurantId;

    /** 菜单种类 */
    @Excel(name = "菜单种类")
    private String category;

    /** 菜品名 */
    @Excel(name = "菜品名")
    private String name;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 价格 */
    @Excel(name = "价格")
    private BigDecimal price;


}
