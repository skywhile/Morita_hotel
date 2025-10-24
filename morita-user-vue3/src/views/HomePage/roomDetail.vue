<template>
  <!-- 页面容器 -->
    <div class="room-detail-page">
        <!-- 页面头部, 包含返回按钮和标题 -->
        <el-page-header @back="router.go(-1)" content="房间详情" class="page-header"/>

        <!-- 主要内容区域 -->
        <div v-loading="loading">
            <el-row :gutter="30">
                <!-- 左侧内容区域 -->
                <el-col :span="14">
                    <!-- 图片展示区域 -->
                    <div>
                        <img :src="baseUrl + room.image" alt="" class="room-main-image">
                    </div>

                    <!-- 房间描述区域 -->
                    <div class="room-description-section">
                        <h2>{{ room.name }}</h2>
                        <p class="room-description">{{ room.description }}</p>
                    </div>
                </el-col>

                <!-- 右侧内容区域 -->
                <el-col :span="10">
                    <div class="booking-panel">
                        <el-card>
                            <!-- 价格展示区域 -->
                            <div class="price-section">
                                <div class="price">¥{{ room.price }}<span class="price-unit">/晚</span></div>
                            </div>

                            <!-- 预订表单 -->
                            <div>
                                <el-form :model="bookingForm" label-position="top"
                                         :rules="rules" ref="bookingFormRef">
                                    <!-- 入住日期 -->
                                    <el-form-item label="入住日期" prop="checkInDate">
                                        <el-date-picker
                                                v-model="bookingForm.checkInDate"
                                                type="date"
                                                placeholder="选择入住日期"
                                                value-format="YYYY-MM-DD"
                                                style="width: 100%;"
                                                :disabled-date="disCheckInDate"
                                                @change="handleCheckInDate"
                                        ></el-date-picker>
                                    </el-form-item>

                                    <!-- 退房日期 -->
                                    <el-form-item label="退房日期" prop="checkOutDate">
                                        <el-date-picker
                                                v-model="bookingForm.checkOutDate"
                                                type="date"
                                                placeholder="选择退房日期"
                                                value-format="YYYY-MM-DD"
                                                style="width: 100%;"
                                                :disabled-date="disCheckOutDate"
                                        ></el-date-picker>
                                    </el-form-item>

                                    <!-- 房间数量 -->
                                    <el-form-item label="房间数量" prop="rooms">
                                        <el-input-number
                                                v-model="bookingForm.rooms"
                                                :min="1"
                                                style="width: 100%;">
                                        </el-input-number>
                                    </el-form-item>

                                    <!-- 入住晚数显示 -->
                                    <el-form-item>
                                        <div class="nights-count" v-if="nights > 0">{{ nights }}晚</div>
                                        <div class="nights-count" v-else>请选择日期</div>
                                    </el-form-item>

                                    <!-- 提交按钮 -->
                                    <el-form-item>
                                        <el-button
                                                type="primary"
                                                @click="submitBooking"
                                                :disabled="!canBook"
                                                :loading="bookingLoading"
                                                style="width: 100%;">
                                            立即预订
                                        </el-button>
                                    </el-form-item>

                                </el-form>
                            </div>

                            <!-- 价格明细 -->
                            <div class="price-breakdown" v-if="nights > 0">
                                <div class="breakdown-item">
                                    <span>¥{{ room.price }} * {{ nights }} * {{ bookingForm.rooms }}</span>
                                    <span>¥{{ room.price * nights * bookingForm.rooms }}</span>
                                </div>
                                <!-- 分割线 -->
                                <el-divider/>
                                <!-- 总价 -->
                                <div class="total-price">
                                    总价: <span class="price">¥{{ totalPrice }}</span>
                                </div>
                            </div>

                        </el-card>
                    </div>
                </el-col>
            </el-row>
        </div>

    </div>
</template>

<script setup>
import {useRoute, useRouter} from "vue-router";
import {getCategory} from "@/api/hotel/category.js";
import {formatDate} from "@/utils/index.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {addOrder} from "@/api/hotel/order.js";

const route = useRoute()
const router = useRouter()

//预约表单数据
const bookingForm = reactive({
    checkInDate: null,
    checkOutDate: null,
    rooms: 1
})

//表单验证规则
const rules = {
    checkInDate: [
        {required: true, message: '请选择入住日期', trigger: 'change'}
    ],
    checkOutDate: [
        {required: true, message: '请选择退房日期', trigger: 'change'}
    ],
    rooms: [
        {required: true, message: '请选择房间数量', trigger: 'change'}
    ],
}

//计算属性: 计算总价格
const totalPrice = computed(() => {
    return room.value.price * nights.value * bookingForm.rooms
})

//计算属性: 计算入住晚数
const nights = computed(() => {
    if (bookingForm.checkInDate && bookingForm.checkOutDate) {
        const checkIn = new Date(bookingForm.checkInDate);
        const checkOut = new Date(bookingForm.checkOutDate);
        const diffTime = Math.max(0, checkOut - checkIn);
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        return diffDays;
    }
    return 0
})

//计算属性: 判断是否可以预定
const canBook = computed(() => {
    return bookingForm.checkInDate &&
        bookingForm.checkOutDate &&
        bookingForm.rooms > 0 &&
        nights.value > 0
})

//基础API路径
const baseUrl = import.meta.env.VITE_APP_BASE_API

//房间详情数据
const room = ref({})

//加载状态
const loading = ref(false)
const bookingLoading = ref(false)

//表单引用
const bookingFormRef = ref(null)

