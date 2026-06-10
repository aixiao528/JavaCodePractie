package com.exam.course.model;

public class EnrollmentDetail {
    private final int courseId;
    private final String courseName;
    private final int credits;
    private final String grade;

    public EnrollmentDetail(int courseId, String courseName, int credits, String grade) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.grade = grade;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredits() {
        return credits;
    }

    public String getGrade() {
        return grade;
    }
}
