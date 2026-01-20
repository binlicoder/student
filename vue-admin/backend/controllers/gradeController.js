const pool = require('../config/db')

const getAllGrades = async (req, res) => {
  try {
    const { page = 1, pageSize = 10, studentNo, courseId, semester } = req.query
    
    let sql = `
      SELECT g.*, s.student_no, u.real_name as student_name, c.course_name, t.real_name as teacher_name
      FROM grades g
      LEFT JOIN students s ON g.student_id = s.id
      LEFT JOIN users u ON s.user_id = u.id
      LEFT JOIN courses c ON g.course_id = c.id
      LEFT JOIN teachers t ON g.teacher_id = t.id
      WHERE 1=1
    `
    const params = []
    
    if (studentNo) {
      sql += ' AND s.student_no LIKE ?'
      params.push(`%${studentNo}%`)
    }
    if (courseId) {
      sql += ' AND g.course_id = ?'
      params.push(courseId)
    }
    if (semester) {
      sql += ' AND g.semester = ?'
      params.push(semester)
    }
    
    const [rows] = await pool.query(sql, params)
    const [countResult] = await pool.query(sql.replace('SELECT g.*, s.student_no, u.real_name as student_name, c.course_name, t.real_name as teacher_name', 'SELECT COUNT(*) as total'), params)
    
    res.json({
      code: 200,
      message: 'success',
      data: rows,
      total: countResult[0].total
    })
  } catch (error) {
    console.error('获取成绩列表失败:', error)
    res.status(500).json({ code: 500, message: '服务器错误' })
  }
}

const getGradeById = async (req, res) => {
  try {
    const { id } = req.params
    const [rows] = await pool.query(
      `SELECT g.*, s.student_no, u.real_name as student_name, c.course_name, t.real_name as teacher_name
       FROM grades g
       LEFT JOIN students s ON g.student_id = s.id
       LEFT JOIN users u ON s.user_id = u.id
       LEFT JOIN courses c ON g.course_id = c.id
       LEFT JOIN teachers t ON g.teacher_id = t.id
       WHERE g.id = ?`,
      [id]
    )
    
    if (rows.length === 0) {
      return res.status(404).json({ code: 404, message: '成绩不存在' })
    }
    
    res.json({ code: 200, message: 'success', data: rows[0] })
  } catch (error) {
    console.error('获取成绩详情失败:', error)
    res.status(500).json({ code: 500, message: '服务器错误' })
  }
}

const createGrade = async (req, res) => {
  try {
    const { student_id, course_id, teacher_id, semester, usual_score, exam_score, total_score, grade_level, remark } = req.body
    
    const [result] = await pool.query(
      `INSERT INTO grades (student_id, course_id, teacher_id, semester, usual_score, exam_score, total_score, grade_level, remark)
       VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`,
      [student_id, course_id, teacher_id, semester, usual_score, exam_score, total_score, grade_level, remark]
    )
    
    res.json({ code: 200, message: '创建成功', data: { id: result.insertId } })
  } catch (error) {
    console.error('创建成绩失败:', error)
    res.status(500).json({ code: 500, message: '创建失败' })
  }
}

const updateGrade = async (req, res) => {
  try {
    const { id } = req.params
    const { usual_score, exam_score, total_score, grade_level, remark } = req.body
    
    const [result] = await pool.query(
      `UPDATE grades 
       SET usual_score = ?, exam_score = ?, total_score = ?, grade_level = ?, remark = ?
       WHERE id = ?`,
      [usual_score, exam_score, total_score, grade_level, remark, id]
    )
    
    if (result.affectedRows === 0) {
      return res.status(404).json({ code: 404, message: '成绩不存在' })
    }
    
    res.json({ code: 200, message: '更新成功' })
  } catch (error) {
    console.error('更新成绩失败:', error)
    res.status(500).json({ code: 500, message: '更新失败' })
  }
}

const deleteGrade = async (req, res) => {
  try {
    const { id } = req.params
    const [result] = await pool.query('DELETE FROM grades WHERE id = ?', [id])
    
    if (result.affectedRows === 0) {
      return res.status(404).json({ code: 404, message: '成绩不存在' })
    }
    
    res.json({ code: 200, message: '删除成功' })
  } catch (error) {
    console.error('删除成绩失败:', error)
    res.status(500).json({ code: 500, message: '删除失败' })
  }
}

const getStudentGrades = async (req, res) => {
  try {
    const { studentId } = req.params
    const [rows] = await pool.query(
      `SELECT g.*, c.course_name, t.real_name as teacher_name
       FROM grades g
       LEFT JOIN courses c ON g.course_id = c.id
       LEFT JOIN teachers t ON g.teacher_id = t.id
       WHERE g.student_id = ?`,
      [studentId]
    )
    
    res.json({ code: 200, message: 'success', data: rows })
  } catch (error) {
    console.error('获取学生成绩失败:', error)
    res.status(500).json({ code: 500, message: '服务器错误' })
  }
}

module.exports = {
  getAllGrades,
  getGradeById,
  createGrade,
  updateGrade,
  deleteGrade,
  getStudentGrades
}
