package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {
    private TextView scoreView;
    private final String SCORE="score";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        scoreView=findViewById(R.id.your_score);
        Intent intent=getIntent();
        scoreView.setText("YOUR SCORE : " + intent.getIntExtra(SCORE,0));

    }

    //press End Game to finish the game and destroy the progress
    public void clickExit(View view) {
        moveTaskToBack(true);
        System.exit(1);
    }

    public void cliclToRestart(View view) {
        Intent gameActivityIntent=new Intent(EndActivity.this,StartActivity.class);
        startActivity(gameActivityIntent);
        finish();
    }
}
