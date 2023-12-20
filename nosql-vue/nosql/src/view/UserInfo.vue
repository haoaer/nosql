<script setup>
import { ref } from 'vue'
import { useUserInfoStore } from '../store/userinfo.js';
import {updateUserInfo} from '../api/user.js'
import { ElMessage } from 'element-plus';
const userinfostore=useUserInfoStore()
//  让数据解构出来
const userInfo = ref({...userinfostore.info})

const rules = {
    nickname: [
        { required: true, message: '请输入用户昵称', trigger: 'blur' },
        {   pattern: /^\S{2,10}$/, message: '昵称必须是2-10位的非空字符串',
            rigger: 'blur'
        }
    ],
    email: [
        { required: true, message: '请输入用户邮箱', trigger: 'blur' },
        { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
    ]
}

// 更新用户信息
const updateuserinfo= async ()=>{
     let result= await updateUserInfo(userInfo.value)
     ElMessage.success(result.message?result.message:'修改成功')
     userinfostore.info.nickname=userInfo.value.nickname
     userinfostore.info.email=userInfo.value.email

}
// 表单对象
const submitform= ref(null)

// 表单校验
const submit= ()=>{
    submitform.value.validate((valid) => {
        if (!valid) {
            ElMessage.warning("请填写完善的信息")
        }
        else{
            updateuserinfo()

        }})
}



</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>基本资料</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form ref="submitform" :model="userInfo" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="登录名称">
                        <el-input v-model="userInfo.username" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="用户昵称" prop="nickname">
                        <el-input v-model="userInfo.nickname"></el-input>
                    </el-form-item>
                    <el-form-item label="用户邮箱" prop="email">
                        <el-input v-model="userInfo.email"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submit">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>