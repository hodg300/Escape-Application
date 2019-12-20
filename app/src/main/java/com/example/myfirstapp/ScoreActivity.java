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

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {
    public static final String SHARE_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private int count =1;
    private ArrayList<Player> players_list;
    private TextView line1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        players_list = new ArrayList<>();
        updateViews();

    }

    private void updateViews() {
        SharedPreferences sharedPref = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
        line1 = (TextView) findViewById(R.id.line1);
        Gson gson = new Gson();
        String json = sharedPref.getString(TEXT, null);
        Log.d("json", "updateViews: " + json);
        Type type = new TypeToken<ArrayList<Player>>() {
        }.getType();
        players_list = gson.fromJson(json, type);


        StringBuilder builder = new StringBuilder();
        for (Player details : players_list) {
            builder.append("#" + count + "     "+ details + "\n");
            count++;
        }

        line1.setText(builder.toString());
        if (players_list == null) {
            players_list = new ArrayList<>();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
