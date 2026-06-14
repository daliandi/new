// 导入 Vue 3 的 createApp 函数，用于创建应用实例
import { createApp } from 'vue'
// 导入根组件 App.vue
import App from './App.vue'
// 导入路由配置文件
import router from './router/index(路由配置).js'
// 导入 Bootstrap CSS 样式
import 'bootstrap/dist/css/bootstrap.min.css'
// 导入 FontAwesome 图标库
import '@fortawesome/fontawesome-free/css/all.min.css'
// 导入 jQuery（Bootstrap 依赖）
import 'jquery/dist/jquery.min.js'
// 导入 Popper.js（Bootstrap 依赖，用于弹出层定位）
import 'popper.js/dist/umd/popper.min.js'
// 导入 Bootstrap JavaScript
import 'bootstrap/dist/js/bootstrap.min.js'

// 创建 Vue 应用实例
const app = createApp(App)
// 安装路由插件
app.use(router)
// 将应用挂载到 HTML 中的 #app 元素上
app.mount('#app')
