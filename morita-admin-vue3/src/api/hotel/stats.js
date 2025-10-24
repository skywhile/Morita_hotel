import request from '@/utils/request'

// 获取首页统计数据 (房间总数, 入住数, 总用户数)
export function listCategory() {
    return request({
        url: '/hotel/char/stats',
        method: 'get'
    })
}

// 获取客房状态统计数据
export function getRoomStatusStat() {
    return request({
        url: '/hotel/char/roomStatus',
        method: 'get'
    })
}

// 获取房型分布数据
export function getRoomType() {
    return request({
        url: '/hotel/char/roomType',
        method: 'get'
    })
}
