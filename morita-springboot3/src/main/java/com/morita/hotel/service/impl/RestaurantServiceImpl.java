package com.morita.hotel.service.impl;

import java.util.List;

import com.morita.system.general.utils.DateUtils;
import com.morita.system.general.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import com.morita.system.general.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.morita.hotel.domain.Menu;
import com.morita.hotel.mapper.RestaurantMapper;
import com.morita.hotel.domain.Restaurant;
import com.morita.hotel.service.IRestaurantService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 餐厅Service业务层处理
 *
 * @author morita
 * @date 2025-09-19
 */
@Service
public class RestaurantServiceImpl implements IRestaurantService {
    @Autowired
    private RestaurantMapper restaurantMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询餐厅
     *
     * @param restaurantId 餐厅主键
     * @return 餐厅
     */
    @Override
    public Restaurant selectRestaurantByRestaurantId(String restaurantId) {
        return restaurantMapper.selectRestaurantByRestaurantId(restaurantId);
    }

    /**
     * 查询餐厅列表
     *
     * @param restaurant 餐厅
     * @return 餐厅
     */
    @Override
    public List<Restaurant> selectRestaurantList(Restaurant restaurant) {
        return restaurantMapper.selectRestaurantList(restaurant);
    }

    /**
     * 新增餐厅
     *
     * @param restaurant 餐厅
     * @return 结果
     */
    @Transactional
    @Override
    public int insertRestaurant(Restaurant restaurant) {
        restaurant.setRestaurantId(IdUtils.fastSimpleUUID());
        restaurant.setCreateTime(DateUtils.getNowDate());
        int rows = restaurantMapper.insertRestaurant(restaurant);
        insertMenu(restaurant);
        return rows;
    }

    /**
     * 批量新增餐厅
     *
     * @param restaurants 餐厅List
     * @return 结果
     */
    @Override
    public int batchInsertRestaurant(List<Restaurant> restaurants) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(restaurants)) {
            try {
                for (int i = 0; i < restaurants.size(); i++) {
                    int row = restaurantMapper.insertRestaurant(restaurants.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i > 0 && i % 100 == 0) || i == restaurants.size() - 1;
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
     * 修改餐厅
     *
     * @param restaurant 餐厅
     * @return 结果
     */
    @Transactional
    @Override
    public int updateRestaurant(Restaurant restaurant) {
        restaurantMapper.deleteMenuByRestaurantId(restaurant.getRestaurantId());
        insertMenu(restaurant);
        return restaurantMapper.updateRestaurant(restaurant);
    }

    /**
     * 批量删除餐厅
     *
     * @param restaurantIds 需要删除的餐厅主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRestaurantByRestaurantIds(String[] restaurantIds) {
        restaurantMapper.deleteMenuByRestaurantIds(restaurantIds);
        return restaurantMapper.deleteRestaurantByRestaurantIds(restaurantIds);
    }

    /**
     * 删除餐厅信息
     *
     * @param restaurantId 餐厅主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRestaurantByRestaurantId(String restaurantId) {
        restaurantMapper.deleteMenuByRestaurantId(restaurantId);
        return restaurantMapper.deleteRestaurantByRestaurantId(restaurantId);
    }

    /**
     * 新增菜单信息
     *
     * @param restaurant 餐厅对象
     */
    public void insertMenu(Restaurant restaurant) {
        List<Menu> menuList = restaurant.getMenuList();
        String restaurantId = restaurant.getRestaurantId();
        if (StringUtils.isNotNull(menuList)) {
            List<Menu> list = new ArrayList<Menu>();
            for (Menu menu : menuList) {
                menu.setRestaurantId(restaurantId);
                list.add(menu);
            }
            if (list.size() > 0) {
                restaurantMapper.batchMenu(list);
            }
        }
    }
}
