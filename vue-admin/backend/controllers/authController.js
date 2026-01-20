const jwt = require('jsonwebtoken')
const bcrypt = require('bcryptjs')
const pool = require('../config/db')

const login = async (req, res) => {
  try {
    const { username, password } = req.body
    
    const [users] = await pool.query(
      'SELECT * FROM users WHERE username = ?',
      [username]
    )
    
    if (users.length === 0) {
      return res.status(401).json({ code: 401, message: '用户名或密码错误' })
    }
    
    const user = users[0]
    
    const isPasswordValid = await bcrypt.compare(password, user.password)
    if (!isPasswordValid) {
      return res.status(401).json({ code: 401, message: '用户名或密码错误' })
    }
    
    if (user.status === 0) {
      return res.status(403).json({ code: 403, message: '账号已被禁用' })
    }
    
    const token = jwt.sign(
      { id: user.id, username: user.username, role: user.role },
      process.env.JWT_SECRET,
      { expiresIn: process.env.JWT_EXPIRES_IN }
    )
    
    res.json({
      code: 200,
      message: '登录成功',
      token,
      user: {
        id: user.id,
        username: user.username,
        real_name: user.real_name,
        role: user.role
      }
    })
  } catch (error) {
    console.error('登录失败:', error)
    res.status(500).json({ code: 500, message: '服务器错误' })
  }
}

const getUserInfo = async (req, res) => {
  try {
    const userId = req.user.id
    
    const [users] = await pool.query(
      'SELECT id, username, real_name, role, status, created_at FROM users WHERE id = ?',
      [userId]
    )
    
    if (users.length === 0) {
      return res.status(404).json({ code: 404, message: '用户不存在' })
    }
    
    res.json({
      code: 200,
      message: 'success',
      data: users[0]
    })
  } catch (error) {
    console.error('获取用户信息失败:', error)
    res.status(500).json({ code: 500, message: '服务器错误' })
  }
}

const logout = async (req, res) => {
  res.json({
    code: 200,
    message: '退出成功'
  })
}

module.exports = {
  login,
  getUserInfo,
  logout
}
