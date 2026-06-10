package com.exam.course.cache;

import com.exam.course.dao.CourseDao;
import com.exam.course.model.Course;
import com.exam.course.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CourseCache {
    private final CourseDao courseDao;
    private final ConcurrentMap<Integer, Course> cache = new ConcurrentHashMap<>();
    private volatile long lastRefreshTime;

    public CourseCache(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public List<Course> getCourses() throws SQLException {
        if (cache.isEmpty()) {
            refresh();
        }
        return cache.values().stream()
                .sorted((left, right) -> Integer.compare(left.getId(), right.getId()))
                .toList();
    }

    public void refresh() throws SQLException {
        try (Connection connection = ConnectionManager.getConnection()) {
            List<Course> courses = courseDao.findAll(connection);
            cache.clear();
            for (Course course : courses) {
                cache.put(course.getId(), course);
            }
            lastRefreshTime = System.currentTimeMillis();
        }
    }

    public long getLastRefreshTime() {
        return lastRefreshTime;
    }
}
