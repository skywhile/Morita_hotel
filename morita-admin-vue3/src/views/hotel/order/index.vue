<template>
    <div class="app-container">
        <!-- 顶部搜索 -->
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="85px">
            <el-form-item label="房间类型" prop="categoryId">
                <el-select style="width: 200px;" v-model="queryParams.categoryId" placeholder="请选择房间类型">
                    <el-option
                            v-for="item in categoryList"
                            :key="item.categoryId"
                            :label="item.name"
                            :value="item.categoryId"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="订单状态" prop="status">
                <el-select style="width: 200px;" v-model="queryParams.status" placeholder="请选择订单状态" clearable>
                    <el-option
                            v-for="dict in order_status"
                            :key="dict.value"
                            :label="dict.label"
                            :value="dict.value"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="订单创建人" prop="userName">
                <el-input
                        v-model="queryParams.userName"
                        placeholder="请输入订单创建人用户名"
                        clearable
                        @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <!-- 顶部按钮 -->
        <el-row :gutter="10" class="mb8">
            <!--            <el-col :span="1.5">-->
            <!--                <el-button-->
            <!--                        type="primary"-->
            <!--                        plain-->
            <!--                        icon="Plus"-->
            <!--                        @click="handleAdd"-->
            <!--                        v-hasPermi="['hotel:order:add']"-->
            <!--                >新增-->
            <!--                </el-button>-->
            <!--            </el-col>-->
            <!--            <el-col :span="1.5">-->
            <!--                <el-button-->
            <!--                        type="success"-->
            <!--                        plain-->
            <!--                        icon="Edit"-->
            <!--                        :disabled="single"-->
            <!--                        @click="handleUpdate"-->
            <!--                        v-hasPermi="['hotel:order:edit']"-->
            <!--                >修改-->
            <!--                </el-button>-->
            <!--            </el-col>-->
            <!--            <el-col :span="1.5">-->
            <!--                <el-button-->
            <!--                        type="danger"-->
            <!--                        plain-->
            <!--                        icon="Delete"-->
            <!--                        :disabled="multiple"-->
            <!--                        @click="handleDelete"-->
            <!--                        v-hasPermi="['hotel:order:remove']"-->
            <!--                >删除-->
            <!--                </el-button>-->
            <!--            </el-col>-->
            <el-col :span="1.5">
                <el-button
                        type="warning"
                        plain
                        icon="Download"
                        @click="handleExport"
                        v-hasPermi="['hotel:order:export']"
                >导出
                </el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!-- 表格 -->
        <el-table @row-click="clickRow" ref="table" highlight-current-row
                  border v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column label="序号" align="center" type="index" :index="indexMethod"/>
            <el-table-column label="订单号" align="center" prop="orderId" width="170">
                <template #default="scope">
                    <el-link type="primary" @click="goToOrderDetail(scope.row.orderId)">
                        {{ scope.row.orderId }}
                    </el-link>
                </template>
            </el-table-column>
            <el-table-column label="房间类型" align="center" prop="name"/>
            <el-table-column label="描述" align="center" prop="description"/>
            <el-table-column label="图片" align="center" prop="image" width="100">
                <template #default="scope">
                    <image-preview :src="scope.row.image" :width="50" :height="50"/>
                </template>
            </el-table-column>
            <el-table-column label="天数" align="center" prop="nights"/>
            <el-table-column label="房间数" align="center" prop="rooms"/>
            <el-table-column label="单价" align="center" prop="unitPrice"/>
            <el-table-column label="总价" align="center" prop="totalPrice"/>
            <el-table-column label="订单状态" align="center" prop="status">
                <template #default="scope">
                    <dict-tag :options="order_status" :value="scope.row.status"/>
                </template>
            </el-table-column>
            <el-table-column label="订单创建用户" align="center" prop="userName"/>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <!--                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"-->
                    <!--                               v-hasPermi="['hotel:order:edit']">修改-->
                    <!--                    </el-button>-->
                    <!--                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"-->
                    <!--                               v-hasPermi="['hotel:order:remove']">删除-->
                    <!--                    </el-button>-->
                    <el-button v-if="scope.row.status === '待入住'" link
                               type="primary" icon="School" @click="handleCheckIn(scope.row)"
                    >入住登记
                    </el-button>
                    <el-button v-if="scope.row.status === '已入住'" link
                               type="primary" icon="School" @click="handleCheckOut(scope.row)"
                    >退房
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页组件 -->
        <pagination
                v-show="total>0"
                :total="total"
                v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize"
                @pagination="getList"
        />

        <!-- 添加或修改订单对话框 -->
        <vxe-modal :title="title" v-model="open" width="500px" show-maximize showFooter resize>
            <el-form ref="orderRef" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="房间类型ID" prop="categoryId">
                    <el-input v-model="form.categoryId" placeholder="请输入房间类型ID"/>
                </el-form-item>
                <el-form-item label="入住日期" prop="checkInDate">
                    <el-date-picker clearable
                                    v-model="form.checkInDate"
                                    type="date"
                                    value-format="YYYY-MM-DD"
                                    placeholder="请选择入住日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="退房日期" prop="checkOutDate">
                    <el-date-picker clearable
                                    v-model="form.checkOutDate"
                                    type="date"
                                    value-format="YYYY-MM-DD"
                                    placeholder="请选择退房日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="天数" prop="nights">
                    <el-input v-model="form.nights" placeholder="请输入天数"/>
                </el-form-item>
                <el-form-item label="房间数" prop="rooms">
                    <el-input v-model="form.rooms" placeholder="请输入房间数"/>
                </el-form-item>
                <el-form-item label="单价" prop="unitPrice">
                    <el-input v-model="form.unitPrice" placeholder="请输入单价"/>
                </el-form-item>
                <el-form-item label="总价" prop="totalPrice">
                    <el-input v-model="form.totalPrice" placeholder="请输入总价"/>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </template>
        </vxe-modal>

        <!-- 入住登记弹窗组件 -->
        <CheckInModal ref="checkInModal" @ok="getList"/>

        <!-- 订单详情弹窗组件 -->
        <OrderDetail ref="orderDetail"/>
    </div>
