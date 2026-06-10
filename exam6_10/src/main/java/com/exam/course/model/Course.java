package com.exam.course.model;

public class Course {
    private final int id;
    private final String name;
    private final int credits;
    private final int capacity;
    private final int selectedCount;

    public Course(int id, String name, int credits, int capacity, int selectedCount) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.capacity = capacity;
        this.selectedCount = selectedCount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public int getRemainingSeats() {
        return capacity - selectedCount;
    }
}
