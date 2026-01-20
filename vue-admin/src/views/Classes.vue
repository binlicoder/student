<template>
  <div class="classes-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>班级管理</span>
          <el-button type="primary" @click="handleAdd">添加班级</el-button>
        </div>
      </template>
      
      <div class="search-bar">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="班级编号">
            <el-input v-model="searchForm.classCode" placeholder="请输入班级编号" clearable />
          </el-form-item>
          <el-form-item label="班级名称">
            <el-input v-model="searchForm.className" placeholder="请输入班级名称" clearable />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="class_code" label="班级编号" width="150" />
        <el-table-column prop="class_name" label="班级名称" width="180" />
        <el-table-column prop="grade" label="年级" width="100" />
        <el-table-column prop="major" label="专业" width="150" />
        <el-table-column prop="class_size" label="班级人数" width="100" />
        <el-table-column prop="head_teacher_name" label="班主任" width="120" />
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
      width="500px"
      @close="handleDialogClose"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="班级编号" prop="class_code">
          <el-input v-model="form.class_code" placeholder="请输入班级编号" :disabled="isEdit" />
        </el-form-item>
        
        <el-form-item label="班级名称" prop="class_name">
          <el-input v-model="form.class_name" placeholder="请输入班级名称" />
        </el-form-item>
        
        <el-form-item label="年级" prop="grade">
          <el-input v-model="form.grade" placeholder="请输入年级" />
        </el-form-item>
        
        <el-form-item label="专业" prop="major">
          <el-input v-model="form.major" placeholder="请输入专业" />
        </el-form-item>
        
        <el-form-item label="班级人数" prop="class_size">
          <el-input-number v-model="form.class_size" :min="0" :max="100" style="width: 100%" />
        </el-form-item>
        
        <el-form-item label="班主任" prop="head_teacher_id">
          <el-select v-model="form.head_teacher_id" placeholder="请选择班主任" clearable style="width: 100%">
            <el-option
              v-for="item in teacherList"
              :key="item.id"
              :label="item.real_name"
              :value="item.id"
            />
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
import { getClasses, createClass, updateClass, deleteClass } from '@/api/class'

const route = useRoute()

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref(null)

const searchForm = reactive({
  classCode: '',
  className: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([])
const teacherList = ref([])

const form = reactive({
  id: null,
  class_code: '',
  class_name: '',
  grade: '',
  major: '',
  class_size: 0,
  head_teacher_id: null
})

const rules = {
  class_code: [
    { required: true, message: '请输入班级编号', trigger: 'blur' }
  ],
  class_name: [
    { required: true, message: '请输入班级名称', trigger: 'blur' }
  ],
  grade: [
    { required: true, message: '请输入年级', trigger: 'blur' }
  ],
  major: [
    { required: true, message: '请输入专业', trigger: 'blur' }
  ]
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await getClasses(params)
    tableData.value = res.data?.data || []
    pagination.total = res.data?.total || 0
  } catch (error) {
    console.error('获取班级列表失败:', error)
  } finally {
    loading.value = false
  }
}

const fetchTeacherList = async () => {
  teacherList.value = [
    { id: 2, real_name: '张老师' },
    { id: 3, real_name: '李老师' }
  ]
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleReset = () => {
  Object.assign(searchForm, {
    classCode: '',
    className: ''
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
  dialogTitle.value = '添加班级'
  isEdit.value = false
  dialogVisible.value = true
  Object.assign(form, {
    id: null,
    class_code: '',
    class_name: '',
    grade: '',
    major: '',
    class_size: 0,
    head_teacher_id: null
  })
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑班级'
  isEdit.value = true
  dialogVisible.value = true
  Object.assign(form, {
    id: row.id,
    class_code: row.class_code,
    class_name: row.class_name,
    grade: row.grade,
    major: row.major,
    class_size: row.class_size,
    head_teacher_id: row.head_teacher_id
  })
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除班级 ${row.class_name} 吗?`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteClass(row.id)
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
          await updateClass(form.id, form)
          ElMessage.success('更新成功')
        } else {
          await createClass(form)
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
  fetchTeacherList()
  fetchData()
})

watch(() => route.path, (newPath) => {
  if (newPath === '/classes') {
    fetchData()
  }
})
</script>

<style scoped>
.classes-page {
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
