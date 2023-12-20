import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import locale from 'element-plus/dist/locale/zh-cn.js'
import router from '@/router'
import { createPinia } from 'pinia'
import { createPersistedState } from 'pinia-persistedstate-plugin'
const app = createApp(App)
const persist = createPersistedState()
const pinia = createPinia()  // 全局使用pinia持久化
pinia.use(persist)
app.use(ElementPlus, { locale })  // element组件中文
app.use(router)
app.use(pinia)
app.mount('#app')
