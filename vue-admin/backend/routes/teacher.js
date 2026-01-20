const express = require('express')
const router = express.Router()
const teacherController = require('../controllers/teacherController')
const authMiddleware = require('../middleware/auth')

router.get('/', authMiddleware, teacherController.getAllTeachers)
router.get('/:id', authMiddleware, teacherController.getTeacherById)
router.post('/', authMiddleware, teacherController.createTeacher)
router.put('/:id', authMiddleware, teacherController.updateTeacher)
router.delete('/:id', authMiddleware, teacherController.deleteTeacher)

module.exports = router
