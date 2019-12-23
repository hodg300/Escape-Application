package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
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
    private int count=0;
    private String userName;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startGame=findViewById(R.id.btn_startGame);
        editName=findViewById(R.id.edit_name);

//        requestPermission();

        checkBox=(CheckBox) findViewById(R.id.check_btn);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count%2 !=0) {
                    isCheckBox = true;
                }else{
                    isCheckBox=false;
                }
            }
        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = editName.getText().toString();
                if(userName.matches("")) {
                    Toast.makeText(StartActivity.this,
                            "Enter your name", Toast.LENGTH_LONG).show();
                }else{
                    Intent startActivityIntent = new Intent(StartActivity.this, GameActivity.class);
                    startActivityIntent.putExtra(CHECK_BOX, isCheckBox);
                    startActivityIntent.putExtra(NAME, editName.getText().toString());
                    startActivity(startActivityIntent);
                    finish();
                }
            }
        });
}
    private void requestPermission() {
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                finish();
//                return ;
            }
        }
    }

}
