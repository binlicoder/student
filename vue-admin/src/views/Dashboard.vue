<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #409EFF">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.studentCount }}</div>
              <div class="stat-label">学生总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #67C23A">
              <el-icon><Avatar /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.teacherCount }}</div>
              <div class="stat-label">教师总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #E6A23C">
              <el-icon><School /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.classCount }}</div>
              <div class="stat-label">班级总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #F56C6C">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.courseCount }}</div>
              <div class="stat-label">课程总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>欢迎使用学生信息管理系统</span>
            </div>
          </template>
          <div class="welcome-content">
            <h3>系统功能介绍</h3>
            <ul>
              <li>学生管理：对学生信息进行增删改查操作</li>
              <li>教师管理：对教师信息进行增删改查操作</li>
              <li>班级管理：对班级信息进行增删改查操作</li>
              <li>课程管理：对课程信息进行增删改查操作</li>
              <li>成绩管理：对学生成绩进行录入和管理</li>
              <li>选课管理：管理学生选课信息</li>
            </ul>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { User, Avatar, School, Document } from '@element-plus/icons-vue'
import { getStats } from '@/api/stats'

const stats = ref({
  studentCount: 0,
  teacherCount: 0,
  classCount: 0,
  courseCount: 0
})

onMounted(async () => {
  try {
    const res = await getStats()
    stats.value = res.data
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.stat-card {
  margin-bottom: 20px;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 30px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.card-header {
  font-weight: bold;
}

.welcome-content h3 {
  margin-bottom: 15px;
  color: #303133;
}

.welcome-content ul {
  list-style: none;
  padding: 0;
}

.welcome-content li {
  padding: 10px 0;
  color: #606266;
  border-bottom: 1px solid #ebeef5;
}

.welcome-content li:last-child {
  border-bottom: none;
}
</style>
