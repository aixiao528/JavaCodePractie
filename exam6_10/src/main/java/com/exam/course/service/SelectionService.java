package com.exam.course.service;

import com.exam.course.cache.CourseCache;
import com.exam.course.dao.CourseDao;
import com.exam.course.dao.EnrollmentDao;
import com.exam.course.dao.StudentDao;
import com.exam.course.exception.BusinessException;
import com.exam.course.model.Course;
import com.exam.course.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SelectionService {
    private final StudentDao studentDao;
    private final CourseDao courseDao;
    private final EnrollmentDao enrollmentDao;
    private final CourseCache courseCache;

    public SelectionService(
            StudentDao studentDao,
            CourseDao courseDao,
            EnrollmentDao enrollmentDao,
            CourseCache courseCache
    ) {
        this.studentDao = studentDao;
        this.courseDao = courseDao;
        this.enrollmentDao = enrollmentDao;
        this.courseCache = courseCache;
    }

    public void selectCourse(int studentId, int courseId) throws SQLException, BusinessException {
        try (Connection connection = ConnectionManager.getConnection()) {
            validateStudent(connection, studentId);
            Course course = requireCourse(connection, courseId, false);
            if (enrollmentDao.existsEnrollment(connection, studentId, courseId)) {
                throw new BusinessException("该学生已选过这门课。");
            }
            if (course.getSelectedCount() >= course.getCapacity()) {
                throw new BusinessException("课程余量不足，选课失败。");
            }
            if (enrollmentDao.insertEnrollment(connection, studentId, courseId) != 1
                    || courseDao.increaseSelectedCount(connection, courseId) != 1) {
                throw new BusinessException("选课失败，请稍后重试。");
            }
        }
        refreshCacheQuietly();
    }

    public void dropCourse(int studentId, int courseId) throws SQLException, BusinessException {
        try (Connection connection = ConnectionManager.getConnection()) {
            validateStudent(connection, studentId);
            requireCourse(connection, courseId, false);
            if (!enrollmentDao.existsEnrollment(connection, studentId, courseId)) {
                throw new BusinessException("学生未选该课程，无法退课。");
            }
            if (enrollmentDao.deleteEnrollment(connection, studentId, courseId) != 1
                    || courseDao.decreaseSelectedCount(connection, courseId) != 1) {
                throw new BusinessException("退课失败，请稍后重试。");
            }
        }
        refreshCacheQuietly();
    }

    public void transferCourse(int fromStudentId, int toStudentId, int courseId, boolean simulateFailure)
            throws SQLException, BusinessException {
        if (fromStudentId == toStudentId) {
            throw new BusinessException("转出学生和接收学生不能相同。");
        }

        try (Connection connection = ConnectionManager.getConnection()) {
            connection.setAutoCommit(false);
            try {
                validateStudent(connection, fromStudentId);
                validateStudent(connection, toStudentId);
                requireCourse(connection, courseId, true);

                if (!enrollmentDao.existsEnrollment(connection, fromStudentId, courseId)) {
                    throw new BusinessException("转出学生未选该课程，不能转课。");
                }
                if (enrollmentDao.existsEnrollment(connection, toStudentId, courseId)) {
                    throw new BusinessException("接收学生已选该课程，不能重复接收转课。");
                }

                // 先把课程名额写给接收学生，再删除转出学生的记录，确保名额不会被其他人抢占。
                if (enrollmentDao.insertEnrollment(connection, toStudentId, courseId) != 1) {
                    throw new BusinessException("接收学生选课记录创建失败。");
                }

                if (simulateFailure) {
                    throw new SQLException("模拟异常: 转课过程中发生错误，触发事务回滚。");
                }

                if (enrollmentDao.deleteEnrollment(connection, fromStudentId, courseId) != 1) {
                    throw new BusinessException("删除转出学生原选课记录失败。");
                }

                connection.commit();
            } catch (Exception exception) {
                connection.rollback();
                if (exception instanceof BusinessException businessException) {
                    throw businessException;
                }
                if (exception instanceof SQLException sqlException) {
                    throw sqlException;
                }
                throw new SQLException("转课失败: " + exception.getMessage(), exception);
            } finally {
                connection.setAutoCommit(true);
            }
        }
        refreshCacheQuietly();
    }

    public void batchSelectCourse(int courseId, List<Integer> studentIds) throws SQLException, BusinessException {
        Set<Integer> uniqueStudentIds = new LinkedHashSet<>(studentIds);
        if (uniqueStudentIds.isEmpty()) {
            throw new BusinessException("批量选课的学生列表不能为空。");
        }

        try (Connection connection = ConnectionManager.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Course course = requireCourse(connection, courseId, true);
                if (course.getRemainingSeats() < uniqueStudentIds.size()) {
                    throw new BusinessException("课程余量不足，批量选课整体失败。");
                }

                for (int studentId : uniqueStudentIds) {
                    validateStudent(connection, studentId);
                    if (enrollmentDao.existsEnrollment(connection, studentId, courseId)) {
                        throw new BusinessException("学生 " + studentId + " 已选该课程，批量选课失败。");
                    }
                }

                for (int studentId : uniqueStudentIds) {
                    enrollmentDao.insertEnrollment(connection, studentId, courseId);
                    if (courseDao.increaseSelectedCount(connection, courseId) != 1) {
                        throw new BusinessException("课程余量不足，批量选课失败。");
                    }
                }

                connection.commit();
            } catch (Exception exception) {
                connection.rollback();
                if (exception instanceof BusinessException businessException) {
                    throw businessException;
                }
                if (exception instanceof SQLException sqlException) {
                    throw sqlException;
                }
                throw new SQLException("批量选课失败: " + exception.getMessage(), exception);
            } finally {
                connection.setAutoCommit(true);
            }
        }
        refreshCacheQuietly();
    }

    private void validateStudent(Connection connection, int studentId) throws SQLException, BusinessException {
        if (!studentDao.existsById(connection, studentId)) {
            throw new BusinessException("学生不存在: " + studentId);
        }
    }

    private Course requireCourse(Connection connection, int courseId, boolean forUpdate)
            throws SQLException, BusinessException {
        Course course = forUpdate ? courseDao.findByIdForUpdate(connection, courseId) : courseDao.findById(connection, courseId);
        if (course == null) {
            throw new BusinessException("课程不存在: " + courseId);
        }
        return course;
    }

    private void refreshCacheQuietly() throws SQLException {
        courseCache.refresh();
    }
}
