package com.example.firebaseapp;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class CourseID {

    @Exclude
    public String CourseID;

    public <T extends CourseID> T withId(@NonNull final String id)
    {
        this.CourseID = id;
        return (T) this;
    }


}
