import request from '@/utils/request'

// 查询入住登记列表
export function listCheckin(query) {
  return request({
    url: '/hotel/checkin/list',
    method: 'get',
    params: query
  })
}

// 查询入住登记详细
export function getCheckin(checkinId) {
  return request({
    url: '/hotel/checkin/' + checkinId,
    method: 'get'
  })
}

// 新增入住登记
export function addCheckin(data) {
  return request({
    url: '/hotel/checkin',
    method: 'post',
    data: data
  })
}

// 修改入住登记
export function updateCheckin(data) {
  return request({
    url: '/hotel/checkin',
    method: 'put',
    data: data
  })
}

// 删除入住登记
export function delCheckin(checkinId) {
  return request({
    url: '/hotel/checkin/' + checkinId,
    method: 'delete'
  })
}
