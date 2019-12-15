package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EndActivity extends AppCompatActivity {
    public static final String SHARE_PREFS="sharedPrefs";
    public static final String TEXT="text";
    private TextView scoreView;
    private final String SCORE="score";
    private final String NAME="name";
    private ArrayList<Player> players_list;
    private Location location;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        players_list=new ArrayList<>();
        scoreView=findViewById(R.id.your_score);
        Intent intent=getIntent();
        scoreView.setText("YOUR SCORE : " + intent.getIntExtra(SCORE,0));

        findViewById(R.id.btn_start_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameActivityIntent=new Intent(EndActivity.this,StartActivity.class);
                startActivity(gameActivityIntent);
                finish();
            }
        });

        findViewById(R.id.btn_restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EndActivityIntent=new Intent(EndActivity.this,GameActivity.class);
                startActivity(EndActivityIntent);
                finish();
            }
        });

        findViewById(R.id.btn_quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
                //System.exit(1);
                finish();
            }
        });

        findViewById(R.id.btn_high_scores).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EndActivityIntent=new Intent(EndActivity.this,ScoreActivity.class);
                startActivity(EndActivityIntent);

            }
        });

        loadData();
        topTen(intent.getIntExtra(SCORE,0));
        saveData();


    }

    private void topTen(int score){

            if(players_list.size()<10 ) {
                players_list.add(new Player(score));
            }else{
                findWhoToClear();
            }

    }

    private void findWhoToClear() {

    }

    private void saveData(){
        SharedPreferences sharedPref = getSharedPreferences(SHARE_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json=gson.toJson(players_list);
        editor.putString(TEXT,json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPref = getSharedPreferences(SHARE_PREFS,MODE_PRIVATE);
        Gson gson = new Gson();
        String json=sharedPref.getString(TEXT,null);
        Type type= new TypeToken<ArrayList<Player>>(){}.getType();
        players_list=gson.fromJson(json,type);
        if(players_list ==null){
            players_list=new ArrayList<>();
        }
    }

}
