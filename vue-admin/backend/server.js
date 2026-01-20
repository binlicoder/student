const express = require('express')
const cors = require('cors')
require('dotenv').config()

const authRoutes = require('./routes/auth')
const studentRoutes = require('./routes/student')
const teacherRoutes = require('./routes/teacher')
const classRoutes = require('./routes/class')
const courseRoutes = require('./routes/course')
const gradeRoutes = require('./routes/grade')
const selectionRoutes = require('./routes/selection')

const app = express()
const PORT = process.env.PORT || 3000

app.use(cors())
app.use(express.json())
app.use(express.urlencoded({ extended: true }))

app.use('/api/auth', authRoutes)
app.use('/api/students', studentRoutes)
app.use('/api/teachers', teacherRoutes)
app.use('/api/classes', classRoutes)
app.use('/api/courses', courseRoutes)
app.use('/api/grades', gradeRoutes)
app.use('/api/course-selections', selectionRoutes)

app.get('/', (req, res) => {
  res.json({ message: '学生信息管理系统 API' })
})

app.use((err, req, res, next) => {
  console.error(err.stack)
  res.status(500).json({ code: 500, message: '服务器内部错误' })
})

app.listen(PORT, () => {
  console.log(`服务器运行在 http://localhost:${PORT}`)
})
