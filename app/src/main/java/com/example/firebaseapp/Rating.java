package com.example.firebaseapp;

import java.util.Date;

public class Rating {

    private int  valuee; //value rating
    private String ratingID;
    private Date timestamp;

    public Rating(){

    }

    public Rating(int valuee) {
        this.valuee = valuee;

    }

    public String getRatingID() {
        return ratingID;
    }

    public int  getValuee() {
        return valuee;
    }

    public void setValuee(int valuee) {
        this.valuee = valuee;
    }
}
