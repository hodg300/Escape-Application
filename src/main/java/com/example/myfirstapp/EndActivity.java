package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {
    private TextView scoreView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);


        scoreView=findViewById(R.id.your_score);

        //press restart and return to a new game
        findViewById(R.id.btn_restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameActivityIntent=new Intent(EndActivity.this,GameActivity.class);
                startActivity(gameActivityIntent);
            }
        });

        Intent intent=getIntent();
        scoreView.setText("YOUR SCORE: " + intent.getIntExtra("score",0));
    }

    //press End Game to finish the game and destroy the progress
    public void clickExit(View view) {
        moveTaskToBack(true);
        System.exit(1);
    }
}
