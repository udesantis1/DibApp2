package com.example.firebaseapp;


public class Course extends CourseID{

    public String courseName;
    public String courseTime;

    public Course() {

    }

    public Course(String courseName) {
        this.courseName = courseName;

    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}