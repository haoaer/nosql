
import request from '@/util/request.js'

// 注册
export const userRegisterService=( registerData )=>{

    //原始registerData是json格式
    const params=new URLSearchParams()
    for (let key in registerData){
        params.append(key,registerData[key])
    }

    return request.post('/user/register',params)
}

// 登录
export const userLoginService=( loginData)=>{
     const params=new URLSearchParams()
    for (let key in loginData){
        params.append(key,loginData[key])
    }
    return request.post('/user/login',params)
}

// 获取用户信息
export const getUserinfo=()=>{
    return request.get("/user/userInfo")
}

// 更新用户信息
export const updateUserInfo=(userData)=>{
    return request.put("/user/update",userData)
}

// 更新用户头像
export const updateAvatar =(avatarurl)=>{
    let params = new URLSearchParams();
    params.append('avatarUrl',avatarurl)
    return request.patch('/user/updateAvatar',params)
}

// 更新用户密码
export const updatePassowrd=(password)=>{
    return request.patch("/user/updatePwd",password)
   
}




