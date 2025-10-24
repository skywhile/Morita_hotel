package com.morita.hotel.domain;

import com.morita.system.general.annotation.Excel;
import com.morita.system.general.core.domain.BaseEntity;
import lombok.*;

/**
 * 轮播图对象 banner
 *
 * @author morita
 * @date 2025-09-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Banner extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 轮播图ID */
    private String bannerId;

    /** 名称 */
    @Excel(name = "名称")
    private String title;

    /** 图片 */
    @Excel(name = "图片")
    private String image;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;


}
