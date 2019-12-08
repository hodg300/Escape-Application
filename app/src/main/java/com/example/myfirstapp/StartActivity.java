package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;


public class StartActivity extends AppCompatActivity {
    private CheckBox checkBox;
    private boolean isCheckBox=false;

    public final String CHECK_BOX = "check_box";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkBox=(CheckBox) findViewById(R.id.check_btn);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheckBox=true;
            }
        });
}


    public void clickToStart(View view) {
        Intent gameActivityIntent=new Intent(StartActivity.this,GameActivity.class);
        gameActivityIntent.putExtra(CHECK_BOX,isCheckBox);
        startActivity(gameActivityIntent);
        finish();
    }


}
