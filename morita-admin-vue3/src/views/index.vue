<template>
    <div class="dashboard">
        <!-- 欢迎区域 -->
        <div class="welcome-section">
            <div class="welcome-content">
                <h1 class="welcome-title">欢迎使用 酒店后台管理端</h1>
                <p class="welcome-subtitle">一站式酒店管理系统解决方案</p>
            </div>
        </div>

        <!-- 数据统计区域 -->
        <div class="stats-grid">
            <el-row :gutter="20">
                <el-col :span="8" v-for="stat in statsData" :key="stat.title">
                    <el-card class="stat-card" shadow="hover">
                        <div class="stat-content">
                            <!-- 图标区域 -->
                            <div class="stat-icon" :style="{ backgroundColor: stat.color + '20', color: stat.color }">
                                <component :is="stat.icon"/>
                            </div>
                            <!-- 统计信息区域 -->
                            <div class="stat-info">
                                <div class="stat-value">{{ stat.value }}</div>
                                <div class="stat-title">{{ stat.title }}</div>
                            </div>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
        </div>

        <!-- 图表区域 -->
        <div class="charts-grid">
            <el-row :gutter="20">
                <el-col :span="16">
                    <el-card class="chart-card" shadow="hover">
                        <template #header>
                            <div class="card-header">
                                <span>客房状态统计</span>
                            </div>
                            <!-- 图表容器 -->
                            <div ref="roomStatusChartRef" class="chart-container"></div>
                        </template>
                    </el-card>
                </el-col>
                <el-col :span="8">
                    <el-card class="chart-card" shadow="hover">
                        <template #header>
                            <div class="card-header">
                                <span>房型分布</span>
                            </div>
                            <!-- 图表容器 -->
                            <div ref="roomTypeChartRef" class="chart-container"></div>
                        </template>
                    </el-card>
                </el-col>
            </el-row>
        </div>

    </div>
</template>

<script setup>
import {getRoomStatusStat, getRoomType, listCategory} from "@/api/hotel/stats.js";
import * as echarts from 'echarts'


//定义统计数据响应式变量
const statsData = ref([
    {
        title: '总房间数',
        value: '0',
        icon: 'House',
        color: '#409EFF'
    },
    {
        title: '入住数',
        value: '0',
        icon: 'OfficeBuilding',
        color: '#40ff43'
    },
    {
        title: '总用户数',
        value: '0',
        icon: 'House',
        color: '#f7fd51'
    },
])

//创建图表DOM元素的引用
const roomStatusChartRef = ref()
const roomTypeChartRef = ref()

//获取首页统计数据的方法
const fetchDashboardStatus = () => {
    //调用API获取数据
    listCategory().then(res => {

        //更新统计数据到响应式变量
        statsData.value[0].value = res.data.totalRooms
        statsData.value[1].value = res.data.occupiedRooms
        statsData.value[2].value = res.data.totalUsers
    })
}

//获取客房状态统计数据并且更新图表
const fetchRoomStatusStats = () => {
    getRoomStatusStat().then(res => {
        const data = res.data

        //如果图表已经初始化, 就更新图表数据
        if (roomStatusChart) {
            const occupancyOption = {
                tooltip: {  // 提示框配置
                    trigger: 'axis'  // 触发方式为坐标轴触发
                },
                xAxis: {
                    type: 'category',  // 类目轴
                    data: ['空闲', '已入住']  // X轴数据
                },
                yAxis: {
                    type: 'value'  // 数值轴
                },
                series: [{  // 数据系列配置
                    name: '房间数量',
                    type: 'bar',  // 柱状图
                    data: [data.available || 0, data.occupied || 0],  // 数据值
                    itemStyle: {  // 柱条样式
                        // 渐变颜色
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,  // 0%处的颜色
                            color: '#83bff6'
                        }, {
                            offset: 0.5,  // 50%处的颜色
                            color: '#188df0'
                        }, {
                            offset: 1,  // 100%处的颜色
                            color: '#188df0'
                        }])
                    },
                    emphasis: {  // 高亮状态下的样式
                        itemStyle: {
                            color: '#188df0'
                        }
                    }
                }]
            }
            // 设置图表选项，true表示不合并之前选项
            roomStatusChart.setOption(occupancyOption, true)
        }
    })
}

//刷新所有数据
const refreshData = () => {
    fetchDashboardStatus()
    fetchRoomStatusStats()
    fetchRoomTypeStats()
}

