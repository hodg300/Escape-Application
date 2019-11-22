package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private int NUM_OF_COL = 3;
    private View player;
    private Button btnLeft;
    private Button btnRight;
    private LinearLayout main_layout;
    private LinearLayout l_layout1;
    private LinearLayout l_layout2;
    private LinearLayout l_layout3;
    private View enemy1;
    private View enemy2;
    private View enemy3;
    private ImageView life_status1;
    private ImageView life_status2;
    private ImageView life_status3;
    private int life = 3;
    private ValueAnimator animation1;
    private ValueAnimator animation2;
    private ValueAnimator animation3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //create handler thread


        player = (View) findViewById(R.id.player);
        btnLeft = findViewById(R.id.move_left);
        btnRight = findViewById(R.id.move_right);
        main_layout=(LinearLayout) findViewById(R.id.main_layout);
        l_layout1 = (LinearLayout) findViewById(R.id.linear1);
        l_layout2 = (LinearLayout) findViewById(R.id.linear2);
        l_layout3 = (LinearLayout) findViewById(R.id.linear3);
        enemy1 = (View) findViewById(R.id.enemy1);
        enemy2 = (View) findViewById(R.id.enemy2);
        enemy3 = (View) findViewById(R.id.enemy3);
        life_status1 = (ImageView) findViewById(R.id.life_status1);
        life_status2 = (ImageView) findViewById(R.id.life_status2);
        life_status3 = (ImageView) findViewById(R.id.life_status3);
        Log.d("hod", "onCreate: " + main_layout.getHeight());



        //stop game and going to end screen
        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation1.pause();
                animation2.pause();
                animation3.pause();
                Intent gameActivityIntent = new Intent(GameActivity.this, EndActivity.class);
                startActivity(gameActivityIntent);
            }
        });


        //move left
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (player.getX() >= (getResources().getDisplayMetrics().widthPixels * 1 / NUM_OF_COL))
                    player.setX(player.getX() - getResources().getDisplayMetrics().widthPixels / NUM_OF_COL);
                Log.d("state", "onClick: " + player.getX());
            }
        });
        //move right
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.getX() < (getResources().getDisplayMetrics().widthPixels * 2 / NUM_OF_COL))
                    player.setX(player.getX() + getResources().getDisplayMetrics().widthPixels / NUM_OF_COL);
                Log.d("state", "onClick: " + player.getX());

            }
        });

        // getHeight dont het me the real height checkkkk it!!!
        animation1 = ValueAnimator.ofFloat(-130f,1500f);
        animation1.setDuration(4000).setRepeatCount(Animation.INFINITE);
        animation1.start();
        animation1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                // You can use the animated value in a property that uses the
                // same type as the animation. In this case, you can use the
                // float value in the translationX property.
                float animatedValue = (float)updatedAnimation.getAnimatedValue();
                enemy1.setTranslationY(animatedValue);


            }
        });
        animation2 = ValueAnimator.ofFloat(-130f,1500f);
        animation2.setDuration(3000).setRepeatCount(Animation.INFINITE);
        animation2.start();
        animation2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                // You can use the animated value in a property that uses the
                // same type as the animation. In this case, you can use the
                // float value in the translationX property.
                float animatedValue = (float)updatedAnimation.getAnimatedValue();

                enemy2.setTranslationY(animatedValue);


            }
        });

        animation3 = ValueAnimator.ofFloat(-130f,1500f);
        animation3.setDuration(5000).setRepeatCount(Animation.INFINITE);
        animation3.start();
        animation3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                // You can use the animated value in a property that uses the
                // same type as the animation. In this case, you can use the
                // float value in the translationX property.
                float animatedValue = (float)updatedAnimation.getAnimatedValue();
                Log.d("enemy3", "onAnimationUpdate: " + enemy3.getY() + " " + player.getX() +" " +player.getY());
                enemy3.setTranslationY(animatedValue);
                hitCheck(enemy3);
            }
        });

    }


//        //here there is a problem , i need to check getx and gety
    private void hitCheck(View v) {
        if (v.getX() == player.getX() && v.getY()>(player.getY() - player.getHeight())) {
            switch (life) {
                case 1:
                    life_status1.setVisibility(View.INVISIBLE);
                    animation1.pause();
                    animation2.pause();
                    animation3.pause();
                    Intent gameActivityIntent = new Intent(GameActivity.this, EndActivity.class);
                    startActivity(gameActivityIntent);
                    break;
                case 2:
                    life_status2.setVisibility(View.INVISIBLE);
                    this.life--;
                    break;
                case 3:
                    life_status3.setVisibility(View.INVISIBLE);
                    this.life--;
                    break;
            }
        }
        return;
    }
}

