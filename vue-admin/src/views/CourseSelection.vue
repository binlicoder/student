<template>
  <div class="selection-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>选课管理</span>
          <el-button type="primary" @click="handleAdd">添加选课</el-button>
        </div>
      </template>
      
      <div class="search-bar">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="学号">
            <el-input v-model="searchForm.studentNo" placeholder="请输入学号" clearable />
          </el-form-item>
          <el-form-item label="课程">
            <el-select v-model="searchForm.courseId" placeholder="请选择课程" clearable>
              <el-option
                v-for="item in courseList"
                :key="item.id"
                :label="item.course_name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="学期">
            <el-select v-model="searchForm.semester" placeholder="请选择学期" clearable>
              <el-option label="2024-2025-1" value="2024-2025-1" />
              <el-option label="2024-2025-2" value="2024-2025-2" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="student_no" label="学号" width="120" />
        <el-table-column prop="student_name" label="学生姓名" width="100" />
        <el-table-column prop="course_name" label="课程名称" width="150" />
        <el-table-column prop="teacher_name" label="任课教师" width="100" />
        <el-table-column prop="semester" label="学期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="selection_time" label="选课时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="danger" link @click="handleDelete(row)">
              退选
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>
    
    <el-dialog
      v-model="dialogVisible"
      title="添加选课"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="学生" prop="student_id">
          <el-select v-model="form.student_id" placeholder="请选择学生" filterable style="width: 100%">
            <el-option
              v-for="item in studentList"
              :key="item.id"
              :label="`${item.student_no} - ${item.real_name}`"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="课程" prop="course_id">
          <el-select v-model="form.course_id" placeholder="请选择课程" style="width: 100%">
            <el-option
              v-for="item in courseList"
              :key="item.id"
              :label="item.course_name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="任课教师" prop="teacher_id">
          <el-select v-model="form.teacher_id" placeholder="请选择任课教师" style="width: 100%">
            <el-option
              v-for="item in teacherList"
              :key="item.id"
              :label="item.real_name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="学期" prop="semester">
          <el-select v-model="form.semester" placeholder="请选择学期" style="width: 100%">
            <el-option label="2024-2025-1" value="2024-2025-1" />
            <el-option label="2024-2025-2" value="2024-2025-2" />
          </el-select>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCourseSelections, createCourseSelection, deleteCourseSelection } from '@/api/selection'
import { getStudents } from '@/api/student'
import { getCourses } from '@/api/course'
import { getTeachers } from '@/api/teacher'

const route = useRoute()

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref(null)

const searchForm = reactive({
  studentNo: '',
  courseId: null,
  semester: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([])
const studentList = ref([])
const courseList = ref([])
const teacherList = ref([])

const form = reactive({
  student_id: null,
  course_id: null,
  teacher_id: null,
  semester: ''
})

const rules = {
  student_id: [
    { required: true, message: '请选择学生', trigger: 'change' }
  ],
  course_id: [
    { required: true, message: '请选择课程', trigger: 'change' }
  ],
  teacher_id: [
    { required: true, message: '请选择任课教师', trigger: 'change' }
  ],
  semester: [
    { required: true, message: '请选择学期', trigger: 'change' }
  ]
}

const getStatusText = (status) => {
  const statusMap = {
    selected: '已选',
    dropped: '已退',
    completed: '已完成'
  }
  return statusMap[status] || status
}

const getStatusType = (status) => {
  const typeMap = {
    selected: 'success',
    dropped: 'info',
    completed: 'primary'
  }
  return typeMap[status] || ''
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await getCourseSelections(params)
    tableData.value = res.data?.data || []
    pagination.total = res.data?.total || 0
  } catch (error) {
    console.error('获取选课列表失败:', error)
  } finally {
    loading.value = false
  }
}

const fetchStudentList = async () => {
  try {
    const res = await getStudents({ page: 1, pageSize: 1000 })
    studentList.value = res.data?.data || []
  } catch (error) {
    console.error('获取学生列表失败:', error)
  }
}

const fetchCourseList = async () => {
  try {
    const res = await getCourses({ page: 1, pageSize: 1000 })
    courseList.value = res.data?.data || []
  } catch (error) {
    console.error('获取课程列表失败:', error)
  }
}

const fetchTeacherList = async () => {
  try {
    const res = await getTeachers({ page: 1, pageSize: 1000 })
    teacherList.value = res.data?.data || []
  } catch (error) {
    console.error('获取教师列表失败:', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleReset = () => {
  Object.assign(searchForm, {
    studentNo: '',
    courseId: null,
    semester: ''
  })
  handleSearch()
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
  fetchData()
}

const handleCurrentChange = (val) => {
  pagination.page = val
  fetchData()
}

const handleAdd = () => {
  dialogVisible.value = true
  Object.assign(form, {
    student_id: null,
    course_id: null,
    teacher_id: null,
    semester: ''
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要退选 ${row.course_name} 吗?`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteCourseSelection(row.id)
    ElMessage.success('退选成功')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('退选失败:', error)
    }
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        await createCourseSelection(form)
        ElMessage.success('选课成功')
        dialogVisible.value = false
        fetchData()
      } catch (error) {
        console.error('选课失败:', error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

onMounted(() => {
  fetchStudentList()
  fetchCourseList()
  fetchTeacherList()
  fetchData()
})

watch(() => route.path, (newPath) => {
  if (newPath === '/course-selection') {
    fetchData()
  }
})
</script>

<style scoped>
.selection-page {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  margin-bottom: 20px;
}
</style>
