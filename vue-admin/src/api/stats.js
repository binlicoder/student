import request from '@/utils/request'

export const getStats = () => {
  return request({
    url: '/stats',
    method: 'get'
  })
}
