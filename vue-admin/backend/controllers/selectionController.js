const pool = require('../config/db')

const getAllCourseSelections = async (req, res) => {
  try {
    const { page = 1, pageSize = 10, studentNo, courseId, semester } = req.query
    
    let sql = `
      SELECT cs.*, s.student_no, u.real_name as student_name, c.course_name, t.real_name as teacher_name
      FROM course_selections cs
      LEFT JOIN students s ON cs.student_id = s.id
      LEFT JOIN users u ON s.user_id = u.id
      LEFT JOIN courses c ON cs.course_id = c.id
      LEFT JOIN teachers t ON cs.teacher_id = t.id
      WHERE 1=1
    `
    const params = []
    
    if (studentNo) {
      sql += ' AND s.student_no LIKE ?'
      params.push(`%${studentNo}%`)
    }
    if (courseId) {
      sql += ' AND cs.course_id = ?'
      params.push(courseId)
    }
    if (semester) {
      sql += ' AND cs.semester = ?'
      params.push(semester)
    }
    
    const [rows] = await pool.query(sql, params)
    const [countResult] = await pool.query(sql.replace('SELECT cs.*, s.student_no, u.real_name as student_name, c.course_name, t.real_name as teacher_name', 'SELECT COUNT(*) as total'), params)
    
    res.json({
      code: 200,
      message: 'success',
      data: rows,
      total: countResult[0].total
    })
  } catch (error) {
    console.error('获取选课列表失败:', error)
    res.status(500).json({ code: 500, message: '服务器错误' })
  }
}

const createCourseSelection = async (req, res) => {
  try {
    const { student_id, course_id, teacher_id, semester } = req.body
    
    const [result] = await pool.query(
      `INSERT INTO course_selections (student_id, course_id, teacher_id, semester, status)
       VALUES (?, ?, ?, ?, 'selected')`,
      [student_id, course_id, teacher_id, semester]
    )
    
    res.json({ code: 200, message: '选课成功', data: { id: result.insertId } })
  } catch (error) {
    console.error('选课失败:', error)
    if (error.code === 'ER_DUP_ENTRY') {
      res.status(400).json({ code: 400, message: '该学生已选择此课程' })
    } else {
      res.status(500).json({ code: 500, message: '选课失败' })
    }
  }
}

const deleteCourseSelection = async (req, res) => {
  try {
    const { id } = req.params
    const [result] = await pool.query('DELETE FROM course_selections WHERE id = ?', [id])
    
    if (result.affectedRows === 0) {
      return res.status(404).json({ code: 404, message: '选课记录不存在' })
    }
    
    res.json({ code: 200, message: '退选成功' })
  } catch (error) {
    console.error('退选失败:', error)
    res.status(500).json({ code: 500, message: '退选失败' })
  }
}

const getStudentSelections = async (req, res) => {
  try {
    const { studentId } = req.params
    const [rows] = await pool.query(
      `SELECT cs.*, c.course_name, t.real_name as teacher_name
       FROM course_selections cs
       LEFT JOIN courses c ON cs.course_id = c.id
       LEFT JOIN teachers t ON cs.teacher_id = t.id
       WHERE cs.student_id = ?`,
      [studentId]
    )
    
    res.json({ code: 200, message: 'success', data: rows })
  } catch (error) {
    console.error('获取学生选课失败:', error)
    res.status(500).json({ code: 500, message: '服务器错误' })
  }
}

module.exports = {
  getAllCourseSelections,
  createCourseSelection,
  deleteCourseSelection,
  getStudentSelections
}
