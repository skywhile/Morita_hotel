<template>
  <!-- 页面容器 -->
    <div class="recharge-page">
        <!-- 主要内容区域 -->
        <div class="recharge-content">
            <el-row :gutter="20">
                <!-- 左侧内容区域 -->
                <el-col :span="16">
                    <!-- 充值卡片 -->
                    <el-card class="recharge-card">
                        <!-- 卡片头部 -->
                        <div class="card-header">
                            <!-- 标题 -->
                            <h2>账户余额充值</h2>
                            <!-- 显示当前余额 -->
                            <div class="current-balance">
                                当前余额: <span class="balance-amount">¥{{ balance }}</span>
                            </div>
                        </div>

                        <!-- 充值表单区域 -->
                        <div class="recharge-form">
                            <el-form :model="rechargeForm" label-width="100px" ref="rechargeFormRef">
                                <!-- 充值金额表单项 -->
                                <el-form-item
                                        label="充值金额"
                                        prop="amount"
                                        :rules="[{ required: true, message: '请输入充值金额', trigger: 'blur' }]">
                                    <!-- 金额输入框 -->
                                    <el-input v-model.number="rechargeForm.amount"
                                              placeholder="请输入充值金额"
                                              style="width: 100%;"
                                              type="number">
                                        <template #prepend>¥</template>
                                    </el-input>
                                </el-form-item>

                                <!-- 充值方式表单项 -->
                                <el-form-item label="充值方式">
                                    <!-- 单选按钮 -->
                                    <el-radio-group v-model="rechargeForm.paymentMethod">
                                        <el-radio label="alipay">支付宝</el-radio>
                                        <el-radio label="wechat">微信支付</el-radio>
                                        <el-radio label="unionpay">银联支付</el-radio>
                                    </el-radio-group>
                                </el-form-item>

                                <!-- 提交按钮 -->
                                <el-form-item>
                                    <el-button
                                            type="primary"
                                            size="large"
                                            @click="submitRecharge"
                                            :loading="loading">
                                        立即充值
                                    </el-button>
                                </el-form-item>

                            </el-form>
                        </div>

                    </el-card>
                </el-col>

                <!-- 右侧信息区域 -->
                <el-col :span="8">
                    <!-- 信息卡片 -->
                    <el-card class="recharge-info">
                        <!-- 信息头部 -->
                        <div class="info-header">
                            <h3>充值说明</h3>
                        </div>
                        <!-- 信息内容 -->
                        <div class="info-content">
                            <ul>
                                <li>充值金额实时到账, 可用于酒店预订等功能</li>
                                <li>如有疑问, 请联系客服: 400-123-4567</li>
                            </ul>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script setup>
import useUserStore from "@/store/modules/user.js";
import {getUser} from "@/api/system/user.js";
import {ElMessage} from "element-plus";
import {recharge} from "@/api/hotel/order.js";

//加载状态, 默认关闭状态
const loading = ref(false)

//充值表单数据
const rechargeForm = reactive({
    amount: null, //充值金额
    paymentMethod: 'alipay' //支付方式, 默认为支付宝
})

//当前用户信息
const loginUser = useUserStore()

//当前用户的账户余额
const balance = ref(0.00)

//提交充值
const submitRecharge = () => {
    //验证充值金额是否有效
    if (!rechargeForm.amount || rechargeForm.amount <= 0) {
        ElMessage.warning('请输入有效的充值金额')
        rechargeForm.amount = null
    }

    //打开加载状态
    loading.value = true

    //调用充值API
    recharge(rechargeForm.amount).then(res => {
        console.log(res, '看看返回值')
        //充值成功提示
        ElMessage.success('充值成功!')
        //刷新余额显示
        getLoginUserBalance()
        //清空输入框
        rechargeForm.amount = null
        //取消加载状态
        loading.value = false
    })

}

const getLoginUserBalance = () => {
    getUser(loginUser.id).then(res => {
        balance.value = res.data.balance
    })
}

onMounted(() => {
    getLoginUserBalance()
})
</script>

<style scoped>
/* 页面容器样式 */
.recharge-page {
    max-width: 1200px; /* 最大宽度 */
    margin: 0 auto; /* 水平居中 */
    padding: 20px; /* 内边距 */
}

/* 主要内容区域样式 */
.recharge-content {
    margin-top: 20px; /* 顶部外边距 */
}

/* 充值卡片样式 */
.recharge-card {
    min-height: 400px; /* 最小高度 */
}

/* 卡片头部样式 */
.card-header {
    display: flex; /* 使用Flex布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中 */
    margin-bottom: 30px; /* 底部外边距 */
    padding-bottom: 15px; /* 底部内边距 */
    border-bottom: 1px solid #eee; /* 底部边框 */
}

/* 卡片标题样式 */
.card-header h2 {
    margin: 0; /* 清除默认外边距 */
    color: #333; /* 字体颜色 */
}

/* 当前余额显示样式 */
.current-balance {
    font-size: 16px; /* 字体大小 */
    color: #666; /* 字体颜色 */
}

/* 余额金额样式 */
.balance-amount {
    font-size: 24px; /* 字体大小 */
    font-weight: bold; /* 字体粗细 */
    color: #e64545; /* 字体颜色（红色系） */
}

/* 充值表单样式 */
.recharge-form {
    max-width: 500px; /* 最大宽度 */
    margin: 0 auto; /* 水平居中 */
}

/* 信息卡片样式 */
.recharge-info {
    min-height: 200px; /* 最小高度 */
}

/* 信息头部样式 */
.info-header {
    padding-bottom: 15px; /* 底部内边距 */
    border-bottom: 1px solid #eee; /* 底部边框 */
    margin-bottom: 15px; /* 底部外边距 */
}

/* 信息标题样式 */
.info-header h3 {
    margin: 0; /* 清除默认外边距 */
    color: #333; /* 字体颜色 */
}

/* 信息内容样式 */
.info-content ul {
    padding-left: 20px; /* 左侧内边距 */
    line-height: 1.8; /* 行高 */
    color: #666; /* 字体颜色 */
}

/* 列表项样式 */
.info-content li {
    margin-bottom: 10px; /* 底部外边距 */
}
</style>
