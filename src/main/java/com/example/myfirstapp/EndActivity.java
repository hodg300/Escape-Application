package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        //press restart and return to a new game
        findViewById(R.id.btn_restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameActivityIntent=new Intent(EndActivity.this,GameActivity.class);
                startActivity(gameActivityIntent);
            }
        });

        //press End Game to finish the game and destroy the progress
        findViewById(R.id.btn_destroy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();

            }
        });
    }
}
