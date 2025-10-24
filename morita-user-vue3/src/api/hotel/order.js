import request from '@/utils/request'

// 查询订单列表
export function listOrder(query) {
  return request({
    url: '/hotel/order/list',
    method: 'get',
    params: query
  })
}

// 查询订单详细
export function getOrder(orderId) {
  return request({
    url: '/hotel/order/' + orderId,
    method: 'get'
  })
}

// 新增订单
export function addOrder(data) {
  return request({
    url: '/hotel/order',
    method: 'post',
    data: data
  })
}

// 修改订单
export function updateOrder(data) {
  return request({
    url: '/hotel/order',
    method: 'put',
    data: data
  })
}

// 删除订单
export function delOrder(orderId) {
  return request({
    url: '/hotel/order/' + orderId,
    method: 'delete'
  })
}

//充值
export function recharge(amount) {
  return request({
    url: '/hotel/order/recharge/' + amount,
    method: 'put',
  })
}

//取消订单
export function cancelOrder(orderId) {
  return request({
    url: '/hotel/order/cancel/' + orderId,
    method: 'put',
  })
}
