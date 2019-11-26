package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Rectangle;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
    private View enemy1;
    private View enemy2;
    private View enemy3;
    private Button btnLeft;
    private Button btnRight;
    private ImageView life_status1;
    private ImageView life_status2;
    private ImageView life_status3;
    private int life = 3;
    private ValueAnimator animation1;
    private ValueAnimator animation2;
    private ValueAnimator animation3;
    private int screenHeight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //create handler thread


        player = (View) findViewById(R.id.player);
        btnLeft = findViewById(R.id.move_left);
        btnRight = findViewById(R.id.move_right);
        enemy1 = (View) findViewById(R.id.enemy1);
        enemy2 = (View) findViewById(R.id.enemy2);
        enemy3 = (View) findViewById(R.id.enemy3);
        life_status1 = (ImageView) findViewById(R.id.life_status1);
        life_status2 = (ImageView) findViewById(R.id.life_status2);
        life_status3 = (ImageView) findViewById(R.id.life_status3);



        //get screenHeight
        WindowManager wm=getWindowManager();
        Display disp= wm.getDefaultDisplay();
        Point size=new Point();
        disp.getSize(size);
        screenHeight=size.y;

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

        //create animation
        animation1 = ValueAnimator.ofInt(-130,screenHeight);
        animation1.setDuration(10000).setRepeatCount(Animation.INFINITE);
        animation1.start();
        animation1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                int animatedValue = (int)updatedAnimation.getAnimatedValue();
                enemy1.setTranslationY(animatedValue);
//              if(isCollision(enemy1,player))
//                    hitCheck();
            }
        });
        animation2 = ValueAnimator.ofInt(-130,screenHeight);
        animation2.setDuration(5000).setRepeatCount(Animation.INFINITE);
        animation2.start();
        animation2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                int animatedValue = (int)updatedAnimation.getAnimatedValue();

                enemy2.setTranslationY(animatedValue);
//              if(isCollision(enemy2,player))
//                    hitCheck();
            }
        });

        animation3 = ValueAnimator.ofInt(-130,screenHeight);
        animation3.setDuration(8000).setRepeatCount(Animation.INFINITE);
        animation3.start();
        animation3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                int animatedValue = (int)updatedAnimation.getAnimatedValue();
                enemy3.setTranslationY(animatedValue);
                if(isCollision(enemy3,player))
                    hitCheck();

            }
        });


    }

    private void hitCheck() {
            this.life--;
                if (life == 0) {
                    life_status1.setVisibility(View.INVISIBLE);
                    animation1.pause();
                    animation2.pause();
                    animation3.pause();
                    Intent gameActivityIntent = new Intent(GameActivity.this, EndActivity.class);
                    startActivity(gameActivityIntent);
                } else if (life == 1) {
                    life_status2.setVisibility(View.INVISIBLE);

                } else if (life == 2) {
                    life_status3.setVisibility(View.INVISIBLE);

                }

        return;
    }

    private boolean isCollision(View e,View p) {
        int[] enemy_locate = new int[2];
        int[] player_locate = new int[2];

        e.getLocationOnScreen(enemy_locate);
        p.getLocationOnScreen(player_locate);

        Log.d("enemy", "isCollision enemy : " + e.getX() +" " + e.getY());
        Log.d("player", "isCollision player : " + p.getX() +" " + p.getY());
//        Log.d("sis", "isCollosion1: " + enemy_locate[0] + " " + enemy_locate[1]);
//        Log.d("sis", "isCollosion2: " + player_locate[0] + " " + player_locate[1]);
        Rect rect1=new Rect(enemy_locate[0],enemy_locate[1],e.getRight(),e.getHeight()+enemy_locate[0]);
        Log.d("rect1", "isCollision: " + enemy_locate[0] + " " +enemy_locate[1] + " " + e.getWidth() + " "+ e.getHeight()+" " + enemy_locate[0] );
        Rect rect2=new Rect(player_locate[0],player_locate[1],p.getRight(),p.getHeight()+player_locate[0]);
        Log.d("rect2", "isCollision: " + player_locate[0] + " " +player_locate[1] + " " + p.getWidth() + " "+ p.getHeight()+" " + player_locate[0] );


        Log.d("answer", "isCollision: " + Rect.intersects(rect1,rect2));

        return Rect.intersects(rect1,rect2);
    }
}

