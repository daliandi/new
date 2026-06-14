// 导入封装好的 axios 请求工具
import request from '../utils/request(请求工具).js'

// 用户登录 API
// 参数：username - 用户名，password - 密码
export function login(username, password) {
  return request({
    url: '/api/login',      // 请求地址
    method: 'post',         // 请求方法
    data: { username, password }  // 请求体数据
  })
}

// 获取无人机列表 API
export function getDroneList() {
  return request({
    url: '/drones/api',     // 请求地址
    method: 'get'           // GET 请求
  })
}

// 获取无人机列表（别名函数）
export function getDrones() {
  return getDroneList()
}

// 根据ID获取单个无人机详情
// 参数：id - 无人机ID
export function getDroneById(id) {
  return request({
    url: `/drones/api/${id}`,  // 动态拼接ID
    method: 'get'
  })
}

// 根据ID获取无人机（别名函数）
export function getDrone(id) {
  return getDroneById(id)
}

// 添加新无人机
// 参数：data - 无人机数据对象
export function addDrone(data) {
  return request({
    url: '/drones/api',
    method: 'post',         // POST 请求
    data                    // 请求体数据
  })
}

// 保存无人机（别名函数）
export function saveDrone(data) {
  return addDrone(data)
}

// 更新无人机信息
// 参数：id - 无人机ID，data - 更新的数据
export function updateDrone(id, data) {
  return request({
    url: `/drones/api/${id}`,
    method: 'put',          // PUT 请求
    data
  })
}

// 删除无人机（内部函数）
// 参数：id - 要删除的无人机ID
export function deleteDroneAPI(id) {
  return request({
    url: `/drones/api/${id}`,
    method: 'delete'        // DELETE 请求
  })
}

// 删除无人机（对外暴露的函数）
export function deleteDrone(id) {
  return deleteDroneAPI(id)
}

// 搜索无人机
// 参数：keyword - 搜索关键词
export function searchDronesAPI(keyword) {
  return request({
    url: '/drones/api/search',
    method: 'get',
    params: { keyword }     // URL 参数
  })
}

// AI 生成随机无人机属性
export function generateDrone() {
  return request({
    url: '/drones/api/generate',
    method: 'post'
  })
}