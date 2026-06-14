// 导入 axios HTTP 客户端库
import axios from 'axios'

// 创建 axios 实例，配置全局默认参数
const request = axios.create({
  baseURL: '',           // 请求基础路径（留空表示相对路径）
  timeout: 10000,       // 请求超时时间（10秒）
  headers: {             // 默认请求头
    'Content-Type': 'application/json'  // 发送 JSON 格式数据
  }
})

// 请求拦截器：发送请求前执行
request.interceptors.request.use(
  config => {
    // 可以在这里添加请求头，比如 token
    return config
  },
  error => {
    // 请求发送失败时的处理
    return Promise.reject(error)
  }
)

// 响应拦截器：收到响应后执行
request.interceptors.response.use(
  response => {
    // 获取响应数据
    const res = response.data
    // 检查业务状态码（非 200 表示业务异常）
    if (res.code !== undefined && res.code !== 200) {
      // 弹出错误提示
      alert(res.msg || '请求失败')
      // 401 表示未登录或登录过期
      if (res.code === 401) {
        // 清除登录状态
        sessionStorage.removeItem('loggedIn')
        // 跳转到登录页
        window.location.href = '/login'
      }
      // 返回失败的 Promise
      return Promise.reject(res)
    }
    // 成功则返回响应数据
    return res
  },
  error => {
    // 网络错误或 HTTP 错误处理
    let errorMsg = '请求失败，请稍后重试'
    
    // 如果有响应对象（HTTP 错误）
    if (error.response) {
      const status = error.response.status
      // 根据不同状态码给出不同提示
      if (status === 403) {
        errorMsg = '没有权限访问此资源'
      } else if (status === 404) {
        errorMsg = '请求的资源未找到'
      } else if (status === 500) {
        errorMsg = '服务器内部错误'
      } else if (status === 401) {
        errorMsg = '未登录或登录已失效，请重新登录'
        sessionStorage.removeItem('loggedIn')
        window.location.href = '/login'
      } else {
        errorMsg = `请求错误，状态码: ${status}`
      }
    } else if (error.message) {
      // 没有响应对象（网络错误等）
      if (error.message.includes('Network Error')) {
        errorMsg = '网络连接失败，请检查网络或后端服务'
      } else if (error.message.includes('timeout')) {
        errorMsg = '请求超时，请稍后重试'
      } else {
        errorMsg = error.message
      }
    }
    
    // 弹出错误提示
    alert(errorMsg)
    // 返回失败的 Promise
    return Promise.reject(error)
  }
)

// 导出 request 实例供其他模块使用
export default request