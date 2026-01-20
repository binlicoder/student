const pool = require('../config/db')

const getAllCourses = async (req, res) => {
  try {
    const { page = 1, pageSize = 10, courseCode, courseName } = req.query
    
    let sql = 'SELECT * FROM courses WHERE 1=1'
    const params = []
    
    if (courseCode) {
      sql += ' AND course_code LIKE ?'
      params.push(`%${courseCode}%`)
    }
    if (courseName) {
      sql += ' AND course_name LIKE ?'
      params.push(`%${courseName}%`)
    }
    
    const [rows] = await pool.query(sql, params)
    const [countResult] = await pool.query(sql.replace('SELECT *', 'SELECT COUNT(*) as total'), params)
    
    res.json({
      code: 200,
      message: 'success',
      data: rows,
      total: countResult[0].total
    })
  } catch (error) {
    console.error('获取课程列表失败:', error)
    res.status(500).json({ code: 500, message: '服务器错误' })
  }
}

const getCourseById = async (req, res) => {
  try {
    const { id } = req.params
    const [rows] = await pool.query('SELECT * FROM courses WHERE id = ?', [id])
    
    if (rows.length === 0) {
      return res.status(404).json({ code: 404, message: '课程不存在' })
    }
    
    res.json({ code: 200, message: 'success', data: rows[0] })
  } catch (error) {
    console.error('获取课程详情失败:', error)
    res.status(500).json({ code: 500, message: '服务器错误' })
  }
}

const createCourse = async (req, res) => {
  try {
    const { course_code, course_name, credit, hours, course_type, description } = req.body
    
    const [result] = await pool.query(
      `INSERT INTO courses (course_code, course_name, credit, hours, course_type, description)
       VALUES (?, ?, ?, ?, ?, ?)`,
      [course_code, course_name, credit, hours, course_type, description]
    )
    
    res.json({ code: 200, message: '创建成功', data: { id: result.insertId } })
  } catch (error) {
    console.error('创建课程失败:', error)
    res.status(500).json({ code: 500, message: '创建失败' })
  }
}

const updateCourse = async (req, res) => {
  try {
    const { id } = req.params
    const { course_name, credit, hours, course_type, description } = req.body
    
    const [result] = await pool.query(
      `UPDATE courses 
       SET course_name = ?, credit = ?, hours = ?, course_type = ?, description = ?
       WHERE id = ?`,
      [course_name, credit, hours, course_type, description, id]
    )
    
    if (result.affectedRows === 0) {
      return res.status(404).json({ code: 404, message: '课程不存在' })
    }
    
    res.json({ code: 200, message: '更新成功' })
  } catch (error) {
    console.error('更新课程失败:', error)
    res.status(500).json({ code: 500, message: '更新失败' })
  }
}

const deleteCourse = async (req, res) => {
  try {
    const { id } = req.params
    const [result] = await pool.query('DELETE FROM courses WHERE id = ?', [id])
    
    if (result.affectedRows === 0) {
      return res.status(404).json({ code: 404, message: '课程不存在' })
    }
    
    res.json({ code: 200, message: '删除成功' })
  } catch (error) {
    console.error('删除课程失败:', error)
    res.status(500).json({ code: 500, message: '删除失败' })
  }
}

module.exports = {
  getAllCourses,
  getCourseById,
  createCourse,
  updateCourse,
  deleteCourse
}
