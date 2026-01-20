import request from '@/utils/request'

export const getStudents = (params) => {
  return request({
    url: '/students',
    method: 'get',
    params
  })
}

export const getStudentById = (id) => {
  return request({
    url: `/students/${id}`,
    method: 'get'
  })
}

export const createStudent = (data) => {
  return request({
    url: '/students',
    method: 'post',
    data
  })
}

export const updateStudent = (id, data) => {
  return request({
    url: `/students/${id}`,
    method: 'put',
    data
  })
}

export const deleteStudent = (id) => {
  return request({
    url: `/students/${id}`,
    method: 'delete'
  })
}