//提交预订
const submitBooking = () => {
    //验证表单
    bookingFormRef.value.validate((valid) => {
        if (valid) {
            //打开加载状态
            bookingLoading.value = true
            //显示确认对话框
            ElMessageBox.confirm(
              `确认预订 ${room.value.name} ${nights.value}晚 ${bookingForm.rooms}间, 总价¥${totalPrice.value}吗?`,
              '提示',
              {
                  confirmButtonText: '确认',
                  cancelButtonText: '取消',
                  type: 'warning',
              }
            )
              .then(() => {
                  //用户确认预订, 准备表单数据
                  const form = {
                      categoryId: route.params.id,
                      checkInDate: bookingForm.checkInDate,
                      checkOutDate: bookingForm.checkOutDate,
                      nights: nights.value,
                      rooms: bookingForm.rooms,
                      unitPrice: room.value.price,
                      totalPrice: totalPrice.value
                  }

                  addOrder(form).then(res => {
                      ElMessage.success('预订成功!')
                      //关闭加载状态
                      bookingLoading.value = false
                      //跳转到订单页面
                      router.push('/index/orders')
                  }).catch(() => {
                      //关闭加载状态
                      bookingLoading.value = false
                  })
              })
              .catch(() => {
                  //关闭加载状态
                  bookingLoading.value = false
              })
        }
    })
}

//禁用入住日期函数(不能选择今天以前的日期)
const disCheckInDate = (date) => {
    const today = new Date();
    today.setHours(0, 0, 0, 0)
    return date < today
}

//禁用退房日期函数(不能选择入住日期之前的日期)
const disCheckOutDate = (date) => {
    if (bookingForm.checkInDate) {
        const checkIn = new Date(bookingForm.checkInDate);
        checkIn.setHours(0, 0, 0, 0)
        return date <= checkIn
    }
    return false
}

//处理入住日期变化
const handleCheckInDate = () => {
    if (bookingForm.checkOutDate && bookingForm.checkInDate) {
        const checkIn = new Date(bookingForm.checkInDate)
        const checkOut = new Date(bookingForm.checkOutDate)

        if (checkOut <= checkIn) {
            //如果退房日期不晚于入住日期, 自动设置为入住日期的后一天
            const nextDay = new Date(checkIn)
            nextDay.setDate(nextDay.getDate() + 1)
            bookingForm.checkOutDate = formatDate(nextDay)
        }
    }
}

//组件挂载时执行
onMounted(() => {
    //获取房间详情信息
    getCategory(route.params.id).then(res => {
        room.value = res.data
    })
})
</script>

<style scoped>
/* 页面容器样式 */
.room-detail-page {
    max-width: 1200px; /* 最大宽度 */
    margin: 0 auto; /* 水平居中 */
    padding: 20px; /* 内边距 */
}

/* 页面头部样式 */
.page-header {
    margin-bottom: 20px; /* 底部外边距 */
}

/* 主房间图片样式 */
.room-main-image {
    width: 100%; /* 宽度100% */
    height: 400px; /* 固定高度 */
    object-fit: cover; /* 保持比例覆盖容器 */
    border-radius: 8px; /* 圆角边框 */
}

/* 深度选择器，修改Element UI组件内部样式 */
:deep(.el-carousel) {
    border-radius: 8px; /* 轮播图圆角 */
}

/* 房间描述区域标题样式 */
.room-description-section h2 {
    margin: 20px 0 10px 0; /* 外边距：上下20px，左右0，底部10px */
    color: #333; /* 字体颜色 */
    font-size: 24px; /* 字体大小 */
}

/* 房间描述文本样式 */
.room-description {
    color: #666; /* 字体颜色 */
    line-height: 1.8; /* 行高 */
    margin-bottom: 30px; /* 底部外边距 */
}

/* 预订面板样式 */
.booking-panel {
    position: sticky; /* 粘性定位 */
    top: 20px; /* 距离顶部20px */
}

/* 价格区域样式 */
.price-section {
    display: flex; /* 使用Flex布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中 */
    margin-bottom: 20px; /* 底部外边距 */
    padding-bottom: 15px; /* 底部内边距 */
    border-bottom: 1px solid #eee; /* 底部边框 */
}

/* 价格文本样式 */
.price {
    font-size: 28px; /* 字体大小 */
    font-weight: bold; /* 字体粗细 */
    color: #e64545; /* 字体颜色 */
}

/* 价格单位样式 */
.price-unit {
    font-size: 14px; /* 字体大小 */
    color: #999; /* 字体颜色 */
    margin-left: 4px; /* 左侧外边距 */
}

/* 入住晚数显示样式 */
.nights-count {
    font-size: 16px; /* 字体大小 */
    font-weight: bold; /* 字体粗细 */
    text-align: center; /* 文本居中 */
    padding: 8px; /* 内边距 */
    background-color: #f5f7fa; /* 背景颜色 */
    border-radius: 4px; /* 圆角边框 */
}

/* 价格明细区域样式 */
.price-breakdown {
    margin-top: 20px; /* 顶部外边距 */
    padding-top: 15px; /* 顶部内边距 */
    border-top: 1px solid #eee; /* 顶部边框 */
}

/* 价格明细项样式 */
.breakdown-item {
    display: flex; /* 使用Flex布局 */
    justify-content: space-between; /* 两端对齐 */
    margin-bottom: 8px; /* 底部外边距 */
    color: #666; /* 字体颜色 */
}

/* 总价区域样式 */
.total-price {
    display: flex; /* 使用Flex布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中 */
    font-size: 18px; /* 字体大小 */
    font-weight: bold; /* 字体粗细 */
}

/* 总价文本样式 */
.total-price .price {
    font-size: 24px; /* 字体大小 */
}

/* 深度选择器，修改表单标签样式 */
:deep(.el-form-item__label) {
    font-weight: bold; /* 字体粗细 */
}
</style>
