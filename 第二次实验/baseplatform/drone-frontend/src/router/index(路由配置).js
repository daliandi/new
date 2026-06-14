// 导入 Vue Router 的核心函数
import { createRouter, createWebHistory } from 'vue-router'

// 定义路由数组，配置每个页面的路径、名称和组件
const routes = [
  // 登录页面路由
  {
    path: '/login',           // 访问路径
    name: 'Login',            // 路由名称
    component: () => import('../views/Login(登录页面).vue')  // 懒加载组件
  },
  // 无人机列表页面（首页）
  {
    path: '/',
    name: 'DroneList',
    component: () => import('../views/DroneList(无人机列表页面).vue')
  },
  // 添加无人机页面
  {
    path: '/add',
    name: 'DroneAdd',
    component: () => import('../views/DroneAdd(添加无人机页面).vue')
  },
  // 编辑无人机页面（带参数 id）
  {
    path: '/edit/:id',        // :id 是动态参数，用于传递无人机ID
    name: 'DroneEdit',
    component: () => import('../views/DroneEdit(编辑无人机页面).vue')
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),  // 使用 HTML5 History 模式（不带 #）
  routes                        // 应用路由配置
})

// 全局前置守卫：路由跳转前执行
router.beforeEach((to, from, next) => {
  // 从 sessionStorage 获取登录状态
  const isLoggedIn = sessionStorage.getItem('loggedIn')
  // 如果访问的不是登录页且未登录，重定向到登录页
  if (to.path !== '/login' && !isLoggedIn) {
    next('/login')
  } else {
    // 否则正常跳转
    next()
  }
})

// 导出路由实例供 main.js 使用
export default router
