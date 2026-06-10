package com.exam.course.service;

import com.exam.course.dao.EnrollmentDao;
import com.exam.course.dao.StudentDao;
import com.exam.course.exception.BusinessException;
import com.exam.course.model.EnrollmentDetail;
import com.exam.course.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class QueryService {
    private final StudentDao studentDao;
    private final EnrollmentDao enrollmentDao;

    public QueryService(StudentDao studentDao, EnrollmentDao enrollmentDao) {
        this.studentDao = studentDao;
        this.enrollmentDao = enrollmentDao;
    }

    public List<EnrollmentDetail> queryStudentCourses(int studentId) throws SQLException, BusinessException {
        try (Connection connection = ConnectionManager.getConnection()) {
            if (!studentDao.existsById(connection, studentId)) {
                throw new BusinessException("学生不存在: " + studentId);
            }
            return enrollmentDao.findStudentCourseDetails(connection, studentId);
        }
    }
}
