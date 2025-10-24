<template>
  <!-- 页面容器 -->
    <div class="room-category-page">
        <!-- 页面标题区域 -->
        <div class="page-header">
            <!-- 主标题 -->
            <h1 class="page-header">客房精选</h1>
            <!-- 副标题-->
            <p class="page-subtitle">为您精心挑选最舒适的客房体验</p>
        </div>

        <!-- 房间列表区域 -->
        <div class="room-list">
            <el-row :gutter="24">
                <el-col :span="8" v-for="room in roomCategoryList" :key="room.categoryId">
                    <!-- 卡片包装器, 用于添加悬停效果 -->
                    <div class="room-card-wrapper">
                        <el-card class="room-card" @click="toRoomDetail(room)">
                            <!-- 房间图片容器 -->
                            <div class="room-image-container">
                                <img :src="baseUrl + room.image" alt="room.name" class="room-image">
                                <!-- 价格标签 -->
                                <div class="room-price-tag">
                                    ¥{{ room.price }}<span class="price-unit">/晚</span>
                                </div>
                            </div>

                            <!-- 房间信息区域 -->
                            <div class="room-info">
                                <!-- 房间名称 -->
                                <h3 class="room-name">{{ room.name }}</h3>
                                <!-- 房间描述 -->
                                <p class="room-description">{{ room.description }}</p>
                                <div class="room-meta">
                                    <div class="room-actions">
                                        <!-- 详情按钮 -->
                                        <el-button type="primary" class="detail-button"
                                                   @click.stop="toRoomDetail(room)">
                                            立即预订
                                        </el-button>
                                    </div>
                                </div>
                            </div>
                        </el-card>
                    </div>
                </el-col>
            </el-row>

            <!-- 分页组件容器 -->
            <div class="pagination-container">
                <pagination
                        v-show="total>0"
                        :total="total"
                        v-model:page="queryParams.pageNum"
                        v-model:limit="queryParams.pageSize"
                        @pagination="getRoomCategoryList"
                        :pageSizes="[6, 12, 24]"
                />
            </div>
        </div>
    </div>
</template>

<script setup>
import {ref} from 'vue'
import {listCategory} from "@/api/hotel/category.js";
import {useRouter} from "vue-router";

//获取基础URL, 用于拼接图片路径
const baseUrl = import.meta.env.VITE_APP_BASE_API;

//房间分类列表
const roomCategoryList = ref([])

//列表数据总条数
const total = ref(0)

//路由实例
const router = useRouter()

//查询参数
const queryParams = ref({
    pageNum: 1,
    pageSize: 6,
    name: null,
})

//打开房间详情页面, 并将点击的房间类型信息传递过去
const toRoomDetail = (room) => {
    //跳转到房间详情页
    router.push(`/index/room/${room.categoryId}`)
}

//查询数据
const getRoomCategoryList = () => {
    listCategory(queryParams.value).then(res => {
        total.value = res.total
        roomCategoryList.value = res.rows
    })
}


onMounted(() => {
    getRoomCategoryList()
})
</script>

<style scoped>
/*
  页面容器样式
  使用BEM(Block Element Modifier)命名规范
*/
.room-category-page {
    /* 设置最大宽度，增加大屏幕适应性 */
    max-width: 1400px;
    /* 水平居中 */
    margin: 0 auto;
    /* 增加内边距 */
    padding: 30px;
    /* 渐变背景，从左上到右下的渐变 */
    background: linear-gradient(135deg, #f5f7fa 0%, #e4edf9 100%);
    /* 最小高度为整个视口高度 */
    min-height: 100vh;
}

/* 页面标题区域样式 */
.page-header {
    /* 文本居中 */
    text-align: center;
    /* 底部外边距 */
    margin-bottom: 10px;
    /* 上下内边距 */
    padding: 5px 0;
}

/* 主标题样式 */
.page-title {
    /* 字体大小 */
    font-size: 36px;
    /* 字体粗细 */
    font-weight: 700;
    /* 字体颜色 */
    color: #182848;
    /* 外边距：上下0，左右0，底部10px */
    margin: 0 0 10px 0;
    /* 字母间距 */
    letter-spacing: 1px;
    /* 文字阴影：水平偏移0，垂直偏移2px，模糊4px，颜色带透明度 */
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 副标题样式 */
.page-subtitle {
    /* 字体大小 */
    font-size: 18px;
    /* 字体颜色 */
    color: #4b6cb7;
    /* 外边距清零 */
    margin: 0;
    /* 字体粗细 */
    font-weight: 300;
}

/* 房间列表区域样式 */
.room-list {
    /* 顶部外边距 */
    margin-top: 20px;
}

/* 房间卡片包装器样式 */
.room-card-wrapper {
    /* 底部外边距 */
    margin-bottom: 30px;
    /* 过渡效果：所有属性，0.3秒，缓动函数 */
    transition: all 0.3s ease;
}

/* 房间卡片悬停效果 */
.room-card-wrapper:hover {
    /* 向上移动10px */
    transform: translateY(-10px);
}

/* 房间卡片样式 */
.room-card {
    /* 圆角边框 */
    border-radius: 15px;
    /* 隐藏溢出内容 */
    overflow: hidden;
    /* 盒子阴影：水平偏移0，垂直偏移5px，模糊20px，颜色带透明度 */
    box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
    /* 过渡效果：所有属性，0.4秒，自定义贝塞尔曲线 */
    transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.1);
    /* 无边框 */
    border: none;
    /* 高度100%填充父容器 */
    height: 100%;
    /* 使用Flex布局 */
    display: flex;
    /* 垂直方向排列 */
    flex-direction: column;
}

/* 房间卡片悬停效果 */
.room-card:hover {
    /* 增强阴影效果 */
    box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15);
    /* 向上移动5px */
    transform: translateY(-5px);
}

