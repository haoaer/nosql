<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref } from 'vue'

// 存储种类列表
const categorys = ref([])
import { getCategoryList, addCategory, deleteCategory, updateCategory } from '../api/article';
import { ElMessage, ElMessageBox } from 'element-plus';

// 获取种类列表
const getcategorylist = async () => {
    let result = await getCategoryList()
    categorys.value = result.data
}


getcategorylist()

// 对话框是否出现标记
const dialogVisible = ref(false)
// 文章种类对象
const categoryModel = ref({
    categoryName: '',
    categoryAlias: '',
    id: ''
})
// 校验规则
const rules = {
    categoryName: [
        { required: true, message: '请输入分类名称', trigger: 'blur' },
    ],
    categoryAlias: [
        { required: true, message: '请输入分类别名', trigger: 'blur' },
    ]
}

// 添加文章种类
const addcategory = async () => {

    let result = await addCategory(categoryModel.value)
    ElMessage.success(result.message ? result.message : "添加成功")
    getcategorylist()
}

// 删除文章种类
const deletecategory = async (row) => {
    ElMessageBox.confirm(
        '确认删除',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning'
        }
    ).then(
        async () => {
            let result = await deleteCategory(row.id)
            ElMessage.success(result.message ? result.message : '删除成功')
            getcategorylist()
        }
    ).catch(() => {
        ElMessage('取消')
    })
}

// 清空数据
const clearData = () => {
    categoryModel.value.categoryName = '';
    categoryModel.value.categoryAlias = '';
    getCategoryList()
}


const title = ref('') //对话框标题---添加和编辑共用一个对话框

// 展示编辑分类信息
const showDialog = (row) => {
    dialogVisible.value = true
    title.value = '编辑分类' 
    categoryModel.value.categoryName = row.categoryName
    categoryModel.value.categoryAlias = row.categoryAlias
    categoryModel.value.id = row.id //传id 因为根据id修改数据
}

// 更新文章分类
const updatecategory = async () => {
    let result = await updateCategory(categoryModel.value)
    ElMessage.success(result.message ? result.message : "添加成功")
}
// 获取form对象
const formsubmit = ref(null)

// 添加文章分类
const submitcategory = () => {
    formsubmit.value.validate((valid) => {
        if (!valid) {
            ElMessage.warning("填写的信息格式不正确")
        }
        else {
            title.value == "编辑分类" ? updatecategory() : addcategory()
            dialogVisible.value = false
            clearData()
            getcategorylist()
        }
    })
}

</script>
<template>
    
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章分类</span>
                <div class="extra">
                    <el-button type="primary" @click=" dialogVisible = true; title = '添加分类'">添加分类</el-button>
                </div>
            </div>
        </template>
        <el-table :data="categorys" style="width: 100%">
            <el-table-column label="序号" width="100" type="index"> </el-table-column>
            <el-table-column label="分类名称" prop="categoryName"></el-table-column>
            <el-table-column label="分类别名" prop="categoryAlias"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deletecategory(row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
    </el-card>


    <!-- 添加分类弹窗 -->
    <el-dialog v-model="dialogVisible" :title=title width="30%" @close="clearData()">
        <el-form ref="formsubmit" :model="categoryModel" :rules="rules" label-width="100px" style="padding-right: 30px">
            <el-form-item label="分类名称" prop="categoryName">
                <el-input v-model="categoryModel.categoryName" minlength="1" maxlength="10"
                    onkeyup="this.value=this.value.replace(/[, ，]/g,'')"></el-input>
            </el-form-item>
            <el-form-item label="分类别名" prop="categoryAlias">
                <el-input v-model="categoryModel.categoryAlias" minlength="1" maxlength="8"
                    onkeyup="this.value=this.value.replace(/[, ，]/g,'')"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisible = false; clearData()">取消</el-button>
                <el-button type="primary" @click="submitcategory()"> 确认 </el-button>
            </span>
        </template>
    </el-dialog>
</template>



<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}
</style>