//获取房型分布数据并更新图表
const fetchRoomTypeStats = () => {
    getRoomType().then(res => {
        const data = res.data

        //处理API返回的房型分布数据
        const charData = []
        for (const key in data) {
            charData.push({ value: data[key], name: key })
        }
        console.log(charData, '看看有没有拿到需要的数据')

        //更新图表数据
        const roomTypeOption = {
            tooltip: {
                trigger: 'item'
            },
            legend: {
                bottom: '0',
                left: 'center'
            },
            series: [{
                name: '房型分布',
                type: 'pie',
                radius: ['40%', '70%'],
                avoidLabelOverlap: false,
                itemStyle: {
                    borderRadius: 10,
                    borderColor: '#fff',
                    borderWidth: 2
                },
                label: {
                    show: false,
                    position: 'center'
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: '18',
                        fontWeight: 'bold'
                    }
                },
                labelLine: {
                    show: false
                },
                data: charData
            }]
        }
        roomTypeChart.setOption(roomTypeOption, true)
    })
}

//初始化图表实例变量
let roomStatusChart = null
let roomTypeChart = null

const initCarts = () => {
    //初始化客房状态统计图表
    roomStatusChart = echarts.init(roomStatusChartRef.value)
    const occupancyOption = {
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'category',
            data: ['空闲', '已入住']
        },
        yAxis: {
            type: 'value'
        },
        series: [{
            name: '房间数量',
            type: 'bar',
            data: [0, 0],  // 初始数据
            itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                    offset: 0,
                    color: '#83bff6'
                }, {
                    offset: 0.5,
                    color: '#188df0'
                }, {
                    offset: 1,
                    color: '#188df0'
                }])
            },
            emphasis: {
                itemStyle: {
                    color: '#188df0'
                }
            }
        }]
    }
    roomStatusChart.setOption(occupancyOption)

    // 初始化房型分布图表
    roomTypeChart = echarts.init(roomTypeChartRef.value)
    const roomTypeOption = {
        tooltip: {
            trigger: 'item'
        },
        legend: {
            bottom: '0',
            left: 'center'
        },
        series: [{
            name: '房型分布',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
            },
            label: {
                show: false,
                position: 'center'
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: '18',
                    fontWeight: 'bold'
                }
            },
            labelLine: {
                show: false
            },
            data: []  // 初始为空数据
        }]
    }
    roomTypeChart.setOption(roomTypeOption)
}

//页面加载时执行
onMounted(() => {
    initCarts() //初始化图表
    refreshData() //获取数据
})
</script>

<style scoped lang="scss">
/* 仪表盘主容器 */
.dashboard {
  padding: 20px; /* 内边距 */
  background-color: #f5f7fa; /* 背景色(浅灰色) */
  min-height: calc(100vh - 84px); /* 最小高度，确保铺满视口 */
}

/* 欢迎区域样式 */
.welcome-section {
  display: flex; /* 弹性布局 */
  align-items: center; /* 垂直居中 */
  justify-content: space-between; /* 两端对齐 */
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); /* 渐变背景 */
  border-radius: 12px; /* 圆角 */
  padding: 60px 40px; /* 内边距 */
  margin-bottom: 30px; /* 底部外边距 */
  color: #fff; /* 文字颜色 */

  .welcome-content {
    flex: 1; /* 占据剩余空间 */

    .welcome-title {
      font-size: 36px; /* 字体大小 */
      font-weight: bold; /* 字体粗细 */
      margin-bottom: 20px; /* 底部外边距 */
    }

    .welcome-subtitle {
      font-size: 18px; /* 字体大小 */
      opacity: 0.9; /* 透明度 */
    }
  }
}

/* 统计卡片网格布局 */
.stats-grid {
  margin-bottom: 30px; /* 底部外边距 */

  .stat-card {
    border-radius: 12px; /* 圆角 */
    border: none; /* 无边框 */
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); /* 阴影效果 */

    .stat-content {
      display: flex; /* 弹性布局 */
      align-items: center; /* 垂直居中 */

      .stat-icon {
        width: 50px; /* 宽度 */
        height: 50px; /* 高度 */
        border-radius: 12px; /* 圆角 */
        display: flex; /* 弹性布局 */
        align-items: center; /* 垂直居中 */
        justify-content: center; /* 水平居中 */
        margin-right: 15px; /* 右侧外边距 */
        font-size: 24px; /* 图标大小 */
      }

      .stat-info {
        flex: 1; /* 占据剩余空间 */

        .stat-value {
          font-size: 24px; /* 字体大小 */
          font-weight: bold; /* 字体粗细 */
          margin-bottom: 5px; /* 底部外边距 */
        }

        .stat-title {
          font-size: 14px; /* 字体大小 */
          color: #909399; /* 文字颜色(灰色) */
        }
      }
    }
  }
}

/* 图表网格布局 */
.charts-grid {
  .chart-card {
    border-radius: 12px; /* 圆角 */
    border: none; /* 无边框 */
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); /* 阴影效果 */

    .card-header {
      font-weight: bold; /* 字体粗细 */
      font-size: 16px; /* 字体大小 */
      color: #303133; /* 文字颜色(深灰色) */
    }

    .chart-container {
      height: 300px; /* 固定高度 */
      width: 100%; /* 宽度100% */
    }
  }
}
</style>
