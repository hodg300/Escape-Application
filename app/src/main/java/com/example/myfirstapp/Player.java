package com.example.myfirstapp;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.Comparator;

public class Player {
    private String name;
    private Location location;
    private int score;


    public Player(String name,Location location,int score){
        this.name=name;
        this.location=location;
        this.score=score;

    }

    public Location getLocation() {
        return location;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }



    @Override
    public String toString() {
        return this.name + " " + this.location.getLatitude() + " " + this.location.getLongitude()  + " " +this.score + "\n";
    }



}
