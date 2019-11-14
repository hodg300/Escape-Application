package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //press click on start and you are going to the game
        findViewById(R.id.btn_startGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button urlEditTxt=findViewById(R.id.btn_startGame);
                Intent gameActivityIntent=new Intent(StartActivity.this,GameActivity.class);
                startActivity(gameActivityIntent);
            }
        });

    }
}
