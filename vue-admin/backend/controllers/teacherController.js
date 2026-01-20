const pool = require('../config/db')

const getAllTeachers = async (req, res) => {
  try {
    const { page = 1, pageSize = 10, teacherNo, name } = req.query
    const offset = (page - 1) * pageSize
    
    let sql = `
      SELECT t.*, u.real_name, u.username 
      FROM teachers t
      LEFT JOIN users u ON t.user_id = u.id
      WHERE 1=1
    `
    const params = []
    
    if (teacherNo) {
      sql += ' AND t.teacher_no LIKE ?'
      params.push(`%${teacherNo}%`)
    }
    if (name) {
      sql += ' AND u.real_name LIKE ?'
      params.push(`%${name}%`)
    }
    
    const [rows] = await pool.query(sql, params)
    const [countResult] = await pool.query(sql.replace('SELECT t.*, u.real_name, u.username', 'SELECT COUNT(*) as total'), params)
    
    res.json({
      code: 200,
      message: 'success',
      data: rows,
      total: countResult[0].total
    })
  } catch (error) {
    console.error('获取教师列表失败:', error)
    res.status(500).json({ code: 500, message: '服务器错误' })
  }
}

const getTeacherById = async (req, res) => {
  try {
    const { id } = req.params
    const [rows] = await pool.query(
      `SELECT t.*, u.real_name, u.username 
       FROM teachers t
       LEFT JOIN users u ON t.user_id = u.id
       WHERE t.id = ?`,
      [id]
    )
    
    if (rows.length === 0) {
      return res.status(404).json({ code: 404, message: '教师不存在' })
    }
    
    res.json({ code: 200, message: 'success', data: rows[0] })
  } catch (error) {
    console.error('获取教师详情失败:', error)
    res.status(500).json({ code: 500, message: '服务器错误' })
  }
}

const createTeacher = async (req, res) => {
  const connection = await pool.getConnection()
  try {
    await connection.beginTransaction()
    
    const { teacher_no, real_name, username, password, gender, phone, email, title, department } = req.body
    
    const [userResult] = await connection.query(
      'INSERT INTO users (username, password, real_name, role, status) VALUES (?, ?, ?, ?, 1)',
      [username, password, real_name, 'teacher']
    )
    
    const [teacherResult] = await connection.query(
      `INSERT INTO teachers (user_id, teacher_no, gender, phone, email, title, department)
       VALUES (?, ?, ?, ?, ?, ?, ?)`,
      [userResult.insertId, teacher_no, gender, phone, email, title, department]
    )
    
    await connection.commit()
    res.json({ code: 200, message: '创建成功', data: { id: teacherResult.insertId } })
  } catch (error) {
    await connection.rollback()
    console.error('创建教师失败:', error)
    res.status(500).json({ code: 500, message: '创建失败' })
  } finally {
    connection.release()
  }
}

const updateTeacher = async (req, res) => {
  try {
    const { id } = req.params
    const { real_name, gender, phone, email, title, department } = req.body
    
    const [result] = await pool.query(
      `UPDATE teachers t
       LEFT JOIN users u ON t.user_id = u.id
       SET t.gender = ?, t.phone = ?, t.email = ?, t.title = ?, t.department = ?, u.real_name = ?
       WHERE t.id = ?`,
      [gender, phone, email, title, department, real_name, id]
    )
    
    if (result.affectedRows === 0) {
      return res.status(404).json({ code: 404, message: '教师不存在' })
    }
    
    res.json({ code: 200, message: '更新成功' })
  } catch (error) {
    console.error('更新教师失败:', error)
    res.status(500).json({ code: 500, message: '更新失败' })
  }
}

const deleteTeacher = async (req, res) => {
  try {
    const { id } = req.params
    const [result] = await pool.query('DELETE FROM teachers WHERE id = ?', [id])
    
    if (result.affectedRows === 0) {
      return res.status(404).json({ code: 404, message: '教师不存在' })
    }
    
    res.json({ code: 200, message: '删除成功' })
  } catch (error) {
    console.error('删除教师失败:', error)
    res.status(500).json({ code: 500, message: '删除失败' })
  }
}

module.exports = {
  getAllTeachers,
  getTeacherById,
  createTeacher,
  updateTeacher,
  deleteTeacher
}
