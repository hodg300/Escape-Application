package com.example.myfirstapp;

import android.location.Location;

import androidx.annotation.NonNull;

public class Player {
    private String name;
    private int rating;
    private int score;
    private Location location;

    public Player(){

    }
    public Player(int score){
        this.score=score;
    }
    public Player(String name,int rating,int score,Location location){
        this.name=name;
        this.rating=rating;
        this.score=score;
        this.location=location;
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

    @NonNull
    @Override
    public String toString() {
        return "Rating " + (this.rating) + " Name " + this.name + " Location " + this.location + " Score " +this.score + "\n";
    }
}
