import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

request.interceptors.request.use(
  config => {
    const token = sessionStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      alert(res.msg || '请求失败')
      if (res.code === 401) {
        sessionStorage.removeItem('token')
        window.location.href = '/login'
      }
      return Promise.reject(res)
    }
    return res
  },
  error => {
    alert('网络错误')
    return Promise.reject(error)
  }
)

export default request