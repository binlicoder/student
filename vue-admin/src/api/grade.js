import request from '@/utils/request'

export const getGrades = (params) => {
  return request({
    url: '/grades',
    method: 'get',
    params
  })
}

export const getGradeById = (id) => {
  return request({
    url: `/grades/${id}`,
    method: 'get'
  })
}

export const createGrade = (data) => {
  return request({
    url: '/grades',
    method: 'post',
    data
  })
}

export const updateGrade = (id, data) => {
  return request({
    url: `/grades/${id}`,
    method: 'put',
    data
  })
}

export const deleteGrade = (id) => {
  return request({
    url: `/grades/${id}`,
    method: 'delete'
  })
}

export const getStudentGrades = (studentId) => {
  return request({
    url: `/grades/student/${studentId}`,
    method: 'get'
  })
}
