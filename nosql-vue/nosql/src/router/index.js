import Login from '@/view/Login.vue'
import Layout from '@/view/Layout.vue'
import { createRouter, createWebHistory } from 'vue-router'
import ArticleCategory from '@/view/ArticleCategory.vue'
import ArticleManage from '@/view/ArticleManage.vue'
import UserAvatar from '@/view/UserAvatar.vue'
import UserInfo from '@/view/UserInfo.vue'
import UserResetPassword from '@/view/UserResetPassword.vue'
import Blog from '@/view/Blog.vue'

const routes = [
    {
        path: '/', component: Layout, redirect: "/article/manage",
        children: [
            // ！！注意： path内容要和子组件内容一致，不然跳转会出现无法预料的错误
            { path: "/article/category", component: ArticleCategory },
            { path: "/article/manage", component: ArticleManage },
            { path: "/user/info", component: UserInfo },
            { path: "/user/avatar", component: UserAvatar },
            { path: "/user/resetPassword", component: UserResetPassword },
            { path:'/user/blog', component: Blog},

        ]
    },
    { path: '/login', component: Login },
  

]

const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

export default router




