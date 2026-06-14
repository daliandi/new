<template>
  <div class="page-container">
    <div class="container">
      <a class="back-link" @click="goBack">
        <i class="fas fa-arrow-left"></i> 返回列表
      </a>

      <div class="form-card">
        <div class="card-header">
          <h3><i class="fas fa-plus-circle"></i> 添加无人机</h3>
        </div>

        <div class="card-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-row">
              <div class="form-group">
                <label class="form-label">
                  名称 <span>*</span>
                </label>
                <input 
                  type="text" 
                  class="form-control" 
                  v-model="drone.name" 
                  placeholder="输入无人机名称"
                  required
                >
              </div>

              <div class="form-group">
                <label class="form-label">
                  型号 <span>*</span>
                </label>
                <input 
                  type="text" 
                  class="form-control" 
                  v-model="drone.model" 
                  placeholder="输入型号"
                  required
                >
              </div>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label class="form-label">
                  制造商 <span>*</span>
                </label>
                <input 
                  type="text" 
                  class="form-control" 
                  v-model="drone.manufacturer" 
                  placeholder="输入制造商"
                  required
                >
              </div>

              <div class="form-group">
                <label class="form-label">
                  序列号 <span>*</span>
                </label>
                <input 
                  type="text" 
                  class="form-control" 
                  v-model="drone.serialNumber" 
                  placeholder="输入序列号"
                  required
                >
              </div>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label class="form-label">
                  飞行时间 <span>*</span>
                </label>
                <input 
                  type="number" 
                  class="form-control" 
                  v-model.number="drone.flightTime" 
                  placeholder="输入飞行时间"
                  min="0"
                  required
                >
              </div>

              <div class="form-group">
                <label class="form-label">
                  状态 <span>*</span>
                </label>
                <select class="form-control" v-model="drone.status" required>
                  <option value="">选择状态</option>
                  <option value="运行中">运行中</option>
                  <option value="维护中">维护中</option>
                  <option value="已退役">已退役</option>
                  <option value="已损坏">已损坏</option>
                </select>
              </div>
            </div>

            <div class="form-group">
              <label class="form-label">
                描述
              </label>
              <textarea 
                class="form-control form-textarea" 
                v-model="drone.description" 
                placeholder="输入无人机描述（可选）"
                rows="4"
              ></textarea>
            </div>

            <div class="form-actions">
              <button type="button" class="btn btn-auto-fill" @click="autoFill">
                <i class="fas fa-magic"></i> 自动填写
              </button>
              <button type="button" class="btn btn-cancel" @click="goBack">
                <i class="fas fa-times"></i> 取消
              </button>
              <button type="submit" class="btn btn-submit" :disabled="loading">
                <i v-if="loading" class="fas fa-spinner fa-spin"></i>
                <i v-else class="fas fa-check"></i>
                {{ loading ? '保存中...' : '保存' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// 导入 Vue 3 的 ref 响应式函数
import { ref } from 'vue'
// 导入 Vue Router 的 useRouter 组合式函数
import { useRouter } from 'vue-router'
// 导入添加无人机的 API
import { addDrone } from '@/api/drone(无人机接口).js'

// 获取路由实例
const router = useRouter()
// 按钮加载状态
const loading = ref(false)

// 无人机表单数据（响应式）
const drone = ref({
  name: '',              // 名称
  model: '',             // 型号
  manufacturer: '',      // 制造商
  serialNumber: '',      // 序列号
  flightTime: 0,         // 飞行时间
  status: '',            // 状态
  description: ''        // 描述（可选）
})

// 提交表单（添加无人机）
const handleSubmit = async () => {
  // 设置加载状态
  loading.value = true
  try {
    // 调用添加 API
    await addDrone(drone.value)
    // 添加成功后跳转到列表页
    router.push('/')
  } catch (error) {
    // 捕获并打印错误
    console.error('添加失败:', error)
    // 显示错误提示
    alert('添加失败，请重试')
  } finally {
    // 取消加载状态
    loading.value = false
  }
}

// 返回列表页
const goBack = () => {
  router.push('/')
}

// 预设的无人机模板数据（用于自动填写）
const droneTemplates = [
  {
    name: '大疆 Mavic 3 Pro',
    model: 'M3P-2024',
    manufacturer: '大疆创新',
    serialNumber: () => 'DJI-' + Date.now().toString(36).toUpperCase(),
    flightTime: 156,
    status: '运行中',
    description: '专业级航拍无人机，配备三摄像头系统'
  },
  {
    name: '无人机侦察型 Alpha',
    model: 'RA-7X',
    manufacturer: '中航工业',
    serialNumber: () => 'AVIC-' + Math.random().toString(36).substr(2, 8).toUpperCase(),
    flightTime: 240,
    status: '运行中',
    description: '军用级侦察无人机，长航时远程监控'
  },
  {
    name: '农业植保机 X1',
    model: 'AG-X1 Pro',
    manufacturer: '极飞科技',
    serialNumber: () => 'XF-' + Date.now().toString(36).toUpperCase(),
    flightTime: 85,
    status: '运行中',
    description: '农业植保专用无人机，高效喷洒农药'
  }
]

// 自动填写表单（随机选择一个模板）
const autoFill = () => {
  // 随机选择一个模板
  const template = droneTemplates[Math.floor(Math.random() * droneTemplates.length)]
  // 将模板数据赋值给表单
  drone.value = {
    name: template.name,
    model: template.model,
    manufacturer: template.manufacturer,
    // 序列号可能是函数，需要执行
    serialNumber: typeof template.serialNumber === 'function' ? template.serialNumber() : template.serialNumber,
    flightTime: template.flightTime,
    status: template.status,
    description: template.description
  }
}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.page-container {
  min-height: 100vh;
  background: linear-gradient(145deg, #f5f5f5 0%, #d4d4d4 25%, #c0c0c0 50%, #a8a8a8 75%, #989898 100%);
  background-attachment: fixed;
}

.container {
  max-width: 600px;
  padding: 40px 20px;
  margin: 0 auto;
}

.back-link {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: #7f8c8d;
  font-size: 14px;
  margin-bottom: 24px;
  transition: color 0.15s ease;
  cursor: pointer;
  text-decoration: none;
}

.back-link:hover {
  color: #3498db;
}

.form-card {
  background: linear-gradient(145deg, #ffffff 0%, #f5f5f5 100%);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12), inset 0 1px 0 rgba(255, 255, 255, 0.9);
  border: 1px solid rgba(255, 255, 255, 0.6);
}

.card-header {
  padding: 24px 32px;
  border-bottom: 1px solid #e8e8e8;
  background-color: #fafbfc;
}

.card-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.card-header h3 i {
  color: #3498db;
}

.card-body {
  padding: 32px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.form-group {
  margin-bottom: 24px;
}

.form-label {
  display: block;
  font-size: 13px;
  font-weight: 500;
  color: #2c3e50;
  margin-bottom: 8px;
}

.form-label span {
  color: #e74c3c;
}

.form-control {
  width: 100%;
  padding: 12px 16px;
  font-size: 14px;
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  transition: all 0.2s ease;
  background-color: #fafbfc;
  font-family: inherit;
}

.form-control:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
  background-color: white;
}

.form-textarea {
  resize: vertical;
  min-height: 100px;
}

select.form-control {
  cursor: pointer;
}

.form-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #e8e8e8;
}

.btn {
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-family: inherit;
}

.btn-auto-fill {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  color: white;
  box-shadow: 0 4px 14px rgba(17, 153, 142, 0.4);
}

.btn-auto-fill:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(17, 153, 142, 0.5);
}

.btn-cancel {
  background: #e8e8e8;
  color: #7f8c8d;
}

.btn-cancel:hover {
  background: #d1d1d1;
  color: #5a6a6a;
}

.btn-submit {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 4px 14px rgba(102, 126, 234, 0.4);
}

.btn-submit:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.btn-submit:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .btn {
    width: 100%;
    justify-content: center;
  }
}
</style>

