package com.exam.course.dao;

import com.exam.course.model.EnrollmentDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDao {
    public boolean existsEnrollment(Connection connection, int studentId, int courseId) throws SQLException {
        String sql = "SELECT 1 FROM enrollments WHERE student_id = ? AND course_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public int insertEnrollment(Connection connection, int studentId, int courseId) throws SQLException {
        String sql = "INSERT INTO enrollments(student_id, course_id, grade) VALUES (?, ?, NULL)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            return statement.executeUpdate();
        }
    }

    public int deleteEnrollment(Connection connection, int studentId, int courseId) throws SQLException {
        String sql = "DELETE FROM enrollments WHERE student_id = ? AND course_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            return statement.executeUpdate();
        }
    }

    public List<EnrollmentDetail> findStudentCourseDetails(Connection connection, int studentId) throws SQLException {
        String sql = """
                SELECT c.id, c.name, c.credits, COALESCE(CAST(e.grade AS CHAR), '未录入') AS grade
                FROM enrollments e
                JOIN courses c ON e.course_id = c.id
                WHERE e.student_id = ?
                ORDER BY c.id
                """;
        List<EnrollmentDetail> details = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    details.add(new EnrollmentDetail(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("credits"),
                            resultSet.getString("grade")
                    ));
                }
            }
        }
        return details;
    }
}
