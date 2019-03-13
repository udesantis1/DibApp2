package com.example.firebaseapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Lesson extends LessonID
{
    private String lesson_name;
    private String courseID;

    public Lesson()
    {
    }

    public Lesson(String courseID)
    {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");     //Funziona
        String currentTime = df.format(Calendar.getInstance().getTime());
        lesson_name = "Lezione del " + currentTime;
        this.courseID = courseID;
    }

    public String getLesson_name() { return lesson_name; }

    public String getCourseID() {
        return courseID;
    } //Necessary
}
