package com.example.myfirstapp;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.Comparator;

public class Player {
    private String name;
    private double lattitude;
    private double longttude;
    private int score;


    public Player(String name,Location location,int score){
        this.name=name;
        this.lattitude=location.getLatitude();
        this.longttude=location.getLongitude();
        this.score=score;

    }



    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }



    @Override
    public String toString() {
        return this.name + " " + (float)this.lattitude + " " + (float)this.longttude + " " +this.score + "\n";
    }



}
