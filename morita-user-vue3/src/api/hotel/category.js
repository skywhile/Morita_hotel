import request from '@/utils/request'

// 查询房间分类列表
export function listCategory(query) {
  return request({
    url: '/hotel/category/list',
    method: 'get',
    params: query
  })
}

// 查询房间分类详细
export function getCategory(categoryId) {
  return request({
    url: '/hotel/category/' + categoryId,
    method: 'get'
  })
}

// 新增房间分类
export function addCategory(data) {
  return request({
    url: '/hotel/category',
    method: 'post',
    data: data
  })
}

// 修改房间分类
export function updateCategory(data) {
  return request({
    url: '/hotel/category',
    method: 'put',
    data: data
  })
}

// 删除房间分类
export function delCategory(categoryId) {
  return request({
    url: '/hotel/category/' + categoryId,
    method: 'delete'
  })
}
