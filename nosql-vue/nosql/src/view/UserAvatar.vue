<script setup>
import { ref } from 'vue'
import { Plus, Upload } from '@element-plus/icons-vue'
import avatar from '@/assets/default.png'
import { useTokenStore } from '../store/token.js';
import {updateAvatar} from '../api/user.js'
import { useUserInfoStore } from '../store/userinfo.js';
import { ElMessage, ElMessageBox } from 'element-plus';
const tokenstore=useTokenStore()
const usertoken=useUserInfoStore()
const uploadRef = ref() // 绑定upload元素


//用户头像地址
const imgUrl= ref(usertoken.info.userPic)

// 上传头像封面
const uploadSucces=(result)=>{
    imgUrl.value=result.data
    console.log(imgUrl.value)
}

// 更新头像
const updateavatar= async()=>{
    console.log(imgUrl.value+'-------')
    let result= await updateAvatar(imgUrl.value)
    console.log('-----')
    console.log(result)
    ElMessage.success("上传成功")
    usertoken.info.userPic=imgUrl.value
}


</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>更换头像</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-upload 
                    ref="uploadRef"
                    class="avatar-uploader" 
                    :show-file-list="false"
                    :auto-upload="true"
                    action="/api/upload"
                    name="file"
                    :headers="{'Authorization':tokenstore.token}"
                    :on-success="uploadSucces"
                    >
                    <img v-if="imgUrl" :src="imgUrl" class="avatar" />
                    <img v-else :src="avatar" width="278" /> 
                </el-upload>
                <br />
                <el-button type="primary" :icon="Plus" size="large"  @click="uploadRef.$el.querySelector('input').click()">
                    选择图片
                </el-button>
                <el-button type="success" :icon="Upload" size="large" @click="updateavatar">
                    上传头像
                </el-button>
            </el-col>
        </el-row>
    </el-card>
</template>

<style lang="scss" scoped>
.avatar-uploader {
    :deep() {
        .avatar {
            width: 278px;
            height: 278px;
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
            width: 278px;
            height: 278px;
            text-align: center;
        }
    }
}
</style>