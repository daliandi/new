<template>
  <div class="container-fluid">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
      <div class="container-fluid">
        <span class="navbar-brand"><i class="glyphicon glyphicon-plane"></i> 无人机信息管理系统</span>
        <div class="navbar-nav ml-auto">
          <button class="btn btn-light text-primary" @click="goBack">返回列表</button>
        </div>
      </div>
    </nav>

    <div class="container mt-3" style="max-width: 760px;">
      <div class="card">
        <div class="card-header bg-primary text-white">新增无人机信息</div>
        <div class="card-body">
          <div class="mb-3">
            <label class="form-label">AI 自动生成属性</label>
            <div class="input-group">
              <input
                type="text"
                class="form-control"
                v-model="aiModel"
                placeholder="输入型号描述，如：工业级固定翼"
              />
              <button class="btn btn-info" @click="handleAiGenerate">
                <i class="glyphicon glyphicon-flash"></i> AI 生成
              </button>
            </div>
          </div>
          <hr />
          <form @submit.prevent="handleSubmit">
            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label>注册编号 <span class="text-danger">*</span></label>
                  <input
                    type="text"
                    class="form-control"
                    v-model="form.uavCode"
                    required
                  />
                </div>
              </div>
              <div class="col-md-6">
                <div class="form-group">
                  <label>型号 <span class="text-danger">*</span></label>
                  <input
                    type="text"
                    class="form-control"
                    v-model="form.model"
                    required
                  />
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label>制造商</label>
                  <input
                    type="text"
                    class="form-control"
                    v-model="form.manufacturer"
                  />
                </div>
              </div>
              <div class="col-md-6">
                <div class="form-group">
                  <label>最大载重 (kg)</label>
                  <input
                    type="number"
                    class="form-control"
                    v-model.number="form.maxPayload"
                    step="0.1"
                    min="0"
                  />
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-4">
                <div class="form-group">
                  <label>最大飞行高度 (m)</label>
                  <input
                    type="number"
                    class="form-control"
                    v-model.number="form.maxAltitude"
                    min="0"
                  />
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <label>最大续航 (min)</label>
                  <input
                    type="number"
                    class="form-control"
                    v-model.number="form.maxFlightTime"
                    min="0"
                  />
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <label>最大速度 (m/s)</label>
                  <input
                    type="number"
                    class="form-control"
                    v-model.number="form.maxSpeed"
                    step="0.1"
                    min="0"
                  />
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-md-4">
                <div class="form-group">
                  <label>翼展 (cm)</label>
                  <input
                    type="number"
                    class="form-control"
                    v-model.number="form.wingspan"
                    step="0.1"
                    min="0"
                  />
                </div>
              </div>
              <div class="col-md-4">
                <div class="form-group">
                  <label>自重 (kg)</label>
                  <input
                    type="number"
                    class="form-control"
                    v-model.number="form.weight"
                    step="0.01"
                    min="0"
                  />
                </div>
              </div>
            </div>
            <div class="form-group">
              <label>备注</label>
              <textarea
                class="form-control"
                rows="2"
                v-model="form.remark"
              ></textarea>
            </div>
            <div class="text-right">
              <button type="button" class="btn btn-default" @click="goBack">取消</button>
              <button type="submit" class="btn btn-primary ml-2">保存</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { createUav, aiGenerate } from '../api/uav'

const router = useRouter()
const aiModel = ref('')
const form = reactive({
  uavCode: '',
  model: '',
  manufacturer: '',
  maxPayload: null,
  maxAltitude: null,
  maxFlightTime: null,
  maxSpeed: null,
  wingspan: null,
  weight: null,
  remark: ''
})

const handleAiGenerate = async () => {
  if (!aiModel.value.trim()) {
    alert('请输入型号描述')
    return
  }
  const res = await aiGenerate(aiModel.value)
  const data = res.data
  form.model = data.model || aiModel.value
  form.manufacturer = data.manufacturer || ''
  form.maxPayload = data.maxPayload || null
  form.maxAltitude = data.maxAltitude || null
  form.maxFlightTime = data.maxFlightTime || null
  form.maxSpeed = data.maxSpeed || null
  form.wingspan = data.wingspan || null
  form.weight = data.weight || null
  form.remark = data.remark || ''
}

const handleSubmit = async () => {
  await createUav(form)
  alert('新增成功')
  router.push('/')
}

const goBack = () => {
  router.push('/')
}
</script>