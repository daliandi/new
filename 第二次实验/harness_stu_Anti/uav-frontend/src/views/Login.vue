<template>
  <div class="container mt-5">
    <div class="row justify-content-center">
      <div class="col-md-4">
        <div class="card">
          <div class="card-header bg-primary text-white text-center">
            <h3><i class="glyphicon glyphicon-plane"></i> 无人机信息管理系统</h3>
          </div>
          <div class="card-body">
            <form @submit.prevent="handleLogin">
              <div class="form-group">
                <label for="username">用户名</label>
                <input
                  type="text"
                  class="form-control"
                  id="username"
                  v-model="form.username"
                  placeholder="请输入用户名"
                  required
                />
              </div>
              <div class="form-group">
                <label for="password">密码</label>
                <input
                  type="password"
                  class="form-control"
                  id="password"
                  v-model="form.password"
                  placeholder="请输入密码"
                  required
                />
              </div>
              <div v-if="error" class="alert alert-danger">{{ error }}</div>
              <button type="submit" class="btn btn-primary btn-block mt-3">
                登录
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../api/uav'

const router = useRouter()
const form = reactive({
  username: '',
  password: ''
})
const error = ref('')

const handleLogin = async () => {
  try {
    const res = await login(form)
    if (res.code === 200) {
      sessionStorage.setItem('token', 'logged')
      router.push('/')
    }
  } catch (e) {
    error.value = '用户名或密码错误'
  }
}
</script>