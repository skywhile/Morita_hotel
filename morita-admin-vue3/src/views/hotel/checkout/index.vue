<template>
    <div class="app-container">
        <!-- 顶部搜索 -->
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="订单号" prop="orderId">
                <el-input
                        v-model="queryParams.orderId"
                        placeholder="请输入订单号"
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
            <el-col :span="1.5">
                <el-button
                        type="primary"
                        plain
                        icon="Plus"
                        @click="handleAdd"
                        v-hasPermi="['hotel:checkout:add']"
                >新增
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="success"
                        plain
                        icon="Edit"
                        :disabled="single"
                        @click="handleUpdate"
                        v-hasPermi="['hotel:checkout:edit']"
                >修改
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="danger"
                        plain
                        icon="Delete"
                        :disabled="multiple"
                        @click="handleDelete"
                        v-hasPermi="['hotel:checkout:remove']"
                >删除
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="warning"
                        plain
                        icon="Download"
                        @click="handleExport"
                        v-hasPermi="['hotel:checkout:export']"
                >导出
                </el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!-- 表格 -->
        <el-table @row-click="clickRow" ref="table" highlight-current-row
                  border v-loading="loading" :data="checkoutList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column label="序号" align="center" type="index" :index="indexMethod"/>
            <el-table-column label="订单号" align="center" prop="orderId">
                <template #default="scope">
                    <el-link type="primary" @click="goToOrderDetail(scope.row.orderId)">
                        {{ scope.row.orderId }}
                    </el-link>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                               v-hasPermi="['hotel:checkout:edit']">修改
                    </el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                               v-hasPermi="['hotel:checkout:remove']">删除
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

        <!-- 添加或修改退房记录对话框 -->
        <vxe-modal :title="title" v-model="open" width="500px" show-maximize showFooter resize>
            <el-form ref="checkoutRef" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="订单号" prop="orderId">
                    <el-input v-model="form.orderId" placeholder="请输入订单号"/>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </template>
        </vxe-modal>

        <!-- 订单详情弹窗组件 -->
        <OrderDetail ref="orderDetail"/>
    </div>
</template>

<script setup name="Checkout">
import {listCheckout, getCheckout, delCheckout, addCheckout, updateCheckout} from "@/api/hotel/checkout"
import OrderDetail from "@/views/hotel/order/OrderDetail.vue";

const baseURL = import.meta.env.VITE_APP_BASE_API

const {proxy} = getCurrentInstance()

const checkoutList = ref([])
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
        orderId: null,
    },
    rules: {
        orderId: [
            {required: true, message: "订单号不能为空", trigger: "blur"}
        ],
    }
})

const {queryParams, form, rules} = toRefs(data)

//订单详情弹窗组件实例
const orderDetail = ref(null)

//打开订单详情弹窗组件
const goToOrderDetail = (orderId) => {
    orderDetail.value.handleOpen(orderId)
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

/** 查询退房记录列表 */
const getList = () => {
    loading.value = true
    listCheckout(queryParams.value).then(response => {
        checkoutList.value = response.rows
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
        checkoutId: null,
        orderId: null,
        createTime: null
    }
    proxy.resetForm("checkoutRef")
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
    ids.value = selection.map(item => item.checkoutId)
    single.value = selection.length != 1
    multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
    reset()
    open.value = true
    title.value = "添加退房记录"
}

/** 修改按钮操作 */
const handleUpdate = (row) => {
    reset()
    const _checkoutId = row.checkoutId || ids.value
    getCheckout(_checkoutId).then(response => {
        form.value = response.data
        open.value = true
        title.value = "修改退房记录"
    })
}

/** 提交按钮 */
const submitForm = () => {
    proxy.$refs["checkoutRef"].validate(valid => {
        if (valid) {
            if (form.value.checkoutId != null) {
                updateCheckout(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功")
                    open.value = false
                    getList()
                })
            } else {
                addCheckout(form.value).then(response => {
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
    const _checkoutIds = row.checkoutId || ids.value
    proxy.$modal.confirm('是否确认删除该项数据？').then(function () {
        return delCheckout(_checkoutIds)
    }).then(() => {
        getList()
        proxy.$modal.msgSuccess("删除成功")
    }).catch(() => {
    })
}

/** 导出按钮操作 */
const handleExport = () => {
    proxy.download('hotel/checkout/export', {
        ...queryParams.value
    }, `checkout_${new Date().getTime()}.xlsx`)
}

getList()
</script>
