package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class StartActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

}


    public void clickToStart(View view) {
        Intent gameActivityIntent=new Intent(StartActivity.this,GameActivity.class);
        startActivity(gameActivityIntent);
        finish();
    }


}
