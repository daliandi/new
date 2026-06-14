<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h1>无人机管理系统</h1>
        <p>登录系统管理无人机设备</p>
      </div>

      <div v-if="error" class="error-message">
        <i class="fas fa-exclamation-circle"></i> {{ error }}
      </div>

      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <input 
            type="text" 
            class="form-control" 
            v-model="username" 
            placeholder="用户名" 
            required
          >
        </div>

        <div class="form-group">
          <input 
            type="password" 
            class="form-control" 
            v-model="password" 
            placeholder="密码" 
            required
          >
        </div>

        <button type="submit" class="btn-login" :disabled="loading">
          <span v-if="loading">登录中...</span>
          <span v-else>登录</span>
        </button>
      </form>

      <div class="credentials-hint">
        <p>默认账号: admin</p>
        <p>默认密码: admin</p>
      </div>
    </div>
  </div>
</template>

<script setup>
// 导入 Vue 3 的 ref 响应式函数
import { ref } from 'vue'
// 导入 Vue Router 的 useRouter 组合式函数
import { useRouter } from 'vue-router'
// 导入登录 API
import { login } from '@/api/drone(无人机接口).js'

// 获取路由实例
const router = useRouter()
// 用户名输入框绑定的响应式变量
const username = ref('')
// 密码输入框绑定的响应式变量
const password = ref('')
// 错误提示信息
const error = ref('')
// 登录按钮加载状态
const loading = ref(false)

// 登录处理函数（异步）
const handleLogin = async () => {
  // 验证用户名是否为空
  if (!username.value.trim()) {
    error.value = '请输入用户名'
    return
  }
  // 验证密码是否为空
  if (!password.value.trim()) {
    error.value = '请输入密码'
    return
  }
  
  // 清空之前的错误信息
  error.value = ''
  // 设置加载状态为 true（按钮变为禁用状态）
  loading.value = true
  
  try {
    // 调用登录 API
    const response = await login(username.value, password.value)
    // 检查响应是否成功（状态码为 200）
    if (response && response.code === 200) {
      // 保存登录状态到 sessionStorage
      sessionStorage.setItem('loggedIn', 'true')
      sessionStorage.setItem('username', username.value)
      // 跳转到首页
      router.push('/')
    } else {
      // 登录失败，显示错误信息
      error.value = response?.msg || '登录失败，请检查用户名和密码'
    }
  } catch (err) {
    // 捕获异常并处理
    console.error('登录错误:', err)
    // 如果有响应对象（HTTP 错误）
    if (err.response) {
      const status = err.response.status
      // 根据不同状态码显示不同错误信息
      if (status === 403) {
        error.value = '没有权限访问此资源，请检查后端配置'
      } else if (status === 404) {
        error.value = '登录接口未找到，请检查后端服务是否正常运行'
      } else if (status === 500) {
        error.value = '服务器内部错误，请稍后重试'
      } else if (status === 401) {
        error.value = '用户名或密码错误，请重新输入'
      } else {
        error.value = `请求错误，状态码: ${status}`
      }
    } else if (err.message) {
      // 网络错误等情况
      if (err.message.includes('Network Error')) {
        error.value = '网络连接失败，请检查网络或后端服务'
      } else if (err.message.includes('timeout')) {
        error.value = '请求超时，请稍后重试'
      } else {
        error.value = err.message
      }
    } else {
      error.value = '登录失败，请检查用户名和密码'
    }
  } finally {
    // 无论成功还是失败，都取消加载状态
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  margin: 0;
  padding: 20px;
}

.login-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  padding: 40px;
  width: 100%;
  max-width: 400px;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h1 {
  font-size: 24px;
  color: #1f2937;
  margin-bottom: 8px;
}

.login-header p {
  color: #6b7280;
  font-size: 14px;
}

.form-group {
  margin-bottom: 20px;
}

.form-control {
  width: 100%;
  height: 48px;
  border-radius: 10px;
  border: 2px solid #e5e7eb;
  padding: 0 16px;
  font-size: 14px;
  transition: all 0.2s ease;
}

.form-control:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.btn-login {
  width: 100%;
  height: 48px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-login:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.btn-login:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.error-message {
  background: #fee2e2;
  color: #dc2626;
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 20px;
  font-size: 13px;
}

.credentials-hint {
  text-align: center;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #e5e7eb;
}

.credentials-hint p {
  color: #9ca3af;
  font-size: 12px;
  margin: 4px 0;
}
</style>

