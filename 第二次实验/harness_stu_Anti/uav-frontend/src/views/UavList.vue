<template>
  <div class="container-fluid">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
      <div class="container-fluid">
        <span class="navbar-brand"><i class="glyphicon glyphicon-plane"></i> 无人机信息管理系统</span>
        <div class="navbar-nav ml-auto">
          <button class="btn btn-danger" @click="handleLogout">退出</button>
        </div>
      </div>
    </nav>

    <div class="card mt-3">
      <div class="card-body">
        <form @submit.prevent="handleSearch" class="form-inline">
          <div class="form-group mr-2">
            <input
              type="text"
              class="form-control"
              v-model="query.model"
              placeholder="型号"
            />
          </div>
          <div class="form-group mr-2">
            <input
              type="text"
              class="form-control"
              v-model="query.uavCode"
              placeholder="注册编号"
            />
          </div>
          <div class="form-group mr-2">
            <select class="form-control" v-model="query.status">
              <option value="">全部状态</option>
              <option value="1">正常</option>
              <option value="0">停用</option>
            </select>
          </div>
          <button type="submit" class="btn btn-primary">
            <i class="glyphicon glyphicon-search"></i> 搜索
          </button>
          <button type="button" class="btn btn-success ml-2" @click="goAdd">
            <i class="glyphicon glyphicon-plus"></i> 新增
          </button>
        </form>
      </div>
    </div>

    <div class="card mt-3">
      <div class="card-header">无人机列表（共 {{ page.total }} 条）</div>
      <div class="card-body">
        <table class="table table-bordered table-hover">
          <thead>
            <tr class="bg-info text-white">
              <th>注册编号</th>
              <th>型号</th>
              <th>制造商</th>
              <th>最大载重(kg)</th>
              <th>最大高度(m)</th>
              <th>续航(min)</th>
              <th>状态</th>
              <th>创建时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="uav in page.rows" :key="uav.id">
              <td>
                {{ uav.uavCode }}
                <span v-if="uav.aiGenerated === 1" class="badge bg-purple ml-1">AI</span>
              </td>
              <td>{{ uav.model }}</td>
              <td>{{ uav.manufacturer || '-' }}</td>
              <td>{{ uav.maxPayload || '-' }}</td>
              <td>{{ uav.maxAltitude || '-' }}</td>
              <td>{{ uav.maxFlightTime || '-' }}</td>
              <td>
                <span :class="uav.status === 1 ? 'badge bg-success' : 'badge bg-danger'">
                  {{ uav.status === 1 ? '正常' : '停用' }}
                </span>
              </td>
              <td>{{ uav.createdAt }}</td>
              <td>
                <button class="btn btn-primary btn-sm" @click="goEdit(uav.id)">
                  <i class="glyphicon glyphicon-edit"></i> 编辑
                </button>
                <button class="btn btn-danger btn-sm ml-1" @click="handleDelete(uav.id)">
                  <i class="glyphicon glyphicon-trash"></i> 删除
                </button>
              </td>
            </tr>
            <tr v-if="page.rows.length === 0">
              <td colspan="9" class="text-center text-muted">暂无数据</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="card-footer" v-if="page.pages > 1">
        <nav class="float-right">
          <ul class="pagination">
            <li :class="{ disabled: page.pageNum === 1 }">
              <button class="page-link" @click="prevPage">上一页</button>
            </li>
            <li v-for="i in page.pages" :key="i" :class="{ active: i === page.pageNum }">
              <button class="page-link" @click="goPage(i)">{{ i }}</button>
            </li>
            <li :class="{ disabled: page.pageNum === page.pages }">
              <button class="page-link" @click="nextPage">下一页</button>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { listUav, deleteUav } from '../api/uav'

const router = useRouter()
const query = reactive({
  model: '',
  uavCode: '',
  status: '',
  pageNum: 1,
  pageSize: 10
})
const page = ref({
  total: 0,
  pages: 0,
  pageNum: 1,
  pageSize: 10,
  rows: []
})

const loadData = async () => {
  const res = await listUav(query)
  page.value = res.data
}

const handleSearch = () => {
  query.pageNum = 1
  loadData()
}

const goAdd = () => {
  router.push('/add')
}

const goEdit = (id) => {
  router.push(`/edit/${id}`)
}

const handleDelete = async (id) => {
  if (!confirm('确认删除该无人机记录？')) return
  await deleteUav(id)
  alert('删除成功')
  loadData()
}

const handleLogout = () => {
  sessionStorage.removeItem('token')
  router.push('/login')
}

const prevPage = () => {
  if (query.pageNum > 1) {
    query.pageNum--
    loadData()
  }
}

const nextPage = () => {
  if (query.pageNum < page.value.pages) {
    query.pageNum++
    loadData()
  }
}

const goPage = (num) => {
  query.pageNum = num
  loadData()
}

onMounted(() => {
  loadData()
})
</script>