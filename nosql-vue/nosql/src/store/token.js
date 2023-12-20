// 存储token 共享给所有组件

import {defineStore } from 'pinia'
import {ref} from 'vue'

/* 
    第一个参数 名字--唯一c
    第二个参数： 函数 ， 内部定义状态内容
    返回 ：  函数
*/
export const useTokenStore = defineStore('token',()=>{
    const token = ref('')

    const setToekn=(newToken)=>{
        token.value=newToken
    }

    const removeToken =()=>{
        token.value=''
    }
    return {token,setToekn,removeToken}  
},{
    persist:true  // 持久化pinia  比如，刷新页面之后，token依然存在
}
    
);

