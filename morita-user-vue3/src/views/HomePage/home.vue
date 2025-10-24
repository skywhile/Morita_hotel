<template>
    <div class="home-page">
        <!-- 轮播图区域 -->
        <div class="carousel-section">
            <el-carousel height="400px" indicator-position="outside">
                <el-carousel-item v-for="item in bannerList" :key="item.bannerId">
                    <!-- 轮播图片 -->
                    <img :src="baseUrl + item.image" alt="" class="carousel-image">
                    <!-- 轮播图文字标题 -->
                    <div class="carousel-text">{{ item.title }}</div>
                </el-carousel-item>
            </el-carousel>
        </div>

        <!-- 酒店介绍区域 -->
        <div class="section">
            <h2 class="section-title">酒店介绍</h2>
            <div class="intro-content">
                <!-- 介绍文本 -->
                <p>欢迎来到我们的迎宾酒店，这里为您提供最舒适的住宿体验。我们拥有各类房型，配备完善的设施，致力于为每一位客人提供贴心的服务。</p>
                <p>酒店地理位置优越，交通便利，周边环境优美，是您商务出行和休闲度假的理想选择。</p>
            </div>
        </div>

        <!-- 房型浏览区域 -->
        <div class="section">
            <!-- 头部 -->
            <div class="section-header">
                <h2 class="section-title">热门房型</h2>
                <!-- 查看全部按钮 -->
                <el-button type="primary" @click="toAllRooms">查看所有房型</el-button>
            </div>
            <!-- 房型列表 -->
            <div class="room-list">
                <el-row :gutter="30">
                    <el-col :span="8" v-for="room in roomTypeList" :key="room.roomId">
                        <!-- 房型卡片 -->
                        <el-card class="room-card" @click="">
                            <!-- 图片容器 -->
                            <div class="room-image-container">
                                <img :src="baseUrl + room.image" alt="" class="room-image">
                                <div class="room-price-tag">¥{{ room.price }}/晚</div>
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
                    </el-col>
                </el-row>
            </div>
        </div>

    </div>
</template>

<script setup>
import {listBanner} from "@/api/hotel/banner.js";
import {useRouter} from "vue-router";
import {listCategory} from "@/api/hotel/category.js";

//基础API路径
const baseUrl = import.meta.env.VITE_APP_BASE_API

const router = useRouter()

//轮播图查询参数
const bannerQuery = ref({
    pageNum: 1,
    pageSize: 10
})

//房型查询参数
const roomQuery = ref({
    pageNum: 1,
    pageSize: 3
})

//房型列表数据
const roomTypeList = ref([])

//轮播图列表数据
const bannerList = ref([])

//获取轮播图列表数据和房间分类列表
const getList = () => {
    listBanner(bannerQuery.value).then(res => {
        bannerList.value = res.rows
    })

    listCategory(roomQuery.value).then(res => {
        roomTypeList.value = res.rows
        console.log(roomTypeList.value)
    })
}

//打开房间详情页面, 并将点击的房间类型信息传递过去
const toRoomDetail = (room) => {
    //跳转到房间详情页
    router.push(`/index/room/${room.categoryId}`)
}

//跳转到所有房型页面
const toAllRooms = () => {
    router.push('/index/rooms')
}

//组件挂载时执行
onMounted(() => {
    getList()
})
</script>

<style scoped>
/* 首页容器样式 */
.home-page {
    max-width: 1200px; /* 最大宽度限制 */
    margin: 0 auto; /* 水平居中 */
    padding: 20px; /* 内边距 */
}

/* 轮播图区域样式 */
.carousel-section {
    margin-bottom: 30px; /* 底部外边距 */
}

/* 轮播图片样式 */
.carousel-image {
    width: 100%; /* 宽度100%填充容器 */
    height: 100%; /* 高度100%填充容器 */
    object-fit: cover; /* 保持比例填充容器 */
}

/* 轮播文字样式 */
.carousel-text {
    position: absolute; /* 绝对定位 */
    bottom: 20px; /* 距离底部20px */
    left: 40px; /* 距离左侧40px */
    text-align: left; /* 文本左对齐 */
    color: white; /* 文字颜色白色 */
    font-size: 40px; /* 字体大小 */
    font-weight: bold; /* 字体加粗 */
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.8); /* 文字阴影效果 */
    padding: 10px; /* 内边距 */
}

/* 区域通用样式 */
.section {
    margin-bottom: 40px; /* 底部外边距 */
}

/* 区域头部样式 */
.section-header {
    display: flex; /* 弹性布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中 */
    margin-bottom: 20px; /* 底部外边距 */
}

/* 区域标题样式 */
.section-title {
    font-size: 24px; /* 字体大小 */
    font-weight: bold; /* 字体加粗 */
    color: #333; /* 字体颜色 */
    margin: 0; /* 外边距清零 */
}

