import request from '@/utils/request'

export const getClasses = (params) => {
  return request({
    url: '/classes',
    method: 'get',
    params
  })
}

export const getClassById = (id) => {
  return request({
    url: `/classes/${id}`,
    method: 'get'
  })
}

export const createClass = (data) => {
  return request({
    url: '/classes',
    method: 'post',
    data
  })
}

export const updateClass = (id, data) => {
  return request({
    url: `/classes/${id}`,
    method: 'put',
    data
  })
}

export const deleteClass = (id) => {
  return request({
    url: `/classes/${id}`,
    method: 'delete'
  })
}
