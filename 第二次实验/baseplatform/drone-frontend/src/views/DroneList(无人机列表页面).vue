<template>
  <div class="page-container">
    <div class="container">
      <div class="page-header">
        <h1>无人机管理系统</h1>
        <p>管理所有无人机设备信息</p>
      </div>

      <div class="search-card">
        <div class="search-form">
          <input 
            type="text" 
            class="search-input" 
            v-model="searchKeyword" 
            placeholder="搜索无人机名称、型号或制造商..."
            @keyup.enter="searchDrones"
          >
          <button class="btn btn-primary" @click="searchDrones">
            <i class="fas fa-search"></i> 搜索
          </button>
          <button class="btn btn-outline" @click="resetSearch">
            <i class="fas fa-refresh"></i> 重置
          </button>
          <button class="btn btn-add" @click="goToAdd">
            <i class="fas fa-plus"></i> 添加无人机
          </button>
        </div>
      </div>

      <div class="table-card">
        <table class="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>名称</th>
              <th>型号</th>
              <th>制造商</th>
              <th>序列号</th>
              <th>飞行时间</th>
              <th>状态</th>
              <th>创建时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="drones.length === 0">
              <td colspan="9" class="empty-row">
                <div class="empty-state">
                  <i class="fas fa-plane"></i>
                  <p>暂无无人机数据</p>
                  <button class="btn btn-primary" @click="goToAdd">
                    <i class="fas fa-plus"></i> 添加第一架无人机
                  </button>
                </div>
              </td>
            </tr>
            <tr v-for="drone in drones" :key="drone.id" v-else>
              <td>{{ drone.id }}</td>
              <td>{{ drone.name }}</td>
              <td>{{ drone.model }}</td>
              <td>{{ drone.manufacturer }}</td>
              <td>{{ drone.serialNumber }}</td>
              <td>{{ drone.flightTime }} 小时</td>
              <td>
                <span :class="['status-badge', getStatusClass(drone.status)]">
                  {{ drone.status }}
                </span>
              </td>
              <td>{{ formatDate(drone.createdDate) }}</td>
              <td>
                <div class="action-buttons">
                  <button class="btn-action btn-edit" @click="goToEdit(drone.id)">
                    <i class="fas fa-edit"></i>
                    <span>编辑</span>
                  </button>
                  <button class="btn-action btn-delete" @click="confirmDelete(drone.id)">
                    <i class="fas fa-trash"></i>
                    <span>删除</span>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div v-if="showDeleteModal" class="modal-overlay" @click="showDeleteModal = false">
        <div class="modal-content" @click.stop>
          <div class="modal-header">
            <h3><i class="fas fa-exclamation-triangle"></i> 确认删除</h3>
          </div>
          <div class="modal-body">
            <p>确定要删除这架无人机吗？此操作不可撤销</p>
          </div>
          <div class="modal-footer">
            <button class="btn btn-cancel" @click="showDeleteModal = false">取消</button>
            <button class="btn btn-confirm" @click="deleteDrone">确认删除</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// 导入 Vue 3 的 ref 响应式函数和 onMounted 生命周期钩子
import { ref, onMounted } from 'vue'
// 导入 Vue Router 的 useRouter 组合式函数
import { useRouter } from 'vue-router'
// 导入无人机相关的 API 函数
import { getDroneList, searchDronesAPI, deleteDroneAPI } from '@/api/drone(无人机接口).js'

// 获取路由实例
const router = useRouter()
// 无人机列表数据（响应式）
const drones = ref([])
// 搜索关键词
const searchKeyword = ref('')
// 是否显示删除确认弹窗
const showDeleteModal = ref(false)
// 要删除的无人机 ID
const deleteId = ref(null)

// 加载无人机列表
const loadDrones = async () => {
  try {
    // 调用 API 获取无人机列表
    const response = await getDroneList()
    // 将响应数据赋值给 drones
    drones.value = response.data || []
  } catch (error) {
    // 捕获并打印错误
    console.error('加载无人机列表失败', error)
  }
}

// 搜索无人机
const searchDrones = async () => {
  // 如果关键词为空，重新加载全部列表
  if (!searchKeyword.value.trim()) {
    loadDrones()
    return
  }
  try {
    // 调用搜索 API
    const response = await searchDronesAPI(searchKeyword.value)
    drones.value = response.data || []
  } catch (error) {
    console.error('搜索失败:', error)
  }
}

// 重置搜索
const resetSearch = () => {
  // 清空搜索关键词
  searchKeyword.value = ''
  // 重新加载全部列表
  loadDrones()
}

// 跳转到添加页面
const goToAdd = () => {
  router.push('/add')
}

// 跳转到编辑页面（带参数 id）
const goToEdit = (id) => {
  router.push(`/edit/${id}`)
}

// 确认删除（显示弹窗）
const confirmDelete = (id) => {
  // 保存要删除的 ID
  deleteId.value = id
  // 显示删除确认弹窗
  showDeleteModal.value = true
}

// 执行删除操作
const deleteDrone = async () => {
  try {
    // 调用删除 API
    await deleteDroneAPI(deleteId.value)
    // 关闭弹窗
    showDeleteModal.value = false
    // 清空删除 ID
    deleteId.value = null
    // 重新加载列表
    await loadDrones()
  } catch (error) {
    console.error('删除失败:', error)
  }
}

// 根据状态获取对应的 CSS 类名
const getStatusClass = (status) => {
  const statusMap = {
    '运行中': 'status-running',      // 绿色
    '维护中': 'status-maintenance',  // 黄色
    '已退役': 'status-retired',      // 灰色
    '已损坏': 'status-damaged'       // 红色
  }
  return statusMap[status] || 'status-default'
}

