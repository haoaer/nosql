import {defineStore } from 'pinia'
import {ref} from 'vue'

export const useUserInfoStore=defineStore('userinfo',()=>{
    const info=ref({})
    const setInfo=(newinfo)=>{
        info.value = newinfo
    }
    const removeinfo=()=>{
        info.value={}
    }
    return {info,setInfo,removeinfo}
},
{persist:true}
)
