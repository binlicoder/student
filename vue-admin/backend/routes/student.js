const express = require('express')
const router = express.Router()
const studentController = require('../controllers/studentController')
const authMiddleware = require('../middleware/auth')

router.get('/', authMiddleware, studentController.getAllStudents)
router.get('/:id', authMiddleware, studentController.getStudentById)
router.post('/', authMiddleware, studentController.createStudent)
router.put('/:id', authMiddleware, studentController.updateStudent)
router.delete('/:id', authMiddleware, studentController.deleteStudent)

module.exports = router
