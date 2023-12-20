<script setup>
import {
    Management,
    Promotion,
    UserFilled,
    User,
    Crop,
    EditPen,
    SwitchButton,
    CaretBottom
} from '@element-plus/icons-vue'
import avatar from '@/assets/default.png'
import { useUserInfoStore } from '../store/userinfo.js';
import { useTokenStore } from '../store/token.js';
import { getUserinfo } from '../api/user.js';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
// 获取路由
const router= useRouter()
// 获取当前用户信息
const userinfostore=useUserInfoStore()
const usetoken=useTokenStore()
const getuserinfo = async()=>{
    let result= await getUserinfo()
    userinfostore.setInfo(result.data)
} 

getuserinfo()

// 右上角小菜单栏
const handleCommand=(command)=>{
    if (command==='logout'){
        logoutinfo()
    }else{
        router.push("/user/"+command)
    }
}

//退出
const logoutinfo=()=>{
    ElMessageBox.confirm(
        '确认退出吗',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning'
        }
    ).then(
        async () => {
            userinfostore.removeinfo()
            usetoken.removeToken()
            router.push('/login')
            ElMessage.success('退出成功')
            
        }
    ).catch(() => {
    })
}


</script>

<template>
    <el-container class="layout-container">
        <!-- 左侧菜单 -->
        <el-aside width="200px">
            <div class="el-aside__logo"></div>
            <el-menu active-text-color="#ffd04b" background-color="#232323"  text-color="#fff"
                router >
                <el-menu-item index="/article/category" >
                    <el-icon>
                        <Management />
                    </el-icon>
                    <span>文章分类</span>
                </el-menu-item>
                <el-menu-item index="/article/manage" >
                    <el-icon>
                        <Promotion />
                    </el-icon>
                    <span>文章管理</span>
                </el-menu-item>
                <el-menu-item index="/user/blog" >
                    <el-icon >
                        <Promotion />
                    </el-icon>
                    <span>社区</span>
                </el-menu-item>
                <el-sub-menu >
                    <template #title>
                        <el-icon>
                            <UserFilled />
                        </el-icon>
                        <span>个人中心</span>
                    </template>
                    <el-menu-item index="/user/info">
                        <el-icon>
                            <User />
                        </el-icon>
                        <span>基本资料</span>
                    </el-menu-item>
                    <el-menu-item index="/user/avatar" >
                        <el-icon>
                            <Crop />
                        </el-icon>
                        <span>更换头像</span>
                    </el-menu-item>
                    <el-menu-item index="/user/resetPassword">
                        <el-icon>
                            <EditPen />
                        </el-icon>
                        <span>修改密码</span>
                    </el-menu-item>
                </el-sub-menu>
                
            </el-menu>
        </el-aside>
        <!-- 右侧主区域 -->
        <el-container>
            <!-- 头部区域 -->
            <el-header >
                <div>用户：<strong>{{userinfostore.info.nickname}}</strong></div>
                <el-dropdown placement="bottom-end"  @command="handleCommand">  <!--使用条目来控制-->
                    <span class="el-dropdown__box">
                        <el-avatar :src="userinfostore.info.userPic?userinfostore.info.userPic:avatar" />
                        <el-icon>
                            <CaretBottom />
                        </el-icon>
                    </span>
                  
                    <template #dropdown >
                        <el-dropdown-menu><!--使用条目来控制-->
                            <el-dropdown-item command="info" :icon="User">基本资料</el-dropdown-item>
                            <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
                            <el-dropdown-item command="resetPassword" :icon="EditPen">重置密码</el-dropdown-item>
                            <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </el-header>
            <!-- 中间区域 -->
            <el-main>
               <router-view></router-view>
            </el-main>

            <!-- 底部区域 -->
            <el-footer>大事件---2023---Created by Haoer</el-footer>

        </el-container>
    </el-container>
</template>

<style lang="scss" scoped>

.layout-container {
    height: 99.9vh;
    // border-style: solid;
   
    .el-aside {
        background-color: #232323;

        &__logo {
            height: 120px;
            background: url('@/assets/logo.png') no-repeat center / 120px auto;
        }

        .el-menu {
            border-right: none;
        }
    }

    .el-header {
        background-color: #fff;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .el-dropdown__box {
            display: flex;
            align-items: center;

            .el-icon {
                color: #999;
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #666;
    }
}
</style>