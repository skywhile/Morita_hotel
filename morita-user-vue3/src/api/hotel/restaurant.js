import request from '@/utils/request'

// 查询餐厅列表
export function listRestaurant(query) {
  return request({
    url: '/hotel/restaurant/list',
    method: 'get',
    params: query
  })
}

// 查询餐厅详细
export function getRestaurant(restaurantId) {
  return request({
    url: '/hotel/restaurant/' + restaurantId,
    method: 'get'
  })
}

// 新增餐厅
export function addRestaurant(data) {
  return request({
    url: '/hotel/restaurant',
    method: 'post',
    data: data
  })
}

// 修改餐厅
export function updateRestaurant(data) {
  return request({
    url: '/hotel/restaurant',
    method: 'put',
    data: data
  })
}

// 删除餐厅
export function delRestaurant(restaurantId) {
  return request({
    url: '/hotel/restaurant/' + restaurantId,
    method: 'delete'
  })
}
