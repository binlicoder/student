import request from '@/utils/request'

export const getTeachers = (params) => {
  return request({
    url: '/teachers',
    method: 'get',
    params
  })
}

export const getTeacherById = (id) => {
  return request({
    url: `/teachers/${id}`,
    method: 'get'
  })
}

export const createTeacher = (data) => {
  return request({
    url: '/teachers',
    method: 'post',
    data
  })
}

export const updateTeacher = (id, data) => {
  return request({
    url: `/teachers/${id}`,
    method: 'put',
    data
  })
}

export const deleteTeacher = (id) => {
  return request({
    url: `/teachers/${id}`,
    method: 'delete'
  })
}