// 格式化日期字符串
const formatDate = (dateString) => {
  if (!dateString) return '-'
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN')
}

// 组件挂载时加载无人机列表
onMounted(() => {
  loadDrones()
})
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.page-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #fafafa 0%, #f5f5f5 50%, #f0f0f0 100%);
  background-attachment: fixed;
}

.container {
  max-width: 1200px;
  padding: 48px 24px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 40px;
  text-align: center;
}

.page-header h1 {
  font-size: 42px;
  font-weight: 700;
  letter-spacing: -0.5px;
  color: #1a1a2e;
  margin-bottom: 8px;
  position: relative;
}

.page-header h1::after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, #4a90d9 0%, #7a6a9a 100%);
  border-radius: 2px;
}

.page-header p {
  font-size: 15px;
  color: #6b7280;
  letter-spacing: 0.3px;
  margin-top: 20px;
}

.search-card {
  background: #ffffff;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  border: 1px solid rgba(0, 0, 0, 0.04);
}

.search-form {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
}

.search-input {
  flex: 1;
  min-width: 200px;
  padding: 14px 18px;
  background: #f9fafb;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  font-size: 14px;
  color: #1a1a2e;
  transition: all 0.2s ease;
}

.search-input:focus {
  outline: none;
  border-color: #4a90d9;
  box-shadow: 0 0 0 3px rgba(74, 144, 217, 0.1);
  background: #ffffff;
}

.search-input::placeholder {
  color: #9ca3af;
}

.btn {
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.btn-primary {
  background: linear-gradient(135deg, #00f5ff 0%, #7b2cbf 25%, #3a0ca3 50%, #4361ee 75%, #00f5ff 100%);
  background-size: 400% 400%;
  color: #ffffff;
  box-shadow: 0 0 20px rgba(0, 245, 255, 0.5), 0 4px 14px rgba(0, 0, 0, 0.2);
  animation: holographic 8s ease infinite;
}

.btn-outline {
  background: linear-gradient(135deg, #a855f7 0%, #6366f1 25%, #3b82f6 50%, #06b6d4 75%, #a855f7 100%);
  background-size: 400% 400%;
  color: #ffffff;
  box-shadow: 0 0 20px rgba(168, 85, 247, 0.5), 0 4px 14px rgba(0, 0, 0, 0.2);
  animation: holographic 7s ease infinite;
}

.btn-add {
  background: linear-gradient(135deg, #ff6b6b 0%, #feca57 25%, #ff9ff3 50%, #54a0ff 75%, #ff6b6b 100%);
  background-size: 400% 400%;
  color: #ffffff;
  box-shadow: 0 0 20px rgba(255, 107, 107, 0.5), 0 4px 14px rgba(0, 0, 0, 0.2);
  animation: holographic 6s ease infinite;
}

.btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

@keyframes holographic {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

.table-card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  border: 1px solid rgba(0, 0, 0, 0.04);
  overflow: hidden;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table thead {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.data-table th {
  padding: 16px 12px;
  text-align: left;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 0.5px;
  text-transform: uppercase;
}

.data-table tbody tr {
  border-bottom: 1px solid #f0f0f0;
  transition: background 0.15s ease;
}

.data-table tbody tr:hover {
  background: #f9fafb;
}

.data-table td {
  padding: 16px 12px;
  font-size: 14px;
  color: #1a1a2e;
}

.status-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-running {
  background: #d1fae5;
  color: #059669;
}

.status-maintenance {
  background: #fef3c7;
  color: #d97706;
}

.status-retired {
  background: #e5e7eb;
  color: #6b7280;
}

.status-damaged {
  background: #fee2e2;
  color: #dc2626;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.btn-action {
  height: 32px;
  padding: 0 12px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  transition: all 0.2s ease;
  font-size: 13px;
}

.btn-edit {
  background: #3b82f6;
  color: white;
}

.btn-edit:hover {
  background: #2563eb;
  transform: translateY(-1px);
}

.btn-delete {
  background: #ef4444;
  color: white;
}

.btn-delete:hover {
  background: #dc2626;
  transform: translateY(-1px);
}

.empty-row {
  text-align: center;
  padding: 60px 20px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.empty-state i {
  font-size: 48px;
  color: #d1d5db;
}

.empty-state p {
  font-size: 16px;
  color: #6b7280;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  max-width: 400px;
  width: 90%;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.modal-header {
  padding: 24px 24px 16px;
  border-bottom: 1px solid #e5e7eb;
}

.modal-header h3 {
  font-size: 18px;
  color: #1a1a2e;
  display: flex;
  align-items: center;
  gap: 10px;
}

.modal-header h3 i {
  color: #f59e0b;
}

.modal-body {
  padding: 24px;
}

.modal-body p {
  font-size: 14px;
  color: #6b7280;
  line-height: 1.6;
}

.modal-footer {
  padding: 16px 24px 24px;
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.btn-cancel {
  padding: 10px 20px;
  background: #e5e7eb;
  color: #6b7280;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-cancel:hover {
  background: #d1d5db;
}

.btn-confirm {
  padding: 10px 20px;
  background: #ef4444;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-confirm:hover {
  background: #dc2626;
}

@media (max-width: 768px) {
  .search-form {
    flex-direction: column;
  }
  
  .search-input {
    width: 100%;
  }
  
  .btn {
    width: 100%;
    justify-content: center;
  }
  
  .data-table {
    font-size: 12px;
  }
  
  .data-table th,
  .data-table td {
    padding: 12px 8px;
  }
}
</style>