</template>

<script setup name="Order">
import {listOrder, getOrder, delOrder, addOrder, updateOrder} from "@/api/hotel/order"
import {getToken} from "@/utils/auth.js";
import {selectCategoryAllList} from "@/api/hotel/category.js";
import CheckInModal from "@/views/hotel/order/CheckInModal.vue";
import OrderDetail from "@/views/hotel/order/OrderDetail.vue";
import {addCheckout} from "@/api/hotel/checkout.js";
import {ElMessage} from "element-plus";

const baseURL = import.meta.env.VITE_APP_BASE_API

const {proxy} = getCurrentInstance()
const {order_status} = proxy.useDict('order_status')

const orderList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const selectedRow = ref(null)

const data = reactive({
    form: {},
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        categoryId: null,
        status: null,
        userId: null,
        userName: null
    },
    rules: {
        categoryId: [
            {required: true, message: "房间类型ID不能为空", trigger: "blur"}
        ],
        checkInDate: [
            {required: true, message: "入住日期不能为空", trigger: "blur"}
        ],
        checkOutDate: [
            {required: true, message: "退房日期不能为空", trigger: "blur"}
        ],
        nights: [
            {required: true, message: "天数不能为空", trigger: "blur"}
        ],
        rooms: [
            {required: true, message: "房间数不能为空", trigger: "blur"}
        ],
        unitPrice: [
            {required: true, message: "单价不能为空", trigger: "blur"}
        ],
        totalPrice: [
            {required: true, message: "总价不能为空", trigger: "blur"}
        ]
    }
})

const {queryParams, form, rules} = toRefs(data)

//退房
const handleCheckOut = (row) => {
    const item = {
        orderId: row.orderId
    }
    addCheckout(item).then(res => {
        ElMessage.success('退房成功')
        getList()
    })
}

//订单详情弹窗组件实例
const orderDetail = ref(null)

//打开订单详情弹窗组件
const goToOrderDetail = (orderId) => {
    orderDetail.value.handleOpen(orderId)
}

//入住登记弹窗组件实例
const checkInModal = ref(null)

//打开入住登记弹窗组件
const handleCheckIn = (row) => {
    checkInModal.value.handleOpen(row)
}

//点击行 获取行
const clickRow = (row) => {
    selectedRow.value = row; // 更新选中的行
    const table = proxy.$refs.table;
    // 清除所有已选中的行
    table.clearSelection();
    // 选中当前点击的行
    table.toggleRowSelection(row, true);
}

/** 自定义序号 */
const indexMethod = (index) => {
    let pageNum = queryParams.value.pageNum - 1;
    if ((pageNum !== -1 && pageNum !== 0)) {
        return (index + 1) + (pageNum * queryParams.value.pageSize);
    } else {
        return (index + 1)
    }
}

/** 查询订单列表 */
const getList = () => {
    loading.value = true
    listOrder(queryParams.value).then(response => {
        orderList.value = response.rows
        total.value = response.total
        loading.value = false
    })
}

// 取消按钮
const cancel = () => {
    open.value = false
    reset()
}

// 表单重置
const reset = () => {
    form.value = {
        orderId: null,
        categoryId: null,
        checkInDate: null,
        checkOutDate: null,
        nights: null,
        rooms: null,
        unitPrice: null,
        totalPrice: null,
        status: null,
        userId: null,
        createTime: null
    }
    proxy.resetForm("orderRef")
}

/** 搜索按钮操作 */
const handleQuery = () => {
    queryParams.value.pageNum = 1
    getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
    proxy.resetForm("queryRef")
    handleQuery()
}

// 多选框选中数据
const handleSelectionChange = (selection) => {
    ids.value = selection.map(item => item.orderId)
    single.value = selection.length != 1
    multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
    reset()
    open.value = true
    title.value = "添加订单"
}

/** 修改按钮操作 */
const handleUpdate = (row) => {
    reset()
    const _orderId = row.orderId || ids.value
    getOrder(_orderId).then(response => {
        form.value = response.data
        open.value = true
        title.value = "修改订单"
    })
}

/** 提交按钮 */
const submitForm = () => {
    proxy.$refs["orderRef"].validate(valid => {
        if (valid) {
            if (form.value.orderId != null) {
                updateOrder(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功")
                    open.value = false
                    getList()
                })
            } else {
                addOrder(form.value).then(response => {
                    proxy.$modal.msgSuccess("新增成功")
                    open.value = false
                    getList()
                })
            }
        }
    })
}

/** 删除按钮操作 */
const handleDelete = (row) => {
    const _orderIds = row.orderId || ids.value
    proxy.$modal.confirm('是否确认删除该项数据？').then(function () {
        return delOrder(_orderIds)
    }).then(() => {
        getList()
        proxy.$modal.msgSuccess("删除成功")
    }).catch(() => {
    })
}

/** 导出按钮操作 */
const handleExport = () => {
    proxy.download('hotel/order/export', {
        ...queryParams.value
    }, `order_${new Date().getTime()}.xlsx`)
}

//房间分类列表
const categoryList = ref([])

//不分页查询房间分类列表
const getAllCategoryList = () => {
    selectCategoryAllList().then(res => {
        categoryList.value = res.data
    })
}

getList()
getAllCategoryList()
</script>
