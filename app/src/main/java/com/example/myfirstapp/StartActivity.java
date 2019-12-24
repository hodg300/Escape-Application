package com.example.myfirstapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
        statusCheck();
        requestLocationPermission();


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
                    if (ContextCompat.checkSelfPermission(StartActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        userName = editName.getText().toString();
                        if (userName.matches("")) {
                            Toast.makeText(StartActivity.this,
                                    "Enter your name", Toast.LENGTH_LONG).show();
                        } else {
                            Intent startActivityIntent = new Intent(StartActivity.this, GameActivity.class);
                            startActivityIntent.putExtra(CHECK_BOX, isCheckBox);
                            startActivityIntent.putExtra(NAME, editName.getText().toString());
                            startActivity(startActivityIntent);
                            finish();
                        }
                    } else {
                        requestLocationPermission();
                    }
                }
            });

}

    private void requestLocationPermission() {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}


