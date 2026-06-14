import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    name: 'UavList',
    component: () => import('../views/UavList.vue')
  },
  {
    path: '/add',
    name: 'UavAdd',
    component: () => import('../views/UavAdd.vue')
  },
  {
    path: '/edit/:id',
    name: 'UavEdit',
    component: () => import('../views/UavEdit.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path !== '/login') {
    const token = sessionStorage.getItem('token')
    if (!token) {
      next('/login')
      return
    }
  }
  next()
})

export default router