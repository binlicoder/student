<template>
  <div class="students-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>学生管理</span>
          <el-button type="primary" @click="handleAdd">添加学生</el-button>
        </div>
      </template>
      
      <div class="search-bar">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="学号">
            <el-input v-model="searchForm.studentNo" placeholder="请输入学号" clearable />
          </el-form-item>
          <el-form-item label="姓名">
            <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable />
          </el-form-item>
          <el-form-item label="班级">
            <el-select v-model="searchForm.classId" placeholder="请选择班级" clearable>
              <el-option
                v-for="item in classList"
                :key="item.id"
                :label="item.class_name"
                :value="item.id"
              />
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
        <el-table-column prop="real_name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            {{ row.gender === 'male' ? '男' : row.gender === 'female' ? '女' : '其他' }}
          </template>
        </el-table-column>
        <el-table-column prop="class_name" label="班级" width="150" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'studying' ? 'success' : 'info'">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
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
        <el-form-item label="学号" prop="student_no">
          <el-input v-model="form.student_no" placeholder="请输入学号" :disabled="isEdit" />
        </el-form-item>
        
        <el-form-item label="姓名" prop="real_name">
          <el-input v-model="form.real_name" placeholder="请输入姓名" />
        </el-form-item>
        
        <el-form-item label="用户名" prop="username" v-if="!isEdit">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        
        <el-form-item label="密码" prop="password" v-if="!isEdit">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio label="male">男</el-radio>
            <el-radio label="female">女</el-radio>
            <el-radio label="other">其他</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="班级" prop="class_id">
          <el-select v-model="form.class_id" placeholder="请选择班级" style="width: 100%">
            <el-option
              v-for="item in classList"
              :key="item.id"
              :label="item.class_name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="出生日期" prop="birthday">
          <el-date-picker
            v-model="form.birthday"
            type="date"
            placeholder="选择出生日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        
        <el-form-item label="家庭住址" prop="address">
          <el-input v-model="form.address" type="textarea" :rows="2" placeholder="请输入家庭住址" />
        </el-form-item>
        
        <el-form-item label="入学日期" prop="enrollment_date">
          <el-date-picker
            v-model="form.enrollment_date"
            type="date"
            placeholder="选择入学日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="学籍状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择学籍状态" style="width: 100%">
            <el-option label="在读" value="studying" />
            <el-option label="毕业" value="graduated" />
            <el-option label="休学" value="suspended" />
            <el-option label="退学" value="withdrawn" />
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
import { getStudents, createStudent, updateStudent, deleteStudent } from '@/api/student'

const route = useRoute()

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref(null)

const searchForm = reactive({
  studentNo: '',
  name: '',
  classId: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([])
const classList = ref([])

const form = reactive({
  id: null,
  student_no: '',
  real_name: '',
  username: '',
  password: '',
  gender: 'male',
  class_id: null,
  birthday: '',
  phone: '',
  email: '',
  address: '',
  enrollment_date: '',
  status: 'studying'
})

const rules = {
  student_no: [
    { required: true, message: '请输入学号', trigger: 'blur' }
  ],
  real_name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  class_id: [
    { required: true, message: '请选择班级', trigger: 'change' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

const getStatusText = (status) => {
  const statusMap = {
    studying: '在读',
    graduated: '毕业',
    suspended: '休学',
    withdrawn: '退学'
  }
  return statusMap[status] || status
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await getStudents(params)
    tableData.value = res.data?.data || []
    pagination.total = res.data?.total || 0
  } catch (error) {
    console.error('获取学生列表失败:', error)
  } finally {
    loading.value = false
  }
}

const fetchClassList = async () => {
  classList.value = [
    { id: 1, class_name: '计算机科学与技术1班' },
    { id: 2, class_name: '软件工程1班' },
    { id: 3, class_name: '人工智能1班' }
  ]
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleReset = () => {
  Object.assign(searchForm, {
    studentNo: '',
    name: '',
    classId: ''
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
  dialogTitle.value = '添加学生'
  isEdit.value = false
  dialogVisible.value = true
  Object.assign(form, {
    id: null,
    student_no: '',
    real_name: '',
    username: '',
    password: '',
    gender: 'male',
    class_id: null,
    birthday: '',
    phone: '',
    email: '',
    address: '',
    enrollment_date: '',
    status: 'studying'
  })
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑学生'
  isEdit.value = true
  dialogVisible.value = true
  Object.assign(form, {
    id: row.id,
    student_no: row.student_no,
    real_name: row.real_name,
    username: row.username,
    password: '',
    gender: row.gender,
    class_id: row.class_id,
    birthday: row.birthday,
    phone: row.phone,
    email: row.email,
    address: row.address,
    enrollment_date: row.enrollment_date,
    status: row.status
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除学生 ${row.real_name} 吗?`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteStudent(row.id)
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
          await updateStudent(form.id, form)
          ElMessage.success('更新成功')
        } else {
          await createStudent(form)
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
  fetchClassList()
  fetchData()
})

watch(() => route.path, (newPath) => {
  if (newPath === '/students') {
    fetchData()
  }
})
</script>

<style scoped>
.students-page {
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
