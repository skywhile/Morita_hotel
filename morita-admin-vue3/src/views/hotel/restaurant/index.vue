<template>
    <div class="app-container">
        <!-- 顶部搜索 -->
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="餐厅名称" prop="name">
                <el-input
                        v-model="queryParams.name"
                        placeholder="请输入餐厅名称"
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
                        v-hasPermi="['hotel:restaurant:add']"
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
                        v-hasPermi="['hotel:restaurant:edit']"
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
                        v-hasPermi="['hotel:restaurant:remove']"
                >删除
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="warning"
                        plain
                        icon="Download"
                        @click="handleExport"
                        v-hasPermi="['hotel:restaurant:export']"
                >导出
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="primary"
                        icon="Upload"
                        size="mini"
                        @click="handleImport"
                >导入
                </el-button>
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!-- 表格 -->
        <el-table @row-click="clickRow" ref="table" highlight-current-row
                  border v-loading="loading" :data="restaurantList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column label="序号" align="center" type="index" :index="indexMethod"/>
            <el-table-column label="餐厅名称" align="center" prop="name"/>
            <el-table-column label="描述" align="center" prop="description"/>
            <el-table-column label="位置" align="center" prop="location"/>
            <el-table-column label="联系电话" align="center" prop="phone"/>
            <el-table-column label="特色" align="center" prop="specialty"/>
            <el-table-column label="图片" align="center" prop="image" width="100">
                <template #default="scope">
                    <image-preview :src="scope.row.image" :width="50" :height="50"/>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                               v-hasPermi="['hotel:restaurant:edit']">修改
                    </el-button>
                    <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                               v-hasPermi="['hotel:restaurant:remove']">删除
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

        <!-- 导入对话框 -->
        <vxe-modal :title="upload.title" v-model="upload.open" width="400px" showFooter show-zoom resize>
            <el-upload
                    ref="uploadRef"
                    :limit="1"
                    accept=".xlsx, .xls"
                    :headers="upload.headers"
                    :action="upload.url"
                    :data="{ updateSupport: upload.updateSupport }"
                    :disabled="upload.isUploading"
                    :on-progress="handleFileUploadProgress"
                    :on-success="handleFileSuccess"
                    :auto-upload="false"
                    drag
            >
                <el-icon class="el-icon--upload">
                    <upload-filled/>
                </el-icon>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <template #tip>
                    <div class="el-upload__tip text-center">
                        <span>仅允许导入xls、xlsx格式文件。</span>
                        <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;"
                                 @click="importTemplate">下载模板
                        </el-link>
                    </div>
                </template>
            </el-upload>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitFileForm">确 定</el-button>
                <el-button @click="upload.open = false">取 消</el-button>
            </div>
        </vxe-modal>

        <!-- 添加或修改餐厅对话框 -->
        <vxe-modal :title="title" v-model="open" width="50%" show-maximize showFooter resize>
            <el-form ref="restaurantRef" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="餐厅名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入餐厅名称"/>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input v-model="form.description" type="textarea" placeholder="请输入内容"/>
                </el-form-item>
                <el-form-item label="位置" prop="location">
                    <el-input v-model="form.location" placeholder="请输入位置"/>
                </el-form-item>
                <el-form-item label="联系电话" prop="phone">
                    <el-input v-model="form.phone" placeholder="请输入联系电话"/>
                </el-form-item>
                <el-form-item label="特色" prop="specialty">
                    <el-input v-model="form.specialty" placeholder="请输入特色"/>
                </el-form-item>
                <el-form-item label="图片" prop="image">
                    <image-upload v-model="form.image"/>
                </el-form-item>
                <el-divider content-position="center">菜单信息</el-divider>
                <el-row :gutter="10" class="mb8">
                    <el-col :span="1.5">
                        <el-button type="primary" icon="Plus" @click="handleAddMenu">添加</el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button type="danger" icon="Delete" @click="handleDeleteMenu">删除</el-button>
                    </el-col>
                </el-row>
                <el-table height="25vh" :data="menuList" :row-class-name="rowMenuIndex" @selection-change="handleMenuSelectionChange"
                          ref="menu">
                    <el-table-column type="selection" width="50" align="center"/>
                    <el-table-column label="序号" align="center" prop="index" width="50"/>
                    <el-table-column label="菜单种类" prop="category">
                        <template #default="scope">
                            <el-select v-model="scope.row.category" placeholder="请选择菜单种类">
                                <el-option
                                        v-for="dict in menu_category"
                                        :key="dict.value"
                                        :label="dict.label"
                                        :value="dict.value"
                                ></el-option>
                            </el-select>
                        </template>
                    </el-table-column>
                    <el-table-column label="菜品名" prop="name">
                        <template #default="scope">
                            <el-input v-model="scope.row.name" placeholder="请输入菜品名"/>
                        </template>
                    </el-table-column>
                    <el-table-column label="价格" prop="price">
                        <template #default="scope">
                            <el-input v-model="scope.row.price" placeholder="请输入价格"/>
                        </template>
                    </el-table-column>
                </el-table>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </template>
        </vxe-modal>
    </div>
