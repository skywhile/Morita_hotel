<template>
  <!-- 页面容器 -->
    <div class="orderList-page">
        <el-tabs v-model="activeTab" @tab-change="handleTableChange">
            <el-tab-pane label="全部订单" name="全部订单"/>
            <el-tab-pane label="待入住" name="待入住"/>
            <el-tab-pane label="已入住" name="已入住"/>
            <el-tab-pane label="已完成" name="已完成"/>
            <el-tab-pane label="已取消" name="已取消"/>
        </el-tabs>

        <!-- 订单列表区域 -->
        <div>
            <el-card v-for="order in orderList" :key="order.orderId" class="order-card">
                <!-- 订单头部信息 -->
                <div class="order-header">
                    <!-- 订单号 -->
                    <div class="order-number">订单号: {{ order.orderId }}</div>
                    <!-- 订单状态 -->
                    <div class="order-status" :class="order.status">
                        <dict-tag :options="order_status" :value="order.status"/>
                    </div>
                </div>
                <!-- 订单内容信息 -->
                <div class="order-content">
                    <!-- 房间信息 -->
                    <div class="room-info">
                        <!-- 房间类型图片 -->
                        <image-preview :src="order.image" class="room-image"/>
                        <!-- 房间详情 -->
                        <div class="room-details">
                            <h3>{{ order.name }}</h3>
                            <p>{{ order.checkInDate }} 至 {{ order.checkOutDate }}</p>
                            <p>{{ order.nights }}晚 | {{ order.rooms }}间房</p>
                        </div>
                    </div>
                    <!-- 订单价格 -->
                    <div class="order-price">
                        <div class="price-label">总价</div>
                        <div class="price">¥{{ order.totalPrice }}</div>
                    </div>
                </div>
                <!-- 订单底部操作区域 -->
                <div class="order-footer">
                    <el-button v-if="order.status === '待入住'"
                               type="primary"
                               @click="cancelToOrder(order.orderId)">
                        取消订单
                    </el-button>
                    <el-button @click="toDetail(order.orderId)">查看详情</el-button>
                </div>
            </el-card>
        </div>

        <!-- 分页组件 -->
        <div class="pagination-container">
            <pagination
                    v-show="total>0"
                    :total="total"
                    v-model:page="query.pageNum"
                    v-model:limit="query.pageSize"
                    @pagination="selectOrderList"
            />
        </div>

        <!-- 空状态提示 -->
        <el-empty v-if="orderList.length === 0" description="暂无订单"/>

    </div>
</template>

<script setup>
import {ref} from 'vue'
import useUserStore from "@/store/modules/user.js";
import {cancelOrder, listOrder} from "@/api/hotel/order.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {useRouter} from "vue-router";

//基础API路径
const baseUrl = import.meta.env.VITE_APP_BASE_API

//当前组件实例
const {proxy} = getCurrentInstance()

//获取对应的字典数据
const {order_status} = proxy.useDict('order_status')

//当前激活的标签页, 默认为全部订单
const activeTab = ref('全部订单')

//获取当前用户的信息
const loginUser = useUserStore()

//查询参数
const query = ref({
    pageNum: 1,
    pageSize: 10,
    categoryId: null,
    status: null,
    userId: loginUser.id,
    userName: null
})

//订单列表数据
const orderList = ref([])

//数据总数
const total = ref(0)

//路由实例
const router = useRouter()

//打开详情页面
const toDetail = (orderId) => {
    //跳转到订单详情页面
    router.push(`/index/order/${orderId}`)
}

//取消订单
const cancelToOrder = (orderId) => {
    ElMessageBox.confirm(
        '确定要取消订单吗?',
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '算了, 不取消',
            type: 'warning',
        }
    )
        .then(() => {
            cancelOrder(orderId).then(res => {
                selectOrderList()
                ElMessage.success("订单取消成功, 已退款至账户余额")
            })
        })
        .catch(() => {
        })
}

//处理标签页切换
const handleTableChange = (tab) => {
    if (tab === '全部订单') {
        query.value.status = null
    } else {
        query.value.status = tab
    }
    //重新查询订单列表
    selectOrderList()
}

//查询订单列表数据
const selectOrderList = () => {
    //调用API查询数据
    listOrder(query.value).then(res => {
        total.value = res.total
        orderList.value = res.rows
    })
}

//组件挂载时执行
onMounted(() => {
    //获取订单列表
    selectOrderList()
})
</script>

<style scoped>
/* 页面容器样式 */
.orderList-page {
    max-width: 1200px; /* 最大宽度 */
    margin: 0 auto; /* 水平居中 */
    padding: 20px; /* 内边距 */
}

/* 订单卡片样式 */
.order-card {
    margin-bottom: 20px; /* 底部外边距 */
}

/* 订单头部样式 */
.order-header {
    display: flex; /* 使用Flex布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中 */
    padding-bottom: 15px; /* 底部内边距 */
    border-bottom: 1px solid #eee; /* 底部边框 */
}

/* 订单编号样式 */
.order-number {
    font-weight: bold; /* 字体粗细 */
}

/* 订单状态样式 */
.order-status {
    padding: 4px 8px; /* 内边距 */
    border-radius: 4px; /* 圆角边框 */
    color: #fff; /* 字体颜色 */
    font-size: 12px; /* 字体大小 */
}

/* 订单内容区域样式 */
.order-content {
    display: flex; /* 使用Flex布局 */
    justify-content: space-between; /* 两端对齐 */
    padding: 20px 0; /* 上下内边距 */
}

/* 房间信息区域样式 */
.room-info {
    display: flex; /* 使用Flex布局 */
    align-items: center; /* 垂直居中 */
}

/* 房间图片样式 */
.room-image {
    width: 120px; /* 宽度 */
    height: 80px; /* 高度 */
    object-fit: cover; /* 保持比例覆盖容器 */
    margin-right: 15px; /* 右侧外边距 */
}

/* 房间详情区域样式 */
.room-details h3 {
    margin: 0 0 10px 0; /* 外边距：上下0，左右0，底部10px */
    color: #333; /* 字体颜色 */
}

.room-details p {
    margin: 5px 0; /* 上下外边距5px，左右0 */
    color: #666; /* 字体颜色 */
}

/* 订单价格区域样式 */
.order-price {
    text-align: right; /* 文本右对齐 */
}

/* 价格标签样式 */
.price-label {
    color: #666; /* 字体颜色 */
    margin-bottom: 5px; /* 底部外边距 */
}

/* 价格样式 */
.price {
    font-size: 20px; /* 字体大小 */
    font-weight: bold; /* 字体粗细 */
    color: #e64545; /* 字体颜色（红色系） */
}

/* 订单底部区域样式 */
.order-footer {
    padding-top: 15px; /* 顶部内边距 */
    border-top: 1px solid #eee; /* 顶部边框 */
    text-align: right; /* 文本右对齐 */
}

/* 分页组件容器样式 */
.pagination-container {
    display: flex; /* 使用Flex布局 */
    justify-content: center; /* 水平居中 */
    margin-top: 20px; /* 顶部外边距 */
}
</style>
