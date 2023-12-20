
<!-- 暂时复用articleManage组件 -->
<script setup>

import { ref } from 'vue'
import { getLike,changelike, getScanCount,getALL,getALLcategory ,getDetailArticle} from '../api/article.js'

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



const getcategorylist = async () => {
    let result = await getALLcategory()
    categorys.value = result.data
}

getcategorylist()

const getarticlelist = async () => {
    let parm = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        categoryId: categoryId.value ? categoryId.value : null,
    }
    let result = await getALL(parm)
    articles.value = result.data.items
    total.value = result.data.totla // 因为后端接口的问题，这里是totla
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


//添加表单数据模型
const articleModel = ref({
    id:'',
    title: '',
    categoryId: '',
    coverImg: '',
    content: '',
    state: '',
    createUser:'',
    scancount:'',
    likecount:'',
})



const isUp=ref(false);


// 获取点赞信息
const getlike=async ()=>{
    let result =await getLike(articleModel.value.id)
    articleModel.value.likecount=result.data.like

    isUp.value=result.data.tag
  
}

const show =async (row)=>{
    let result= await getDetailArticle(row.id) 
    articleModel.value.id=result.data.id
    articleModel.value.title=result.data.title
    articleModel.value.categoryId=result.data.categoryId
    articleModel.value.coverImg=result.data.coverImg
    articleModel.value.content=result.data.content
    articleModel.value.state=result.data.state
    articleModel.value.createUser=result.data.createUser
    result=await getScanCount(row.id)
    articleModel.value.scancount=result.data
    await getlike()

}



const handleClick= async()=>{
    await changelike(articleModel.value.id)
    // isUp.value=!isUp.value;
    await getlike();
    
}


const dialogVisible=ref(false);




</script>
<template>
    <el-card class="page-container">
       
        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="文章分类：">
                <el-select placeholder="请选择" v-model="categoryId">
                    <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                        <!--这个value和v上面的v-model绑定在一起 -->
                    </el-option>
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
            <el-table-column label="查看" width="100">
                <template #default="{ row }">
                    <el-button type="primary" @click=" dialogVisible = true; show(row)">查看文章</el-button>
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


    <el-dialog v-model="dialogVisible" width="800px"  top="30px" append-to-body class="myDialogClass" >
        <div style="height: 800px;">
        <p style=" font-size: 35px;font-weight: bolder; margin-top: 0;margin-bottom: 20px;  text-align: center; ">
          {{ articleModel.title }}
        </p>
        <hr>
        <p>作者 id:{{ articleModel.createUser }} </p>
        <p style="margin-top: 2px;">浏览量： {{ articleModel.scancount }}</p>
    <div style="display: flex; justify-self: left">
    <div class="circle flex-h" @click="handleClick" :class="isUp?'check':''">
      <div class="img-box" :class="isUp?'img-box-check':''">
        <img v-if="isUp" src="../assets/zanhong.png" />
        <img v-else src="../assets/zanhei.png"  />
      </div>
     
    </div>
    <hr>
    <div class="support-num">{{articleModel.likecount}}</div>
  </div >

        <div style="text-align: center; margin-top: 0px;padding: 0; ">
        <img :src="articleModel.coverImg" style=" width: 200px;height: 200px;; ">
        </div>
        <p style=" font-size: 20px;">
            &nbsp; &nbsp;{{ articleModel.content }}
        </p>
    </div>
    </el-dialog>

</template>
<style lang="scss" >
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}


// 对话框圆角

.myDialogClass {
    /*scoped 模式下  background-color 不生效 直接写到style*/
    background-color: rgb(224,242,241) !important; 
    border-radius: 10px;
}

 
.el-dialog .el-dialog__header 
{
    margin: 0px !important;
    padding: 0px !important;
    padding-left: 0px !important;
}




.circle {
  position: absolute;
  left: 22px;
  top: 165px;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  cursor: pointer;
  box-shadow: 0px 0px 0px 0px rgba(223, 46, 58, 0.5)
}
.img-box {
  position: absolute;
  left: 50%;
  top: 50%;
  margin-left: -25px;
  margin-top: -25px;
  width: 50px;
  height: 50px;
  /*margin: 5px;*/
  -moz-user-select: none;
  -webkit-user-select: none;
  -ms-user-select: none;
  -khtml-user-select: none;
  user-select: none; /* 防止快速点击图片被选中，可不加，为提高体验，博主加上了这几行代码。*/
}
.support-num{
  position: absolute;
  left: 60px;
  top: 174px;
  font-size: 15px;
}

.check {
  -webkit-transition: box-shadow 0.5s;
  -moz-transition: box-shadow 0.5s;
  -o-transition: box-shadow 0.5s;
  transition: box-shadow 0.5s;
  box-shadow: 0px 0px 0px 1em rgba(226, 32, 44, 0);
}
.img-box-check {
  animation: anm 0.5s;
  -moz-animation: anm 0.5s;
  -webkit-animation: anm 0.5s;
  -o-animation: anm 0.5s;
}
@keyframes anm {
  0% {
    transform: scale(0);
    -webkit-transform: scale(0);
    -moz-transform: scale(0);
  }
  50% {
    transform: scale(1.3);
    -webkit-transform: scale(1.3);
    -moz-transform: scale(1.3);
  }
  100% {
    transform: scale(1);
    -webkit-transform: scale(1);
    -moz-transform: scale(1);
  }
}

/* 以下为处理兼容代码，可不看。*/

@-moz-keyframes anm {
  0% {
    transform: scale(0);
    -webkit-transform: scale(0);
    -moz-transform: scale(0);
  }
  50% {
    transform: scale(1.3);
    -webkit-transform: scale(1.3);
    -moz-transform: scale(1.3);
  }
  100% {
    transform: scale(1);
    -webkit-transform: scale(1);
    -moz-transform: scale(1);
  }
}

@-webkit-keyframes anm {
  0% {
    transform: scale(0);
    -webkit-transform: scale(0);
    -moz-transform: scale(0);
  }
  50% {
    transform: scale(1.3);
    -webkit-transform: scale(1.3);
    -moz-transform: scale(1.3);
  }
  100% {
    transform: scale(1);
    -webkit-transform: scale(1);
    -moz-transform: scale(1);
  }
}

@-o-keyframes anm {
  0% {
    transform: scale(0);
    -webkit-transform: scale(0);
    -moz-transform: scale(0);
  }
  50% {
    transform: scale(1.3);
    -webkit-transform: scale(1.3);
    -moz-transform: scale(1.3);
  }
  100% {
    transform: scale(1);
    -webkit-transform: scale(1);
    -moz-transform: scale(1);
  }
}


</style>