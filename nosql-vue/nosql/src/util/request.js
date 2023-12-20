import axios from "axios";

import {ElMessage} from 'element-plus'
// 定义公共变量 前缀
const baseURL ="/api"

//创建实例
const instance = axios.create({baseURL})

import { useTokenStore } from "../store/token";
import rout from "@/router";
// 请求拦截器
instance.interceptors.request.use(
    (config)=>{
        const tokenstore=useTokenStore()
        // 如果有token 则为请求添加请求头
        if(tokenstore.token){
            config.headers.Authorization=tokenstore.token
        }
        return config
    },
    (err)=>{
        Promise.reject(err)
    }
)


// 响应拦截器
instance.interceptors.response.use(
    result=>{
        if(result.data.code===0) //操作成功         
            return result.data;
            
        ElMessage.error(result.data.message?result.data.message:'服务异常')
        //异步的操作状态转换为失败
        return Promise.reject(result.data);
      
    },
    err=>{
        if(err.response.status===401){
            //401状态码 未登录
            rout.push('/login')
        }else{
            ElMessage.error('服务异常')
        }
       return Promise.reject(err);
    }
)

export default instance;



