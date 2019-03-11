package com.example.firebaseapp;

import java.util.Date;

public class Comment {

   private String message;
   private Date timestamp;
   private String lessonId;

   public Comment(){}

   public Comment(String message, Date timestamp, String lessonId)
   {
       this.message = message;
       this.timestamp = timestamp;
       this.lessonId = lessonId;
   }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getLessonId() {
        return lessonId;
    }
}
