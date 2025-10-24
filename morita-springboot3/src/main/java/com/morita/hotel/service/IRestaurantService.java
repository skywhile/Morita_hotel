package com.morita.hotel.service;

import java.util.List;
import com.morita.hotel.domain.Restaurant;

/**
 * 餐厅Service接口
 *
 * @author morita
 * @date 2025-09-19
 */
public interface IRestaurantService
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
     * 批量新增餐厅
     *
     * @param restaurants 餐厅List
     * @return 结果
     */
    public int batchInsertRestaurant(List<Restaurant> restaurants);

    /**
     * 修改餐厅
     *
     * @param restaurant 餐厅
     * @return 结果
     */
    public int updateRestaurant(Restaurant restaurant);

    /**
     * 批量删除餐厅
     *
     * @param restaurantIds 需要删除的餐厅主键集合
     * @return 结果
     */
    public int deleteRestaurantByRestaurantIds(String[] restaurantIds);

    /**
     * 删除餐厅信息
     *
     * @param restaurantId 餐厅主键
     * @return 结果
     */
    public int deleteRestaurantByRestaurantId(String restaurantId);
}
