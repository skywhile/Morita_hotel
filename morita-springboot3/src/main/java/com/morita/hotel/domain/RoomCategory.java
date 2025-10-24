package com.morita.hotel.domain;

import java.math.BigDecimal;
import com.morita.system.general.annotation.Excel;
import com.morita.system.general.core.domain.BaseEntity;
import lombok.*;

/**
 * 房间分类对象 room_category
 *
 * @author morita
 * @date 2025-09-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分类ID */
    private String categoryId;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 单晚价格 */
    @Excel(name = "单晚价格")
    private BigDecimal price;

    /** 图片 */
    @Excel(name = "图片")
    private String image;


}
