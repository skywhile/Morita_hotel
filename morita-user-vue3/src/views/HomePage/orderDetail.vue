<template>
    <!-- 订单详情页面容器 -->
    <div class="order-detail-page">
        <!-- 订单详情页面容器 -->
        <div class="page-header">
            <el-page-header @back="router.go(-1)" content="订单详情"/>
        </div>

        <!-- 订单信息 -->
        <el-card>
            <!-- 卡片头部插槽 -->
            <template #header>
                <div class="card-header">
                    <span>订单信息</span>
                    <dict-tag :options="order_status" :value="order.status"/>
                </div>
            </template>

            <el-descriptions :column="2" border>
                <el-descriptions-item label="订单号">{{ order.orderId }}</el-descriptions-item>
                <el-descriptions-item label="下单时间">{{ order.createTime }}</el-descriptions-item>
                <el-descriptions-item label="入住时间">{{ order.checkInDate }}</el-descriptions-item>
                <el-descriptions-item label="退房时间">{{ order.checkOutDate }}</el-descriptions-item>
                <el-descriptions-item label="入住天数">{{ order.nights }}晚</el-descriptions-item>
                <el-descriptions-item label="房间数量">{{ order.rooms }}天</el-descriptions-item>
            </el-descriptions>
        </el-card>

        <!-- 房间信息 -->
        <el-card>
            <!-- 卡片头部插槽 -->
            <template #header>
                <div class="card-header">
                    <span>房间信息</span>
                </div>
            </template>

            <el-row :gutter="20">
                <!-- 房间图片 -->
                <el-col :span="8">
                    <image-preview :src="order.image" class="room-image"/>
                </el-col>
                <!-- 房间详细信息 -->
                <el-col :span="16">
                    <h3>{{ order.name }}</h3>
                    <p class="room-description">{{ order.description }}</p>
                    <div class="room-price-info">
                        <div class="price-item">
                            <span>单价: </span>
                            <span>¥{{ order.unitPrice }}</span>
                        </div>
                        <div class="price-item">
                            <span>数量: </span>
                            <span>{{ order.rooms }}间</span>
                        </div>
                        <div class="price-item">
                            <span>天数: </span>
                            <span>{{ order.nights }}天</span>
                        </div>
                        <div class="price-item">
                            <span>总价: </span>
                            <span class="price">¥{{ order.totalPrice }}</span>
                        </div>
                    </div>
                </el-col>
            </el-row>
        </el-card>

    </div>
</template>

<script setup>
import {useRoute, useRouter} from "vue-router";
import {getOrder} from "@/api/hotel/order.js";

const route = useRoute()
const router = useRouter()

//当前组件实例
const {proxy} = getCurrentInstance()

//获取对应的字典数据
const {order_status} = proxy.useDict('order_status')

//订单信息
const order = ref({})

//组价挂载时执行
onMounted(() => {
    //调用AIP查询订单详细信息
    getOrder(route.params.id).then(res => {
        //将获取到的订单数据赋值给响应式对象
        order.value = res.data
    })
})
</script>

<style scoped>
/* 订单详情页面容器样式 */
.order-detail-page {
    /* 设置最大宽度，防止在大屏幕上过宽 */
    max-width: 1200px;
    /* 居中显示 */
    margin: 0 auto;
    /* 内边距 */
    padding: 20px;
}

/* 页面头部样式 */
.page-header {
    /* 底部外边距，与下方内容分隔 */
    margin-bottom: 20px;
}

/* 卡片头部样式 */
.card-header {
    /* 使用弹性布局 */
    display: flex;
    /* 在主轴（水平方向）上两端对齐 */
    justify-content: space-between;
    /* 在交叉轴（垂直方向）上居中对齐 */
    align-items: center;
}

/* 房间详情区域样式 */
.room-details {
    /* 上下内边距 */
    padding: 20px 0;
}

/* 房间图片样式 */
.room-image {
    /* 宽度100%适应容器 */
    width: 100%;
    /* 固定高度 */
    height: 200px;
    /* 保持图片比例，裁剪以适应容器 */
    object-fit: cover;
    /* 圆角边框 */
    border-radius: 8px;
}

/* 房间描述文字样式 */
.room-description {
    /* 文字颜色（中灰色） */
    color: #666;
    /* 行高 */
    line-height: 1.6;
    /* 上下外边距 */
    margin: 15px 0;
}

/* 房间价格信息区域样式 */
.room-price-info {
    /* 背景颜色（浅灰色） */
    background-color: #f5f7fa;
    /* 内边距 */
    padding: 15px;
    /* 圆角边框 */
    border-radius: 4px;
}

/* 单个价格项样式 */
.price-item {
    /* 使用弹性布局 */
    display: flex;
    /* 在主轴（水平方向）上两端对齐 */
    justify-content: space-between;
    /* 底部外边距，分隔各项 */
    margin-bottom: 10px;
}

/* 总价区域样式 */
.total-price {
    /* 使用弹性布局 */
    display: flex;
    /* 在主轴（水平方向）上两端对齐 */
    justify-content: space-between;
    /* 顶部外边距，与上方内容分隔 */
    margin-top: 15px;
    /* 顶部内边距 */
    padding-top: 15px;
    /* 顶部边框，视觉上分隔 */
    border-top: 1px solid #eee;
    /* 字体大小 */
    font-size: 16px;
    /* 字体粗细（粗体） */
    font-weight: bold;
}

/* 价格数字样式 */
.price {
    /* 价格颜色（红色） */
    color: #e64545;
    /* 字体大小略大于周围文字 */
    font-size: 18px;
}
</style>
