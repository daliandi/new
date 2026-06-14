import request from '../utils/request'

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export function listUav(params) {
  return request({
    url: '/uav/list',
    method: 'get',
    params
  })
}

export function getUav(id) {
  return request({
    url: `/uav/${id}`,
    method: 'get'
  })
}

export function createUav(data) {
  return request({
    url: '/uav',
    method: 'post',
    data
  })
}

export function updateUav(id, data) {
  return request({
    url: `/uav/${id}`,
    method: 'put',
    data
  })
}

export function deleteUav(id) {
  return request({
    url: `/uav/${id}`,
    method: 'delete'
  })
}

export function aiGenerate(model) {
  return request({
    url: '/uav/ai-generate',
    method: 'get',
    params: { model }
  })
}