<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus';
import { ref } from 'vue'
import { getAllArticle, getCategoryList, deleteArticle,addArticle,updateArticle,getDetailArticle} from '../api/article.js'
import { Plus } from '@element-plus/icons-vue'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'



//文章分类数据模型
const categorys = ref([])

//用户搜索时选中的分类id
const categoryId = ref('')

//用户搜索时选中的发布状态
const state = ref('')

//文章列表数据模型
const articles = ref([])

//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(5)//每页条数


// 总类列表
const getcategorylist = async () => {
    let result = await getCategoryList()
    categorys.value = result.data
}

getcategorylist()

// 获取文章列表
const getarticlelist = async () => {
    let parm = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        categoryId: categoryId.value ? categoryId.value : null,
        state: state.value ? state.value : null
    }
    let result = await getAllArticle(parm)
    articles.value = result.data.items
    total.value = result.data.total 
}

getarticlelist()

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    getarticlelist()
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    getarticlelist()
}


// 删除文章分类
const deletearticle = async (row) => {
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
            let result = await deleteArticle(row.id)
            ElMessage.success(result.message ? result.message : '删除成功')
            getarticlelist()
        }
    ).catch(() => {
        ElMessage('取消')
    })
}

//控制抽屉是否显示
const visibleDrawer = ref(false)
//添加表单数据模型
const articleModel = ref({
    id:'',
    title: '',
    categoryId: '',
    coverImg: '',
    content: '',
    state: ''
})

import {useTokenStore} from '../store/token.js'

const tokenstore=useTokenStore() 

// 上传文章封面
const uploadSuccess=(result)=>{
    articleModel.value.coverImg = result.data
}

// 添加文章
const addarticle=  async(state)=>{
    articleModel.value.state=state
    let result = await addArticle(articleModel.value)
    ElMessage.success(result.message ? result.message : "添加成功")
}

// 更新文章
const updatearticle= async(state)=>{
    articleModel.value.state=state
    console.log(articleModel.value)
    let result = await updateArticle(articleModel.value)
    ElMessage.success(result.message ? result.message : "添加成功")

}
// 清空数据
const clearData= ()=>{
    editorr.value.setText('')  
    //通过ref 拿到对象！！！ 记得加value
    articleModel.value.title='',
    articleModel.value.categoryId='',
    articleModel.value.coverImg='',
    articleModel.value.content='',
    articleModel.value.state=''
    
}

// 表单校验
const rules={
    
    title: [
        { required: true, message: '请输入标题', trigger: 'blur' },
    ],
    categoryId:[
        { required: true, message: '请选择分类', trigger: 'blur' },
    ],
    coverImg: [
        { required: true, message: '请选择封面', trigger: 'blur' },
    ],
    content: [
        { required: true, message: '请输入文章内容', trigger: 'blur' },
    ]
}

const title=ref('') 

// 富文本编辑器对象
const editorr=ref()

//表单对象
const formsubmit=ref(null)

// 提交修改或增加
const submit =(state)=>{
    formsubmit.value.validate((valid) => {
        if (!valid) {
            ElMessage.warning("请填写完善的信息")
        }
        else {
            title.value == "修改文章" ? updatearticle(state) : addarticle(state)
            getarticlelist()
            visibleDrawer.value = false
            clearData()
            
        }
    })
}

// 修改文章
const showmodify= async(row)=>{
    title.value='修改文章'
    let result= await getDetailArticle(row.id) 
    visibleDrawer.value=true 
    articleModel.value.id=result.data.id
    articleModel.value.title=result.data.title
    articleModel.value.categoryId=result.data.categoryId
    articleModel.value.coverImg=result.data.coverImg
    articleModel.value.content=result.data.content
    articleModel.value.state=result.data.state
    
}


</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章管理</span>
                <div class="extra">
                    <el-button type="primary" @click="visibleDrawer = true; title='添加文章'">添加文章</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="文章分类：">
                <el-select placeholder="请选择" v-model="categoryId">
                    <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                        <!--这个value和v上面的v-model绑定在一起 -->
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="发布状态：">
                <el-select placeholder="请选择" v-model="state">
                    <el-option label="已发布" value="已发布"></el-option>
                    <el-option label="草稿" value="草稿"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="getarticlelist">搜索</el-button>
                <el-button @click="categoryId = ''; state = ''">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 文章列表 -->
        <el-table :data="articles" style="width: 100%">
            <el-table-column label="文章标题" width="400" prop="title"></el-table-column>
            <el-table-column label="分类" prop="categoryName"></el-table-column>
            <el-table-column label="发表时间" prop="createTime"> </el-table-column>
            <el-table-column label="状态" prop="state"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showmodify(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deletearticle(row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
        <!-- 分页条 -->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3, 5, 10]"
            layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
    </el-card>


    <!-- 抽屉 -->
    <el-drawer v-model="visibleDrawer" :title=title direction="rtl" size="50%" @close="clearData()">
        <!-- 添加文章表单 -->
        <el-form ref="formsubmit" :model="articleModel" label-width="100px" :rules="rules">
            <el-form-item label="文章标题" prop="title">
                <el-input v-model="articleModel.title" placeholder="请输入标题"></el-input>
            </el-form-item>
            <el-form-item label="文章分类" prop="categoryId">
                <el-select placeholder="请选择" v-model="articleModel.categoryId">
                    <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="文章封面" prop="coverImg">

                <!--  ！----第三方库 要重新添加请求头
                    auto-upload:是否自动上传
                    action:服务器接口路径
                    name:设置上传文件字段名
                    headers:设置上传的请求头
                    on-sucess:设置上传的回调函数
                -->
                <el-upload class="avatar-uploader" 
                :auto-upload="true"
                :show-file-list="false"
                action="/api/upload"
                name="file"
                :headers="{'Authorization':tokenstore.token}"
                :on-success="uploadSuccess"
                >
                    <img v-if="articleModel.coverImg" :src="articleModel.coverImg" class="avatar" />
                    <el-icon v-else class="avatar-uploader-icon">
                        <Plus />
                    </el-icon>
                </el-upload>
            </el-form-item>
            <el-form-item label="文章内容" prop="content">
                <div class="editor">
                    <quill-editor  ref="editorr" theme="snow" v-model:content="articleModel.content"  content-type="text">
                    </quill-editor>
                </div>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="submit('已发布')">发布</el-button>
                <el-button type="info" @click="submit('草稿')">草稿</el-button>
            </el-form-item>
        </el-form>
    </el-drawer>
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




.avatar-uploader {
    :deep() {
        .avatar {
            width: 178px;
            height: 178px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 178px;
            height: 178px;
            text-align: center;
        }
    }
}

.editor {
    width: 100%;

    :deep(.ql-editor) {
        min-height: 200px;
    }
}
</style>