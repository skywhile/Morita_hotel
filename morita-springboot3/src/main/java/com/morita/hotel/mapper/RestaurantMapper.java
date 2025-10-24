package com.morita.hotel.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.morita.hotel.domain.Restaurant;
import com.morita.hotel.domain.Menu;

/**
 * 餐厅Mapper接口
 *
 * @author morita
 * @date 2025-09-19
 */
@Mapper
public interface RestaurantMapper
{
    /**
     * 查询餐厅
     *
     * @param restaurantId 餐厅主键
     * @return 餐厅
     */
    public Restaurant selectRestaurantByRestaurantId(String restaurantId);

    /**
     * 查询餐厅列表
     *
     * @param restaurant 餐厅
     * @return 餐厅集合
     */
    public List<Restaurant> selectRestaurantList(Restaurant restaurant);

    /**
     * 新增餐厅
     *
     * @param restaurant 餐厅
     * @return 结果
     */
    public int insertRestaurant(Restaurant restaurant);

    /**
     * 修改餐厅
     *
     * @param restaurant 餐厅
     * @return 结果
     */
    public int updateRestaurant(Restaurant restaurant);

    /**
     * 删除餐厅
     *
     * @param restaurantId 餐厅主键
     * @return 结果
     */
    public int deleteRestaurantByRestaurantId(String restaurantId);

    /**
     * 批量删除餐厅
     *
     * @param restaurantIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRestaurantByRestaurantIds(String[] restaurantIds);

    /**
     * 批量删除菜单
     *
     * @param restaurantIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMenuByRestaurantIds(String[] restaurantIds);

    /**
     * 批量新增菜单
     *
     * @param menuList 菜单列表
     * @return 结果
     */
    public int batchMenu(List<Menu> menuList);


    /**
     * 通过餐厅主键删除菜单信息
     *
     * @param restaurantId 餐厅ID
     * @return 结果
     */
    public int deleteMenuByRestaurantId(String restaurantId);
}
