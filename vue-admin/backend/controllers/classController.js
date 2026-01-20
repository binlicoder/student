const pool = require('../config/db')

const getAllClasses = async (req, res) => {
  try {
    const { page = 1, pageSize = 10, classCode, className } = req.query
    
    let sql = `
      SELECT c.*, u.real_name as head_teacher_name 
      FROM classes c
      LEFT JOIN users u ON c.head_teacher_id = u.id
      WHERE 1=1
    `
    const params = []
    
    if (classCode) {
      sql += ' AND c.class_code LIKE ?'
      params.push(`%${classCode}%`)
    }
    if (className) {
      sql += ' AND c.class_name LIKE ?'
      params.push(`%${className}%`)
    }
    
    const [rows] = await pool.query(sql, params)
    const [countResult] = await pool.query(sql.replace('SELECT c.*, u.real_name as head_teacher_name', 'SELECT COUNT(*) as total'), params)
    
    res.json({
      code: 200,
      message: 'success',
      data: rows,
      total: countResult[0].total
    })
  } catch (error) {
    console.error('获取班级列表失败:', error)
    res.status(500).json({ code: 500, message: '服务器错误' })
  }
}

const getClassById = async (req, res) => {
  try {
    const { id } = req.params
    const [rows] = await pool.query(
      `SELECT c.*, u.real_name as head_teacher_name 
       FROM classes c
       LEFT JOIN users u ON c.head_teacher_id = u.id
       WHERE c.id = ?`,
      [id]
    )
    
    if (rows.length === 0) {
      return res.status(404).json({ code: 404, message: '班级不存在' })
    }
    
    res.json({ code: 200, message: 'success', data: rows[0] })
  } catch (error) {
    console.error('获取班级详情失败:', error)
    res.status(500).json({ code: 500, message: '服务器错误' })
  }
}

const createClass = async (req, res) => {
  try {
    const { class_code, class_name, grade, major, class_size, head_teacher_id } = req.body
    
    const [result] = await pool.query(
      `INSERT INTO classes (class_code, class_name, grade, major, class_size, head_teacher_id)
       VALUES (?, ?, ?, ?, ?, ?)`,
      [class_code, class_name, grade, major, class_size, head_teacher_id]
    )
    
    res.json({ code: 200, message: '创建成功', data: { id: result.insertId } })
  } catch (error) {
    console.error('创建班级失败:', error)
    res.status(500).json({ code: 500, message: '创建失败' })
  }
}

const updateClass = async (req, res) => {
  try {
    const { id } = req.params
    const { class_name, grade, major, class_size, head_teacher_id } = req.body
    
    const [result] = await pool.query(
      `UPDATE classes 
       SET class_name = ?, grade = ?, major = ?, class_size = ?, head_teacher_id = ?
       WHERE id = ?`,
      [class_name, grade, major, class_size, head_teacher_id, id]
    )
    
    if (result.affectedRows === 0) {
      return res.status(404).json({ code: 404, message: '班级不存在' })
    }
    
    res.json({ code: 200, message: '更新成功' })
  } catch (error) {
    console.error('更新班级失败:', error)
    res.status(500).json({ code: 500, message: '更新失败' })
  }
}

const deleteClass = async (req, res) => {
  try {
    const { id } = req.params
    const [result] = await pool.query('DELETE FROM classes WHERE id = ?', [id])
    
    if (result.affectedRows === 0) {
      return res.status(404).json({ code: 404, message: '班级不存在' })
    }
    
    res.json({ code: 200, message: '删除成功' })
  } catch (error) {
    console.error('删除班级失败:', error)
    res.status(500).json({ code: 500, message: '删除失败' })
  }
}

module.exports = {
  getAllClasses,
  getClassById,
  createClass,
  updateClass,
  deleteClass
}
