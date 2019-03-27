package com.example.firebaseapp;

import java.util.Date;
import java.util.List;

public class Rating {

    private int  valuee; //value rating
    private List<Integer> rate;

    public List<Integer> getRate() {
        return rate;
    }

    public void setRate(List<Integer> rate) {
        this.rate = rate;
    }

    public Rating(){ }
    public Rating(int valuee) { this.valuee = valuee; }
    public int  getValuee() {
        return valuee;
    }


}