/* 介绍内容样式 */
.intro-content {
    line-height: 1.8; /* 行高 */
    color: #666; /* 字体颜色 */
    font-size: 16px; /* 字体大小 */
}

/* 房间列表样式 */
.room-list {
    display: block; /* 块级显示 */
    margin-top: 20px; /* 顶部外边距 */
}

/* 房间卡片包装器样式 */
.room-card-wrapper {
    margin-bottom: 30px; /* 底部外边距 */
    transition: all 0.3s ease; /* 过渡动画效果 */
}

/* 房间卡片包装器悬停效果 */
.room-card-wrapper:hover {
    transform: translateY(-10px); /* 上移10px */
}

/* 房间卡片样式 */
.room-card {
    border-radius: 15px; /* 圆角边框 */
    overflow: hidden; /* 溢出隐藏 */
    box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1); /* 阴影效果 */
    transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.1); /* 过渡动画 */
    border: none; /* 无边框 */
    height: 100%; /* 高度100% */
    display: flex; /* 弹性布局 */
    flex-direction: column; /* 垂直方向排列 */
    cursor: pointer; /* 鼠标指针样式 */
}

/* 房间卡片悬停效果 */
.room-card:hover {
    box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15); /* 增强阴影效果 */
    transform: translateY(-5px); /* 上移5px */
}

/* 房间图片容器样式 */
.room-image-container {
    position: relative; /* 相对定位 */
    overflow: hidden; /* 溢出隐藏 */
}

/* 房间图片样式 */
.room-image {
    width: 100%; /* 宽度100%填充容器 */
    height: 250px; /* 固定高度 */
    object-fit: cover; /* 保持比例填充容器 */
    transition: transform 0.5s ease; /* 变换过渡效果 */
}

/* 房间卡片悬停时图片放大效果 */
.room-card:hover .room-image {
    transform: scale(1.05); /* 放大5% */
}

/* 房间价格标签样式 */
.room-price-tag {
    position: absolute; /* 绝对定位 */
    top: 15px; /* 距离顶部15px */
    right: 15px; /* 距离右侧15px */
    background: linear-gradient(135deg, #4b6cb7 0%, #182848 100%); /* 渐变背景 */
    color: white; /* 文字颜色白色 */
    padding: 8px 15px; /* 内边距 */
    border-radius: 20px; /* 圆角边框 */
    font-size: 20px; /* 字体大小 */
    font-weight: 700; /* 字体加粗 */
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15); /* 阴影效果 */
}

/* 价格单位样式 */
.price-unit {
    font-size: 14px; /* 字体大小 */
    font-weight: 400; /* 字体粗细 */
}

/* 房间信息区域样式 */
.room-info {
    padding: 25px; /* 内边距 */
    flex-grow: 1; /* 弹性增长因子 */
    display: flex; /* 弹性布局 */
    flex-direction: column; /* 垂直方向排列 */
}

/* 房间名称样式 */
.room-name {
    margin: 0 0 15px 0; /* 外边距 */
    color: #182848; /* 字体颜色 */
    font-size: 22px; /* 字体大小 */
    font-weight: 700; /* 字体加粗 */
    letter-spacing: 0.5px; /* 字母间距 */
    min-height: 10px; /* 最小高度 */
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
}

/* 房间描述样式 */
.room-description {
    color: #666; /* 字体颜色 */
    margin-bottom: 20px; /* 底部外边距 */
    flex-grow: 1; /* 弹性增长因子 */
    line-height: 1.6; /* 行高 */
    font-size: 15px; /* 字体大小 */
    min-height: 50px; /* 最小高度 */
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
}

/* 房间元数据区域样式 */
.room-meta {
    border-top: 1px solid #eee; /* 顶部边框 */
    padding-top: 20px; /* 顶部内边距 */
    margin-top: auto; /* 顶部自动外边距，将元素推到底部 */
}

/* 房间操作按钮区域样式 */
.room-actions {
    display: flex; /* 弹性布局 */
    gap: 10px; /* 元素间距 */
}

/* 详情按钮样式 */
.detail-button {
    flex: 1; /* 弹性因子，占据剩余空间 */
    background: linear-gradient(135deg, #4b6cb7 0%, #182848 100%); /* 渐变背景 */
    border: none; /* 无边框 */
    font-weight: 600; /* 字体加粗 */
    padding: 12px 20px; /* 内边距 */
    transition: all 0.3s ease; /* 过渡动画 */
}

/* 详情按钮悬停效果 */
.detail-button:hover {
    transform: translateY(-2px); /* 上移2px */
    box-shadow: 0 5px 15px rgba(75, 108, 183, 0.4); /* 阴影效果 */
}
</style>
