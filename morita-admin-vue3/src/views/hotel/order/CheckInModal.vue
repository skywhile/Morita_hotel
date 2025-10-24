<template>
    <vxe-modal title="入住登记" v-model="open" width="500px" show-maximize showFooter resize>
        <el-form ref="checkinRef" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="房间号" prop="roomId">
                <el-select v-model="form.roomId" placeholder="请分配房间号">
                    <el-option
                      v-for="item in roomNumberList"
                      :key="item.roomId"
                      :label="item.roomNumber"
                      :value="item.roomId"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="姓名" prop="name">
                <el-input v-model="form.name" placeholder="请输入姓名"/>
            </el-form-item>
            <el-form-item label="性别" prop="gender">
                <el-select v-model="form.gender" placeholder="请选择性别">
                    <el-option
                            v-for="dict in sys_user_sex"
                            :key="dict.value"
                            :label="dict.label"
                            :value="dict.value"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="电话号码" prop="phone">
                <el-input v-model="form.phone" placeholder="请输入电话号码"/>
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="open = false">取 消</el-button>
            </div>
        </template>
    </vxe-modal>
</template>

<script setup>

//当前组件实例
import {selectRoomNumberListByCategoryId} from "@/api/hotel/room.js";
import {addCheckin, updateCheckin} from "@/api/hotel/checkin.js";

const {proxy} = getCurrentInstance()

//获取对应的字典数据
const {sys_user_sex} = proxy.useDict('sys_user_sex')

//弹窗是否打开, 默认为关闭状态
const open = ref(false)

//表单
const form = ref({
    checkinId: null,
    orderId: null,
    roomId: null,
    name: null,
    gender: null,
    phone: null
})

//表单验证规则
const rules = ref({
    orderId: [
        {required: true, message: "订单ID不能为空", trigger: "blur"}
    ],
    roomId: [
        {required: true, message: "房间ID不能为空", trigger: "blur"}
    ],
    name: [
        {required: true, message: "姓名不能为空", trigger: "blur"}
    ],
    gender: [
        {required: true, message: "性别不能为空", trigger: "change"}
    ],
    phone: [
        {required: true, message: "电话号码不能为空", trigger: "blur"}
    ]
})

//定义组件可触发的自定义事件
const emit = defineEmits(['ok'])

//提交按钮
const submitForm = () => {
    proxy.$refs["checkinRef"].validate(valid => {
        if (valid) {
            addCheckin(form.value).then(response => {
                proxy.$modal.msgSuccess("登记成功")
                open.value = false
                emit('ok')
            })
        }
    })
}

//对应房间类型的房间号列表
const roomNumberList = ref([])

//父组件调用的方法
const handleOpen = (row) => {
    form.value = {
        checkinId: null,
        orderId: null,
        roomId: null,
        name: null,
        gender: null,
        phone: null
    }
    form.value.orderId = row.orderId
    selectRoomNumberListByCategoryId(row.categoryId).then(res => {
        roomNumberList.value = res.data
        open.value = true
    })
}

//必须要暴露方法, 父组件才能调用
defineExpose({
    handleOpen
})
</script>

<style scoped>

</style>
