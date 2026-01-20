import request from '@/utils/request'

export const getCourseSelections = (params) => {
  return request({
    url: '/course-selections',
    method: 'get',
    params
  })
}

export const createCourseSelection = (data) => {
  return request({
    url: '/course-selections',
    method: 'post',
    data
  })
}

export const deleteCourseSelection = (id) => {
  return request({
    url: `/course-selections/${id}`,
    method: 'delete'
  })
}

export const getStudentSelections = (studentId) => {
  return request({
    url: `/course-selections/student/${studentId}`,
    method: 'get'
  })
}
