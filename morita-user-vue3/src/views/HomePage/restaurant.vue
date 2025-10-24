<template>
  <!-- 餐饮页面主容器 -->
    <div class="dining-page">
        <!-- 餐厅介绍 -->
        <p class="intro-text">
            酒店提供多样化的餐饮选择, 从精致的国际美食到地道的本地佳肴, 我们的厨师团队致力于为宾客打造难忘的用餐体验
        </p>

        <!-- 餐厅列表展示区域 -->
        <el-row :gutter="30">
            <el-col :span="12" v-for="restaurant in restaurantList" :key="restaurant.restaurantId">
                <!-- 餐厅卡片组件 -->
                <el-card class="restaurant-card">
                    <img :src="baseUrl + restaurant.image" alt="" class="restaurant-image">
                    <!-- 餐厅信息 -->
                    <div class="restaurant-info">
                        <h2>{{ restaurant.name }}</h2>
                        <p class="restaurant-description">{{ restaurant.description }}</p>
                        <!-- 位置信息 -->
                        <div class="detail-item">
                            <strong>位置: </strong>
                            <span>{{ restaurant.location }}</span>
                        </div>
                        <!-- 联系电话 -->
                        <div class="detail-item">
                            <strong>联系电话: </strong>
                            <span>{{ restaurant.phone }}</span>
                        </div>
                        <!-- 特色菜品 -->
                        <div class="detail-item">
                            <strong>特色菜品: </strong>
                            <span>{{ restaurant.specialty }}</span>
                        </div>
                    </div>
                    <!-- 查看菜单按钮 -->
                    <el-button type="primary" @click="viewMenu(restaurant)">查看菜单</el-button>
                </el-card>
            </el-col>
        </el-row>

        <!-- 菜单弹窗组件 -->
        <vxe-modal v-model="open" title="菜单" width="800px">
            <div class="menu-dialog menu-content">
                <div v-for="category in selectedMenuList" :key="category.category" class="menu-category">
                    <h3>{{ category.category }}</h3>
                    <el-table :data="category.items" style="width: 100%;" :show-header="false">
                        <el-table-column prop="name" label="菜名" width="200"></el-table-column>
                        <el-table-column prop="description" label="描述"></el-table-column>
                        <el-table-column prop="price" label="价格" width="80">
                            <template #default="scope">
                                <span class="price">¥{{ scope.row.price }}</span>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>
        </vxe-modal>

    </div>
</template>

<script setup>
import {listRestaurant} from "@/api/hotel/restaurant.js";

//弹窗是否打开状态
const open = ref(false)

//基础API地址
const baseUrl = import.meta.env.VITE_APP_BASE_API

//餐厅列表数据
const restaurantList = ref([])

//菜单数据
const selectedRestaurant = ref({})

//计算属性: 将当前选中的餐厅菜单进行按分类合并
const selectedMenuList = computed(() => {
    if (!selectedRestaurant.value) return []

    //按分类对菜单项进行分组
    const cate = {}
    selectedRestaurant.value.menuList.forEach(menu => {
        if (!cate[menu.category]) {
            cate[menu.category] = {
                category: menu.category,
                items: []
            }
        }
        cate[menu.category].items.push(menu)
    })

    //将分组对象转为为数组返回
    return Object.values(cate)
})

//查看菜单按钮
const viewMenu = (item) => {
    selectedRestaurant.value = item
    open.value = true
}

onMounted(() => {
    listRestaurant().then(res => {
        restaurantList.value = res.rows
        console.log(restaurantList.value, '看看主子表的数据')
    })
})
</script>

<style scoped>
/* 餐饮页面主容器样式 */
.dining-page {
    max-width: 1200px; /* 最大宽度 */
    margin: 0 auto; /* 水平居中 */
    padding: 20px; /* 内边距 */
}

/* 介绍文本样式 */
.intro-text {
    font-size: 18px; /* 字体大小 */
    text-align: center; /* 文本居中 */
    color: #666; /* 文字颜色 */
    margin-bottom: 40px; /* 底部外边距 */
    line-height: 1.6; /* 行高 */
}

/* 餐厅卡片样式 */
.restaurant-card {
    margin-bottom: 30px; /* 底部外边距 */
}

/* 餐厅图片样式 */
.restaurant-image {
    width: 100%; /* 宽度100% */
    height: 250px; /* 固定高度 */
    object-fit: cover; /* 保持比例填充容器 */
    border-radius: 4px; /* 圆角 */
}

/* 餐厅信息区域样式 */
.restaurant-info {
    padding: 20px 0 0; /* 上、左右、下内边距 */
}

/* 餐厅名称样式 */
.restaurant-info h2 {
    margin: 0 0 15px 0; /* 外边距 */
    color: #333; /* 文字颜色 */
}

/* 餐厅描述样式 */
.restaurant-description {
    color: #666; /* 文字颜色 */
    line-height: 1.6; /* 行高 */
    margin-bottom: 20px; /* 底部外边距 */
}

/* 详情项样式 */
.detail-item {
    margin-bottom: 10px; /* 底部外边距 */
    color: #666; /* 文字颜色 */
}

/* 详情项中strong标签样式 */
.detail-item strong {
    color: #333; /* 文字颜色 */
}

/* 菜单弹窗内容区域样式 */
.menu-dialog .menu-content {
    max-height: 60vh; /* 最大高度为视口的60% */
    overflow-y: auto; /* 垂直方向溢出时显示滚动条 */
}

/* 菜单分类样式 */
.menu-dialog .menu-category {
    margin-bottom: 20px; /* 底部外边距 */
}

/* 菜单分类标题样式 */
.menu-dialog .menu-category h3 {
    border-left: 4px solid #409eff; /* 左侧边框，蓝色 */
    padding-left: 10px; /* 左侧内边距 */
    margin-bottom: 15px; /* 底部外边距 */
    color: #333; /* 文字颜色 */
}

/* 价格样式 */
.menu-dialog .price {
    font-weight: bold; /* 字体加粗 */
    color: #e64545; /* 红色文字 */
}
</style>
