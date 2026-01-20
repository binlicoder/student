import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/views/Layout.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页' }
      },
      {
        path: '/students',
        name: 'Students',
        component: () => import('@/views/Students.vue'),
        meta: { title: '学生管理' }
      },
      {
        path: '/teachers',
        name: 'Teachers',
        component: () => import('@/views/Teachers.vue'),
        meta: { title: '教师管理' }
      },
      {
        path: '/classes',
        name: 'Classes',
        component: () => import('@/views/Classes.vue'),
        meta: { title: '班级管理' }
      },
      {
        path: '/courses',
        name: 'Courses',
        component: () => import('@/views/Courses.vue'),
        meta: { title: '课程管理' }
      },
      {
        path: '/grades',
        name: 'Grades',
        component: () => import('@/views/Grades.vue'),
        meta: { title: '成绩管理' }
      },
      {
        path: '/course-selection',
        name: 'CourseSelection',
        component: () => import('@/views/CourseSelection.vue'),
        meta: { title: '选课管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router
