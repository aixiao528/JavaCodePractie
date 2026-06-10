package com.exam.course.dao;

import com.exam.course.model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {
    public Course findById(Connection connection, int courseId) throws SQLException {
        String sql = "SELECT id, name, credits, capacity, selected_count FROM courses WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return mapCourse(resultSet);
            }
        }
    }

    public Course findByIdForUpdate(Connection connection, int courseId) throws SQLException {
        String sql = "SELECT id, name, credits, capacity, selected_count FROM courses WHERE id = ? FOR UPDATE";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return mapCourse(resultSet);
            }
        }
    }

    public List<Course> findAll(Connection connection) throws SQLException {
        String sql = "SELECT id, name, credits, capacity, selected_count FROM courses ORDER BY id";
        List<Course> courses = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                courses.add(mapCourse(resultSet));
            }
        }
        return courses;
    }

    public int increaseSelectedCount(Connection connection, int courseId) throws SQLException {
        String sql = """
                UPDATE courses
                SET selected_count = selected_count + 1
                WHERE id = ? AND selected_count < capacity
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            return statement.executeUpdate();
        }
    }

    public int decreaseSelectedCount(Connection connection, int courseId) throws SQLException {
        String sql = """
                UPDATE courses
                SET selected_count = selected_count - 1
                WHERE id = ? AND selected_count > 0
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            return statement.executeUpdate();
        }
    }

    private Course mapCourse(ResultSet resultSet) throws SQLException {
        return new Course(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("credits"),
                resultSet.getInt("capacity"),
                resultSet.getInt("selected_count")
        );
    }
}
