const express = require('express')
const router = express.Router()
const selectionController = require('../controllers/selectionController')
const authMiddleware = require('../middleware/auth')

router.get('/', authMiddleware, selectionController.getAllCourseSelections)
router.get('/student/:studentId', authMiddleware, selectionController.getStudentSelections)
router.post('/', authMiddleware, selectionController.createCourseSelection)
router.delete('/:id', authMiddleware, selectionController.deleteCourseSelection)

module.exports = router
