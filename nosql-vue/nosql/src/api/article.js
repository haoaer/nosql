import request from '@/util/request.js'
import { useTokenStore } from '../store/token'

//获取自己的种类列表
export const getCategoryList=()=>{
    const tokenstore=useTokenStore()
    return request.get("/category")

}

//获取所有人的种类列表
export const getALLcategory=()=>{
    return request.get("/category/all")
}

//添加自己的种类列表
export const addCategory=(CategoryData)=>{
    return request.post("/category",CategoryData)
}

//删除
export const deleteCategory=(id)=>{
    return request.delete("/category?id="+id)
}

//更新种类
export const updateCategory=(categoryData)=>{
    return request.put("/category",categoryData)
}

//获取自己发布的所有文章
export const getAllArticle=(parm)=>{
    return request.get("/article",{params:parm})

}
//获取所有人的文章
export const getALL=(parm)=>{
    return request.get("/article/all",{params:parm})
}

//删除文章
export const deleteArticle=(id)=>{
    return request.delete("/article?id="+id)
}

//添加文章
export const addArticle=(articleData)=>{
    return request.post("/article",articleData)
}

//更新文章
export const updateArticle=(articleData)=>{
    return request.put("/article",articleData)
}

// 获取文章详细
export const getDetailArticle=(id)=>{
    return request.get("/article/detail?id="+id)
}

// 获取浏览量
export const getScanCount=(id)=>{
    return request.get("/article/scan?id="+id)
}
// 获取点赞信息
export const getLike=(id)=>{
    return request.get("/article/like?id="+id)
}

// 改变点赞信息
export const changelike=(id)=>{
    return request.get("/article/changelike?id="+id)
}
