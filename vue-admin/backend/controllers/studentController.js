const pool = require('../config/db')

const getAllStudents = async (req, res) => {
  try {
    const { page = 1, pageSize = 10, studentNo, name, classId } = req.query
    const offset = (page - 1) * pageSize
    
    let sql = `
      SELECT s.*, u.real_name, u.username, c.class_name 
      FROM students s
      LEFT JOIN users u ON s.user_id = u.id
      LEFT JOIN classes c ON s.class_id = c.id
      WHERE 1=1
    `
    const params = []
    
    if (studentNo) {
      sql += ' AND s.student_no LIKE ?'
      params.push(`%${studentNo}%`)
    }
    if (name) {
      sql += ' AND u.real_name LIKE ?'
      params.push(`%${name}%`)
    }
    if (classId) {
      sql += ' AND s.class_id = ?'
      params.push(classId)
    }
    
    const [rows] = await pool.query(sql, params)
    const [countResult] = await pool.query(sql.replace('SELECT s.*, u.real_name, u.username, c.class_name', 'SELECT COUNT(*) as total'), params)
    
    res.json({
      code: 200,
      message: 'success',
      data: rows,
      total: countResult[0].total
    })
  } catch (error) {
    console.error('获取学生列表失败:', error)
    res.status(500).json({ code: 500, message: '服务器错误' })
  }
}

const getStudentById = async (req, res) => {
  try {
    const { id } = req.params
    const [rows] = await pool.query(
      `SELECT s.*, u.real_name, u.username, c.class_name 
       FROM students s
       LEFT JOIN users u ON s.user_id = u.id
       LEFT JOIN classes c ON s.class_id = c.id
       WHERE s.id = ?`,
      [id]
    )
    
    if (rows.length === 0) {
      return res.status(404).json({ code: 404, message: '学生不存在' })
    }
    
    res.json({ code: 200, message: 'success', data: rows[0] })
  } catch (error) {
    console.error('获取学生详情失败:', error)
    res.status(500).json({ code: 500, message: '服务器错误' })
  }
}

const createStudent = async (req, res) => {
  const connection = await pool.getConnection()
  try {
    await connection.beginTransaction()
    
    const { student_no, real_name, username, password, gender, class_id, birthday, phone, email, address, enrollment_date, status } = req.body
    
    const [userResult] = await connection.query(
      'INSERT INTO users (username, password, real_name, role, status) VALUES (?, ?, ?, ?, 1)',
      [username, password, real_name, 'student']
    )
    
    const [studentResult] = await connection.query(
      `INSERT INTO students (user_id, student_no, class_id, gender, birthday, phone, email, address, enrollment_date, status)
       VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`,
      [userResult.insertId, student_no, class_id, gender, birthday, phone, email, address, enrollment_date, status]
    )
    
    await connection.commit()
    res.json({ code: 200, message: '创建成功', data: { id: studentResult.insertId } })
  } catch (error) {
    await connection.rollback()
    console.error('创建学生失败:', error)
    res.status(500).json({ code: 500, message: '创建失败' })
  } finally {
    connection.release()
  }
}

const updateStudent = async (req, res) => {
  try {
    const { id } = req.params
    const { real_name, gender, class_id, birthday, phone, email, address, enrollment_date, status } = req.body
    
    const [result] = await pool.query(
      `UPDATE students s
       LEFT JOIN users u ON s.user_id = u.id
       SET s.gender = ?, s.class_id = ?, s.birthday = ?, s.phone = ?, s.email = ?, 
           s.address = ?, s.enrollment_date = ?, s.status = ?, u.real_name = ?
       WHERE s.id = ?`,
      [gender, class_id, birthday, phone, email, address, enrollment_date, status, real_name, id]
    )
    
    if (result.affectedRows === 0) {
      return res.status(404).json({ code: 404, message: '学生不存在' })
    }
    
    res.json({ code: 200, message: '更新成功' })
  } catch (error) {
    console.error('更新学生失败:', error)
    res.status(500).json({ code: 500, message: '更新失败' })
  }
}

const deleteStudent = async (req, res) => {
  try {
    const { id } = req.params
    const [result] = await pool.query('DELETE FROM students WHERE id = ?', [id])
    
    if (result.affectedRows === 0) {
      return res.status(404).json({ code: 404, message: '学生不存在' })
    }
    
    res.json({ code: 200, message: '删除成功' })
  } catch (error) {
    console.error('删除学生失败:', error)
    res.status(500).json({ code: 500, message: '删除失败' })
  }
}

module.exports = {
  getAllStudents,
  getStudentById,
  createStudent,
  updateStudent,
  deleteStudent
}