/* 房间图片容器样式 */
.room-image-container {
    /* 相对定位，为子元素的绝对定位提供参考 */
    position: relative;
    /* 隐藏溢出内容 */
    overflow: hidden;
}

/* 房间图片样式 */
.room-image {
    /* 宽度100%填充容器 */
    width: 100%;
    /* 固定高度 */
    height: 250px;
    /* 保持图片比例，覆盖整个容器 */
    object-fit: cover;
    /* 变换效果的过渡 */
    transition: transform 0.5s ease;
}

/* 卡片悬停时图片放大效果 */
.room-card:hover .room-image {
    /* 缩放图片到105% */
    transform: scale(1.05);
}

/* 房间价格标签样式 */
.room-price-tag {
    /* 绝对定位 */
    position: absolute;
    /* 距离顶部15px */
    top: 15px;
    /* 距离右侧15px */
    right: 15px;
    /* 渐变背景：从左上到右下，蓝色渐变 */
    background: linear-gradient(135deg, #4b6cb7 0%, #182848 100%);
    /* 文字颜色 */
    color: white;
    /* 内边距：上下8px，左右15px */
    padding: 8px 15px;
    /* 圆角边框 */
    border-radius: 20px;
    /* 字体大小 */
    font-size: 20px;
    /* 字体粗细 */
    font-weight: 700;
    /* 阴影效果 */
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 价格单位样式 */
.price-unit {
    /* 较小的字体大小 */
    font-size: 14px;
    /* 正常字体粗细 */
    font-weight: 400;
}

/* 房间信息区域样式 */
.room-info {
    /* 内边距 */
    padding: 25px;
    /* 允许元素在Flex容器中增长 */
    flex-grow: 1;
    /* 使用Flex布局 */
    display: flex;
    /* 垂直方向排列 */
    flex-direction: column;
}

/* 房间名称样式 */
.room-name {
    /* 外边距：上下0，左右0，底部15px */
    margin: 0 0 15px 0;
    /* 字体颜色 */
    color: #182848;
    /* 字体大小 */
    font-size: 22px;
    /* 字体粗细 */
    font-weight: 700;
    /* 字母间距 */
    letter-spacing: 0.5px;
}

/* 房间描述样式 */
.room-description {
    /* 字体颜色 */
    color: #666;
    /* 底部外边距 */
    margin-bottom: 20px;
    /* 允许元素在Flex容器中增长 */
    flex-grow: 1;
    /* 行高 */
    line-height: 1.6;
    /* 字体大小 */
    font-size: 15px;
}

/* 房间元数据区域样式 */
.room-meta {
    /* 顶部边框 */
    border-top: 1px solid #eee;
    /* 顶部内边距 */
    padding-top: 20px;
}

/* 房间操作按钮区域样式 */
.room-actions {
    /* 使用Flex布局 */
    display: flex;
    /* 子元素之间的间隙 */
    gap: 10px;
}

/* 详情按钮样式 */
.detail-button {
    /* 弹性增长因子为1，占据可用空间 */
    flex: 1;
    /* 渐变背景：从左上到右下，蓝色渐变 */
    background: linear-gradient(135deg, #4b6cb7 0%, #182848 100%);
    /* 无边框 */
    border: none;
    /* 字体粗细 */
    font-weight: 600;
    /* 内边距：上下12px，左右20px */
    padding: 12px 20px;
    /* 过渡效果 */
    transition: all 0.3s ease;
}

/* 按钮悬停效果 */
.detail-button:hover {
    /* 向上移动2px */
    transform: translateY(-2px);
    /* 添加阴影效果 */
    box-shadow: 0 5px 15px rgba(75, 108, 183, 0.4);
}

/* 分页组件容器样式 */
.pagination-container {
    /* 使用Flex布局 */
    display: flex;
    /* 水平居中 */
    justify-content: center;
    /* 顶部外边距 */
    margin-top: 20px;
}
</style>
