package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //stop game and going to end screen
        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameActivityIntent=new Intent(GameActivity.this,EndActivity.class);
                startActivity(gameActivityIntent);
            }
        });
        final int NUM_OF_COL=3;
        final Button player=findViewById(R.id.btn_player);
        final Button btnLeft=findViewById(R.id.move_left);
        final Button btnRight=findViewById(R.id.move_right);
    //exmaple from web


        final GridView gv = (GridView) findViewById(R.id.gv);

        // Initializing a new String Array
        String[] plants = new String[]{
                "Catalina ironwood",
                "Cabinet cherry",
                "Pale corydalis",
                "Pink corydalis",
                "Belle Isle cress",
                "Land cress",
                "Orange coneflower",
                "Coast polypody",
                "Water fern"
        };

        // Populate a List from Array elements
        final List<String> plantsList = new ArrayList<String>(Arrays.asList(plants));

        // Data bind GridView with ArrayAdapter (String Array elements)
        gv.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, plantsList) {
            @Override
            public boolean isEnabled(int position) {
                // Disable the third item of GridView
                if (position == 2) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Return the GridView current item as a View
                View view = super.getView(position, convertView, parent);

                // Convert the view as a TextView widget
                TextView tv = (TextView) view;

                // Set the GridView third item text color different
                if(position == 2){
                    // set the GridView disable item color
                    tv.setTextColor(Color.DKGRAY);

                    tv.setVisibility(View.GONE);
                }
                else {
                    // set the TextView text color (GridView item color)
                    tv.setTextColor(Color.BLACK);
                    tv.setVisibility(View.VISIBLE);
                    tv.setBackgroundColor(Color.RED);
                }

                // Return the TextView widget as GridView item
                return tv;
            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the GridView selected/clicked item text
                String selectedItem = parent.getItemAtPosition(position).toString();

                // Display the selected/clicked item text
                Toast.makeText(getApplicationContext(), selectedItem,Toast.LENGTH_SHORT).show();
            }
        });


        //move left
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player.getX()>=360)
                    player.setX(player.getX()-getResources().getDisplayMetrics().widthPixels/NUM_OF_COL);

            }
        });
        //move right
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player.getX()<720)
                    player.setX(player.getX()+ getResources().getDisplayMetrics().widthPixels/NUM_OF_COL);
                Log.d("state", "onClick: "+ getResources().getDisplayMetrics().widthPixels/NUM_OF_COL);

            }
        });
    }
}
