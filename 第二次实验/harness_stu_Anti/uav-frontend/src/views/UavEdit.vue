<template>
  <div class="container-fluid">
    <nav class="navbar navbar-expand-lg navbar-dark bg-warning">
      <div class="container-fluid">
        <span class="navbar-brand"><i class="glyphicon glyphicon-plane"></i> 无人机信息管理系统</span>
        <div class="navbar-nav ml-auto">
          <button class="btn btn-light text-warning" @click="goBack">返回列表</button>
        </div>
      </div>
    </nav>

    <div class="container mt-3" style="max-width: 760px;">
      <div class="card">
        <div class="card-header bg-warning text-white">编辑无人机信息 &nbsp;
          <small>{{ '编号：' + form.uavCode }}</small>
        </div>
        <div class="card-body">
          <form @submit.prevent="handleSubmit">
            <input type="hidden" v-model="form.id" />
            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label>注册编号</label>
                  <input
                    type="text"
                    class="form-control"
                    v-model="form.uavCode"
                    disabled
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
              <div class="col-md-4">
                <div class="form-group">
                  <label>状态</label>
                  <select class="form-control" v-model.number="form.status">
                    <option :value="1">正常</option>
                    <option :value="0">停用</option>
                  </select>
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
              <button type="submit" class="btn btn-warning ml-2">保存修改</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getUav, updateUav } from '../api/uav'

const router = useRouter()
const route = useRoute()
const form = reactive({
  id: null,
  uavCode: '',
  model: '',
  manufacturer: '',
  maxPayload: null,
  maxAltitude: null,
  maxFlightTime: null,
  maxSpeed: null,
  wingspan: null,
  weight: null,
  status: 1,
  remark: ''
})

const loadData = async () => {
  const res = await getUav(route.params.id)
  const data = res.data
  form.id = data.id
  form.uavCode = data.uavCode
  form.model = data.model
  form.manufacturer = data.manufacturer || ''
  form.maxPayload = data.maxPayload || null
  form.maxAltitude = data.maxAltitude || null
  form.maxFlightTime = data.maxFlightTime || null
  form.maxSpeed = data.maxSpeed || null
  form.wingspan = data.wingspan || null
  form.weight = data.weight || null
  form.status = data.status || 1
  form.remark = data.remark || ''
}

const handleSubmit = async () => {
  await updateUav(form.id, form)
  alert('修改成功')
  router.push('/')
}

const goBack = () => {
  router.push('/')
}

onMounted(() => {
  loadData()
})
</script>