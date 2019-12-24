package com.example.myfirstapp;

import android.location.Location;



public class Player {
    private String name;
    private double lattitude;
    private double longitude;
    private int score;
    private Location location;


    public Player(String name,Location location,int score){
        this.name=name;
        this.lattitude=location.getLatitude();
        this.longitude=location.getLongitude();
        this.score=score;
        this.location=location;

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
    public Location getLocation(){
        return location;
    }




    @Override
    public String toString() {
        return this.name + " " + (float)this.lattitude + " " + (float)this.longitude + " " +this.score + "\n";
    }



}
