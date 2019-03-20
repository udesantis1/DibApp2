package com.example.firebaseapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Lesson extends LessonID
{
    private String lesson_name;
    private String courseID;
    private int countUser = 0;
    private List<String> usersList; //rendere set, ma fa crashare

    public Lesson()
    {
    }

    public Lesson(String courseID)
    {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");     //Funziona
        String currentTime = df.format(Calendar.getInstance().getTime());
        lesson_name = "Lezione del " + currentTime;
        usersList = new ArrayList<>();
        this.courseID = courseID;
    }

    public String getLesson_name() { return lesson_name; }

    public String getCourseID() {
        return courseID;
    } //Necessary

    public void setCountUser(int countUser) { this.countUser = countUser; }

    public int getCountUser() { return countUser;}

    public List<String> getUsersList() {
        return usersList;
    }

    public void addUser(String email)
    {
        usersList.add(email);
    }
}