</template>

<script setup name="Restaurant">
import {listRestaurant, getRestaurant, delRestaurant, addRestaurant, updateRestaurant} from "@/api/hotel/restaurant"
import {getToken} from "@/utils/auth.js";

const baseURL = import.meta.env.VITE_APP_BASE_API

const {proxy} = getCurrentInstance()
const {menu_category} = proxy.useDict('menu_category')

const restaurantList = ref([])
const menuList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const checkedMenu = ref([])
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
        name: null,
    },
    rules: {
        name: [
            {required: true, message: "餐厅名称不能为空", trigger: "blur"}
        ],
        description: [
            {required: true, message: "描述不能为空", trigger: "blur"}
        ],
        location: [
            {required: true, message: "位置不能为空", trigger: "blur"}
        ],
        phone: [
            {required: true, message: "联系电话不能为空", trigger: "blur"}
        ],
        specialty: [
            {required: true, message: "特色不能为空", trigger: "blur"}
        ],
        image: [
            {required: true, message: "图片不能为空", trigger: "blur"}
        ],
    },
    // 导入参数
    upload: {
        // 是否显示弹出层（导入）
        open: false,
        // 弹出层标题（导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: {Authorization: "Bearer " + getToken()},
        // 上传的地址
        url: baseURL + "/hotel/restaurant/importData"
    }
})

const {queryParams, form, rules, upload} = toRefs(data)

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

/** 查询餐厅列表 */
const getList = () => {
    loading.value = true
    listRestaurant(queryParams.value).then(response => {
        restaurantList.value = response.rows
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
        restaurantId: null,
        name: null,
        description: null,
        location: null,
        phone: null,
        specialty: null,
        image: null,
        createTime: null
    }
    menuList.value = []
    proxy.resetForm("restaurantRef")
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
    ids.value = selection.map(item => item.restaurantId)
    single.value = selection.length != 1
    multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
    reset()
    open.value = true
    title.value = "添加餐厅"
}

/** 修改按钮操作 */
const handleUpdate = (row) => {
    reset()
    const _restaurantId = row.restaurantId || ids.value
    getRestaurant(_restaurantId).then(response => {
        form.value = response.data
        menuList.value = response.data.menuList
        open.value = true
        title.value = "修改餐厅"
    })
}

/** 提交按钮 */
const submitForm = () => {
    proxy.$refs["restaurantRef"].validate(valid => {
        if (valid) {
            form.value.menuList = menuList.value
            if (form.value.restaurantId != null) {
                updateRestaurant(form.value).then(response => {
                    proxy.$modal.msgSuccess("修改成功")
                    open.value = false
                    getList()
                })
            } else {
                addRestaurant(form.value).then(response => {
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
    const _restaurantIds = row.restaurantId || ids.value
    proxy.$modal.confirm('是否确认删除该项数据？').then(function () {
        return delRestaurant(_restaurantIds)
    }).then(() => {
        getList()
        proxy.$modal.msgSuccess("删除成功")
    }).catch(() => {
    })
}

/** 菜单序号 */
const rowMenuIndex = ({row, rowIndex}) => {
    row.index = rowIndex + 1
}

/** 菜单添加按钮操作 */
const handleAddMenu = () => {
    let obj = {}
    obj.category = ""
    obj.name = ""
    obj.description = ""
    obj.price = ""
    menuList.value.push(obj)
}

/** 菜单删除按钮操作 */
const handleDeleteMenu = () => {
    if (checkedMenu.value.length == 0) {
        proxy.$modal.msgError("请先选择要删除的菜单数据")
    } else {
        const menus = menuList.value
        const checkedMenus = checkedMenu.value
        menuList.value = menus.filter(function (item) {
            return checkedMenus.indexOf(item.index) == -1
        })
    }
}

/** 复选框选中数据 */
const handleMenuSelectionChange = (selection) => {
    checkedMenu.value = selection.map(item => item.index)
}

/** 导出按钮操作 */
const handleExport = () => {
    proxy.download('hotel/restaurant/export', {
        ...queryParams.value
    }, `restaurant_${new Date().getTime()}.xlsx`)
}

/** 下载模板操作 */
const importTemplate = () => {
    proxy.download('hotel/restaurant/importTemplate', {}, `template_${new Date().getTime()}.xlsx`)
}

/** 导入按钮操作 */
const handleImport = () => {
    upload.value.title = "餐厅导入";
    upload.value.open = true;
}

// 文件上传中处理
const handleFileUploadProgress = (event, file, fileList) => {
    upload.value.isUploading = true;
}

// 文件上传成功处理
const handleFileSuccess = (response, file, fileList) => {
    upload.value.open = false;
    upload.value.isUploading = false;
    proxy.$refs.uploadRef.clearFiles();
    proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", {dangerouslyUseHTMLString: true});
    getList();
}

// 提交上传文件
const submitFileForm = () => {
    proxy.$refs.uploadRef.submit();
}

getList()
</script>
