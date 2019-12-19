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
    public final String CHECK_BOX = "check_box";


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
                Intent intent= getIntent();
                boolean check_box= intent.getBooleanExtra(CHECK_BOX,false);
                EndActivityIntent.putExtra(CHECK_BOX,check_box);
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
        topTen(intent.getStringExtra(NAME),intent.getIntExtra(SCORE,0));
        saveData();


    }

    private void topTen(String name,int score){

            if(players_list.size()<10 ) {
                findPlace(name,score);


            }else{
                if(score >= players_list.get(players_list.size()-1).getScore()) {
                    players_list.remove(players_list.size() - 1);
                    findPlace(name,score);
                }
            }

    }

    private void findPlace(String name,int score) {
        if(players_list.size()==0){
            players_list.add(new Player(name,score));
        }else {
            for (Player p : players_list) {
                if (score > p.getScore()) {
                    players_list.add(players_list.indexOf(p), new Player(name,score));
                    break;
                }
            }
            if(score < players_list.get(players_list.size()-1).getScore()){
                players_list.add(players_list.size(),new Player(name,score));
            }
        }
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



    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
