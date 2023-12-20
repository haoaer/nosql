<script setup>

import { ref } from 'vue'
import {updatePassowrd} from '../api/user.js'
import { ElMessage } from 'element-plus';

const password=ref({
    old_pwd:'',
    new_pwd:'',
    re_pwd:''
})
// 表单对象
const formm=ref()
// 修改密码
const updatepasswd= async()=>{
    let result=await updatePassowrd(password.value)
    ElMessage.success(result.message?result.message:"修改成功")
    ElMessage.warning("身份失效 请重新登录")
}

// 检查密码
const checkrepassword = (rule,value,callback)=>{
    if(value !== password.value.new_pwd){
        callback(new Error("两次密码不一致"))
    }else{
        callback()
    }
}
// 校验规则
const rules=  {
    old_pwd : [
        { required: true, message:"请输入用户名", trigger:'blur'},
        { min: 5, max:16 , message:"请输入5-16位字符",trigger:'blur'}
    ],
    new_pwd : [
        { required: true,message:"请输入密码", trigger:'blur'},
        { min: 5, max:16,message:"请输入5-16位字符",trigger:'blur'}
    ],
    re_pwd : [
        { validator:checkrepassword,trigger:'blur'}
    ]
}


// 提交修改
const submit=()=>{
    formm.value.validate((valid) => {
    if(!valid){
             ElMessage.warning("填写的信息格式不正确") 
    }
    else{
        //单独写在一个方法里，因为要使用异步中断
        updatepasswd();
    }
    })
}

</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>修改密码</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form  ref="formm" :rules="rules"  :model="password" label-width="100px" size="large">
                    <el-form-item label="原密码" prop="old_pwd" >
                        <el-input v-model="password.old_pwd" onkeyup="this.value=this.value.replace(/[, ，]/g,'')"></el-input>
                    </el-form-item>
                    <el-form-item  label="新密码" prop="new_pwd">
                        <el-input v-model="password.new_pwd" onkeyup="this.value=this.value.replace(/[, ，]/g,'')"></el-input>
                    </el-form-item>
                    <el-form-item  label="重复密码" prop="re_pwd">
                        <el-input v-model="password.re_pwd" onkeyup="this.value=this.value.replace(/[, ，]/g,'')"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submit" >提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>


    