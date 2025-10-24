import request from '@/utils/request'

// 查询房间列表
export function listRoom(query) {
  return request({
    url: '/hotel/room/list',
    method: 'get',
    params: query
  })
}

// 查询房间详细
export function getRoom(roomId) {
  return request({
    url: '/hotel/room/' + roomId,
    method: 'get'
  })
}

// 新增房间
export function addRoom(data) {
  return request({
    url: '/hotel/room',
    method: 'post',
    data: data
  })
}

// 修改房间
export function updateRoom(data) {
  return request({
    url: '/hotel/room',
    method: 'put',
    data: data
  })
}

// 删除房间
export function delRoom(roomId) {
  return request({
    url: '/hotel/room/' + roomId,
    method: 'delete'
  })
}

//根据房间类型ID查询房间号列表
export function selectRoomNumberListByCategoryId(categoryId) {
  return request({
    url: '/hotel/room/selectRoomNumberListByCategoryId/' + categoryId,
    method: 'get'
  })
}
