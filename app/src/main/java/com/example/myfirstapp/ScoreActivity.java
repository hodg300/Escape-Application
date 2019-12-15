package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {
    public static final String SHARE_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private int count=1;
    private ArrayList<Player> players_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        players_list = new ArrayList<>();
        SharedPreferences sharedPref = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
        TextView t = (TextView) findViewById(R.id.test);
        Gson gson = new Gson();
        String json = sharedPref.getString(TEXT, null);
        Type type = new TypeToken<ArrayList<Player>>() {
        }.getType();
        players_list = gson.fromJson(json, type);
        for(Player p:players_list){
            t.setText("#" + (count) + " " + p.getName() + " " + p.getLocation()+ " " +p.getScore()+ "\n");
            count++;
        }
        t.setText(players_list.toString());
        if (players_list == null) {
            players_list = new ArrayList<>();
//        }

        }
    }
}
