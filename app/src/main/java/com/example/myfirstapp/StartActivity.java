package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;


public class StartActivity extends AppCompatActivity {
    private CheckBox checkBox;
    private boolean isCheckBox=false;
    private EditText editName;
    private ImageView startGame;
    public final String CHECK_BOX = "check_box";
    public final String NAME = "name";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startGame=findViewById(R.id.btn_startGame);
        editName=findViewById(R.id.edit_name);

        checkBox=(CheckBox) findViewById(R.id.check_btn);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheckBox=true;
            }
        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent startActivityIntent = new Intent(StartActivity.this, GameActivity.class);
                    startActivityIntent.putExtra(CHECK_BOX, isCheckBox);
                if(editName.getText().toString() ==""){
                    startActivityIntent.putExtra(NAME,"Player");
                }else{
                    startActivityIntent.putExtra(NAME,editName.getText().toString());
                }
                    startActivity(startActivityIntent);
                    finish();

            }
        });
}


}
