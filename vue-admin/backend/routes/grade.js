const express = require('express')
const router = express.Router()
const gradeController = require('../controllers/gradeController')
const authMiddleware = require('../middleware/auth')

router.get('/', authMiddleware, gradeController.getAllGrades)
router.get('/:id', authMiddleware, gradeController.getGradeById)
router.get('/student/:studentId', authMiddleware, gradeController.getStudentGrades)
router.post('/', authMiddleware, gradeController.createGrade)
router.put('/:id', authMiddleware, gradeController.updateGrade)
router.delete('/:id', authMiddleware, gradeController.deleteGrade)

module.exports = router
