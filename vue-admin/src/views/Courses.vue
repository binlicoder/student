<template>
  <div class="courses-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>课程管理</span>
          <el-button type="primary" @click="handleAdd">添加课程</el-button>
        </div>
      </template>
      
      <div class="search-bar">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="课程编号">
            <el-input v-model="searchForm.courseCode" placeholder="请输入课程编号" clearable />
          </el-form-item>
          <el-form-item label="课程名称">
            <el-input v-model="searchForm.courseName" placeholder="请输入课程名称" clearable />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="course_code" label="课程编号" width="120" />
        <el-table-column prop="course_name" label="课程名称" width="200" />
        <el-table-column prop="credit" label="学分" width="80" />
        <el-table-column prop="hours" label="学时" width="80" />
        <el-table-column prop="course_type" label="课程类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getCourseTypeTag(row.course_type)">
              {{ getCourseTypeText(row.course_type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="课程描述" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
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
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="课程编号" prop="course_code">
          <el-input v-model="form.course_code" placeholder="请输入课程编号" :disabled="isEdit" />
        </el-form-item>
        
        <el-form-item label="课程名称" prop="course_name">
          <el-input v-model="form.course_name" placeholder="请输入课程名称" />
        </el-form-item>
        
        <el-form-item label="学分" prop="credit">
          <el-input-number v-model="form.credit" :min="0" :max="10" :step="0.5" :precision="1" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="学时" prop="hours">
          <el-input-number v-model="form.hours" :min="0" :max="200" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="课程类型" prop="course_type">
          <el-select v-model="form.course_type" placeholder="请选择课程类型" style="width: 100%">
            <el-option label="必修课" value="required" />
            <el-option label="选修课" value="elective" />
            <el-option label="实践课" value="practical" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="课程描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请输入课程描述" />
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
import { getCourses, createCourse, updateCourse, deleteCourse } from '@/api/course'

const route = useRoute()

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref(null)

const searchForm = reactive({
  courseCode: '',
  courseName: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([])

const form = reactive({
  id: null,
  course_code: '',
  course_name: '',
  credit: 0,
  hours: 0,
  course_type: 'required',
  description: ''
})

const rules = {
  course_code: [
    { required: true, message: '请输入课程编号', trigger: 'blur' }
  ],
  course_name: [
    { required: true, message: '请输入课程名称', trigger: 'blur' }
  ],
  credit: [
    { required: true, message: '请输入学分', trigger: 'blur' }
  ],
  hours: [
    { required: true, message: '请输入学时', trigger: 'blur' }
  ],
  course_type: [
    { required: true, message: '请选择课程类型', trigger: 'change' }
  ]
}

const getCourseTypeText = (type) => {
  const typeMap = {
    required: '必修课',
    elective: '选修课',
    practical: '实践课'
  }
  return typeMap[type] || type
}

const getCourseTypeTag = (type) => {
  const tagMap = {
    required: '',
    elective: 'success',
    practical: 'warning'
  }
  return tagMap[type] || ''
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await getCourses(params)
    tableData.value = res.data?.data || []
    pagination.total = res.data?.total || 0
  } catch (error) {
    console.error('获取课程列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleReset = () => {
  Object.assign(searchForm, {
    courseCode: '',
    courseName: ''
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
  dialogTitle.value = '添加课程'
  isEdit.value = false
  dialogVisible.value = true
  Object.assign(form, {
    id: null,
    course_code: '',
    course_name: '',
    credit: 0,
    hours: 0,
    course_type: 'required',
    description: ''
  })
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑课程'
  isEdit.value = true
  dialogVisible.value = true
  Object.assign(form, {
    id: row.id,
    course_code: row.course_code,
    course_name: row.course_name,
    credit: row.credit,
    hours: row.hours,
    course_type: row.course_type,
    description: row.description
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除课程 ${row.course_name} 吗?`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteCourse(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (isEdit.value) {
          await updateCourse(form.id, form)
          ElMessage.success('更新成功')
        } else {
          await createCourse(form)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        fetchData()
      } catch (error) {
        console.error('提交失败:', error)
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
  fetchData()
})

watch(() => route.path, (newPath) => {
  if (newPath === '/courses') {
    fetchData()
  }
})
</script>

<style scoped>
.courses-page {
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
