package com.example.firebaseapp;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class LessonID {

    @Exclude
    public String LessonID;

    public <T extends LessonID> T withId(@NonNull final String id)
    {
        this.LessonID = id;
        return (T) this;
    }

}
