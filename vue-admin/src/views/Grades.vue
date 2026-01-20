<template>
  <div class="grades-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>成绩管理</span>
          <el-button type="primary" @click="handleAdd">录入成绩</el-button>
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
        <el-table-column prop="semester" label="学期" width="120" />
        <el-table-column prop="usual_score" label="平时成绩" width="100" />
        <el-table-column prop="exam_score" label="考试成绩" width="100" />
        <el-table-column prop="total_score" label="总评成绩" width="100" />
        <el-table-column prop="grade_level" label="等级" width="80">
          <template #default="{ row }">
            <el-tag :type="getGradeLevelType(row.grade_level)">
              {{ row.grade_level }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="teacher_name" label="任课教师" width="100" />
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
        
        <el-form-item label="平时成绩" prop="usual_score">
          <el-input-number v-model="form.usual_score" :min="0" :max="100" :precision="2" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="考试成绩" prop="exam_score">
          <el-input-number v-model="form.exam_score" :min="0" :max="100" :precision="2" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="总评成绩" prop="total_score">
          <el-input-number v-model="form.total_score" :min="0" :max="100" :precision="2" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="等级" prop="grade_level">
          <el-select v-model="form.grade_level" placeholder="请选择等级" style="width: 100%">
            <el-option label="A (90-100)" value="A" />
            <el-option label="B (80-89)" value="B" />
            <el-option label="C (70-79)" value="C" />
            <el-option label="D (60-69)" value="D" />
            <el-option label="F (0-59)" value="F" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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
import { getGrades, createGrade, updateGrade, deleteGrade } from '@/api/grade'

const route = useRoute()

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
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
  id: null,
  student_id: null,
  course_id: null,
  teacher_id: null,
  semester: '',
  usual_score: 0,
  exam_score: 0,
  total_score: 0,
  grade_level: '',
  remark: ''
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
  ],
  total_score: [
    { required: true, message: '请输入总评成绩', trigger: 'blur' }
  ],
  grade_level: [
    { required: true, message: '请选择等级', trigger: 'change' }
  ]
}

const getGradeLevelType = (level) => {
  const typeMap = {
    A: 'success',
    B: 'primary',
    C: 'warning',
    D: 'info',
    F: 'danger'
  }
  return typeMap[level] || ''
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await getGrades(params)
    tableData.value = res.data?.data || []
    pagination.total = res.data?.total || 0
  } catch (error) {
    console.error('获取成绩列表失败:', error)
  } finally {
    loading.value = false
  }
}

const fetchStudentList = async () => {
  studentList.value = [
    { id: 1, student_no: 'S2024001', real_name: '张三' },
    { id: 2, student_no: 'S2024002', real_name: '李四' }
  ]
}

const fetchCourseList = async () => {
  courseList.value = [
    { id: 1, course_name: '高等数学' },
    { id: 2, course_name: '程序设计基础' },
    { id: 3, course_name: '数据结构' },
    { id: 4, course_name: '数据库原理' }
  ]
}

const fetchTeacherList = async () => {
  teacherList.value = [
    { id: 1, real_name: '张老师' },
    { id: 2, real_name: '李老师' }
  ]
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
  dialogTitle.value = '录入成绩'
  isEdit.value = false
  dialogVisible.value = true
  Object.assign(form, {
    id: null,
    student_id: null,
    course_id: null,
    teacher_id: null,
    semester: '',
    usual_score: 0,
    exam_score: 0,
    total_score: 0,
    grade_level: '',
    remark: ''
  })
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑成绩'
  isEdit.value = true
  dialogVisible.value = true
  Object.assign(form, {
    id: row.id,
    student_id: row.student_id,
    course_id: row.course_id,
    teacher_id: row.teacher_id,
    semester: row.semester,
    usual_score: row.usual_score,
    exam_score: row.exam_score,
    total_score: row.total_score,
    grade_level: row.grade_level,
    remark: row.remark
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除这条成绩记录吗?`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteGrade(row.id)
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
          await updateGrade(form.id, form)
          ElMessage.success('更新成功')
        } else {
          await createGrade(form)
          ElMessage.success('录入成功')
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
  fetchStudentList()
  fetchCourseList()
  fetchTeacherList()
  fetchData()
})

watch(() => route.path, (newPath) => {
  if (newPath === '/grades') {
    fetchData()
  }
})
</script>

<style scoped>
.grades-page {
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
