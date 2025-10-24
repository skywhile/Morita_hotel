import request from '@/utils/request'

// 查询退房记录列表
export function listCheckout(query) {
  return request({
    url: '/hotel/checkout/list',
    method: 'get',
    params: query
  })
}

// 查询退房记录详细
export function getCheckout(checkoutId) {
  return request({
    url: '/hotel/checkout/' + checkoutId,
    method: 'get'
  })
}

// 新增退房记录
export function addCheckout(data) {
  return request({
    url: '/hotel/checkout',
    method: 'post',
    data: data
  })
}

// 修改退房记录
export function updateCheckout(data) {
  return request({
    url: '/hotel/checkout',
    method: 'put',
    data: data
  })
}

// 删除退房记录
export function delCheckout(checkoutId) {
  return request({
    url: '/hotel/checkout/' + checkoutId,
    method: 'delete'
  })
}
