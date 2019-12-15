package com.example.myfirstapp;

import android.location.Location;

import androidx.annotation.NonNull;

import java.util.Comparator;

public class Player {
    private String name;
    private int rating=1;
    private int score;
    private Location location;

    public Player(){

    }
    public Player(int score){
        this.score=score;

    }
    public Player(String name,Location location,int score){
        this.name=name;
        this.location=location;
        this.score=score;

    }

    public Location getLocation() {
        return location;
    }

    public int getRating() {
        return rating;
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

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setScore(int score) {
        this.score = score;
    }



    @Override
    public String toString() {
        return "#" + (this.rating++) + " " + this.name + " " + this.location + " " +this.score + "\n";
    }



}
