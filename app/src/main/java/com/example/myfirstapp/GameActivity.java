package com.example.myfirstapp;



import androidx.appcompat.app.AppCompatActivity;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class GameActivity extends AppCompatActivity implements SensorEventListener {
    private static final int ADD_SCORE_FROM_ENEMY = 1;
    private final int BONUS_SCORE=5;
    private final int NUM_OF_COL = 5;
    public final String CHECK_BOX = "check_box";
    private final String SCORE = "score";
    private final String TEXT_SCORE = "SCORE: ";
    private final String NAME="name";
    private final static int MAX_VOLUME = 100;
    private final float volume = (float) (1 - (Math.log(MAX_VOLUME - 5) / Math.log(MAX_VOLUME)));
    private int countOfPressedBack=0;
    private int speed;
    private RelativeLayout relativeLayout;
    private View player;
    private View[] enemies;
    private View[] bonus_staff;
    private ImageView[] life_status;
    private ImageView life_status2;
    private ImageView life_status3;
    private ImageView btnLeft;
    private ImageView btnRight;
    private int life = 3;
    private ValueAnimator[] enemyArr;
    private ValueAnimator[] bonusArr;
    private int screenHeight;
    private int screenwidth;
    private int score=0;
    private TextView scoreView;
    private MediaPlayer mpBackground;
    //sensor
    private SensorManager sensorManager;
    private Sensor accelerometer;
    static int x = 360;
    private boolean isSensor;
    private boolean level2=true;
    private boolean level3=true;
    private boolean level4=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toast.makeText(GameActivity.this,
                "Get away from the asteroids", Toast.LENGTH_SHORT).show();
        //MediaPlayer mpBackground
        startMusicBackground();

        initialViews();
        screenHeightAndWidth();
        enemyArr=new ValueAnimator[5];
        bonusArr=new ValueAnimator[5];

        //make always screen light
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        onClickResumeOrPauseOrStop();
        buttonsOrSensor();
        //create bonus animations and create enemies animation
        bonusAnimate();
        enemiesAnimate();

    }



    ///-----------------------Method--------------------------------------
    private void screenHeightAndWidth(){
        //get screenHeight and width
        WindowManager wm=getWindowManager();
        Display disp= wm.getDefaultDisplay();
        Point size=new Point();
        disp.getSize(size);
        screenHeight=size.y;
        screenwidth=size.x;
    }
    private void buttonsOrSensor(){
        Intent intent=getIntent();
        //play with buttons
        if(!(intent.getBooleanExtra(CHECK_BOX,false))) {
            isSensor=false;
            speed=3000;
            //move right
            btnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (player.getX() < (getResources().getDisplayMetrics().widthPixels * (NUM_OF_COL - 1) / NUM_OF_COL))
                        player.setX(player.getX() + getResources().getDisplayMetrics().widthPixels / NUM_OF_COL);
                    scoreView.setTextColor(Color.WHITE);
                }
            });


            //move left
            btnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (player.getX() >= (getResources().getDisplayMetrics().widthPixels * 1 / NUM_OF_COL))
                        player.setX(player.getX() - getResources().getDisplayMetrics().widthPixels / NUM_OF_COL);
                    scoreView.setTextColor(Color.WHITE);
                }
            });

        }else{//play with Motion Sensors

            btnLeft.setVisibility(View.INVISIBLE);
            btnRight.setVisibility(View.INVISIBLE);
            isSensor=true;
            speed=5000;
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    private void initialViews(){ //initial views
        relativeLayout=(RelativeLayout)findViewById(R.id.rl);
        player = (View) findViewById(R.id.player);

        enemies=new View[5];
        enemies[0] = (View) findViewById(R.id.enemy1);
        enemies[1] = (View) findViewById(R.id.enemy2);
        enemies[2] = (View) findViewById(R.id.enemy3);
        enemies[3] = (View) findViewById(R.id.enemy4);
        enemies[4] = (View) findViewById(R.id.enemy5);

        bonus_staff=new View[5];
        bonus_staff[0]  = (View) findViewById(R.id.coin1);
        bonus_staff[1]  = (View) findViewById(R.id.coin2);
        bonus_staff[2]  = (View) findViewById(R.id.coin3);
        bonus_staff[3]  = (View) findViewById(R.id.coin4);
        bonus_staff[4]  = (View) findViewById(R.id.coin5);

        life_status=new ImageView[3];
        life_status[0] = (ImageView) findViewById(R.id.life_status1);
        life_status[1]  = (ImageView) findViewById(R.id.life_status2);
        life_status[2]  = (ImageView) findViewById(R.id.life_status3);
        scoreView=findViewById(R.id.score_view);

        //initial Buttons
        btnLeft=(ImageView) findViewById(R.id.move_left);
        btnRight=(ImageView) findViewById(R.id.move_right);

        //initial score
        scoreView.setText(TEXT_SCORE + '0');

        //initial enemies position
        for(int i=0;i<NUM_OF_COL;i++) {
            enemies[i].setTranslationY(-130f);
            bonus_staff[i].setTranslationY(-260f);
        }
    }

    private void startMusicBackground(){
        mpBackground = MediaPlayer.create(getApplicationContext(),R.raw.starwars);
        mpBackground.setLooping(true);
        mpBackground.setVolume(volume, volume);
        mpBackground.start();
    }

    //make problem with switch between level---------------------------------------------
//    private void changeBackgroundAndMoreSpeed(){
//        if(score==0) {
//            Toast.makeText(GameActivity.this,
//                    "Level 1", Toast.LENGTH_LONG).show();
//        }
//        else if(score>=50 && score <=55 &&level2){
//            level2=false;
//            relativeLayout.setBackground(getResources().getDrawable(R.drawable.level2));
//            Toast.makeText(GameActivity.this,
//                    "Level 2", Toast.LENGTH_LONG).show();
//            if(isSensor){
//                speed=4500;
//                setSpeedOnAnimation(speed);
//            }else{
//                speed=2500;
//                setSpeedOnAnimation(speed);
//            }
//        }else if(score>=150 && score<=155 && level3){
//            level3=false;
//            relativeLayout.setBackground(getResources().getDrawable(R.drawable.level3));
//            Toast.makeText(GameActivity.this,
//                    "Level 3", Toast.LENGTH_LONG).show();
//            if(isSensor){
//                speed=4000;
//                setSpeedOnAnimation(speed);
//            }else{
//                speed=2000;
//                setSpeedOnAnimation(speed);
//            }
//        }else if(score>=300 && score <=310 && level4){
//            level4=false;
//            relativeLayout.setBackground(getResources().getDrawable(R.drawable.level4));
//            Toast.makeText(GameActivity.this,
//                    "Final level", Toast.LENGTH_LONG).show();
//            if(isSensor){
//                speed=3500;
//                setSpeedOnAnimation(speed);
//            }else{
//                speed=1500;
//                setSpeedOnAnimation(speed);
//            }
//        }
//    }
//
//    //update speed animation
//    private void setSpeedOnAnimation(int speedAnimate){
//        for(int i=0;i<NUM_OF_COL;i++){
//            enemyArr[i].setDuration(speed).setRepeatCount(Animation.INFINITE);
//            bonusArr[i].setDuration(speed).setRepeatCount(Animation.INFINITE);
//        }
//    }
    //-----------------------------------------------------------------

    private synchronized void addScore(View enemy,ValueAnimator updatedAnimation){
        if(enemy.getY()>player.getY()+player.getHeight()){
//            changeBackgroundAndMoreSpeed();
            score +=ADD_SCORE_FROM_ENEMY;
            scoreView.setText(TEXT_SCORE + score);
            updatedAnimation.start();
        }
    }


    private synchronized void addBonusScore(){
        score += BONUS_SCORE;
        scoreView.setText(TEXT_SCORE + score +" +5");
    }


//check collision and update hearts--------------------------
    private synchronized  void hitCheck() {
            this.life--;
                if (life == 0) {
                    life_status[0].setVisibility(View.INVISIBLE);
                    Intent intent=getIntent();
                    Intent gameActivityIntent = new Intent(GameActivity.this, EndActivity.class);
                    gameActivityIntent.putExtra(SCORE,score);
                    gameActivityIntent.putExtra(CHECK_BOX,intent.getBooleanExtra(CHECK_BOX,false));
                    gameActivityIntent.putExtra(NAME,intent.getStringExtra(NAME));
                    startActivity(gameActivityIntent);
                    finish();
                } else if (life == 1) {
                    life_status[1].setVisibility(View.INVISIBLE);

                } else if (life == 2) {
                    life_status[2].setVisibility(View.INVISIBLE);

                }
        return;
    }

    private boolean isCollision(View e,View p) {
        int[] enemy_locate = new int[2];
        int[] player_locate = new int[2];

        e.getLocationOnScreen(enemy_locate);
        p.getLocationOnScreen(player_locate);

        Rect rect1=new Rect(enemy_locate[0]+50,enemy_locate[1],(int)(enemy_locate[0]+ e.getWidth()-50),(int)(enemy_locate[1]+e.getHeight()));
        Rect rect2=new Rect(player_locate[0]+50,player_locate[1],(int)(player_locate[0]+ p.getWidth()-50),(int)(player_locate[1]+p.getHeight()));

        return Rect.intersects(rect1,rect2);
    }
//-----------------------------------


    private void onClickResumeOrPauseOrStop(){

        //resume
        findViewById(R.id.btn_resume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<NUM_OF_COL;i++){
                    enemyArr[i].resume();
                    bonusArr[i].resume();
                }

                findViewById(R.id.move_left).setEnabled(true);
                findViewById(R.id.move_right).setEnabled(true);
                mpBackground.start();
                if(isSensor) {
                    sensorManager.registerListener(GameActivity.this, accelerometer,
                            SensorManager.SENSOR_DELAY_FASTEST);
                }
            }
        });

        //pause
        findViewById(R.id.btn_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
            }
        });

        //stop
        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStop();
                Intent gameActivityIntent = new Intent(GameActivity.this, EndActivity.class);
                gameActivityIntent.putExtra(SCORE,score);
                gameActivityIntent.putExtra(CHECK_BOX,getIntent().getBooleanExtra(CHECK_BOX,false));
                gameActivityIntent.putExtra(NAME,getIntent().getStringExtra(NAME));
                startActivity(gameActivityIntent);
                finish();
            }
        });
    }

//---------------------------------


    @Override
    protected void onPause() {
        super.onPause();
        for(int i=0;i<NUM_OF_COL;i++){
            enemyArr[i].pause();
            bonusArr[i].pause();
        }
        findViewById(R.id.move_left).setEnabled(false);
        findViewById(R.id.move_right).setEnabled(false);
        mpBackground.pause();
        if (isSensor) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!findViewById(R.id.btn_pause).isEnabled()) {
            mpBackground.start();
        }
//        changeBackgroundAndMoreSpeed();
        countOfPressedBack=0;
    }

    @Override
    protected void onStop() {
        super.onStop();
        for(int i=0;i<NUM_OF_COL;i++){
            enemyArr[i].pause();
            bonusArr[i].pause();
        }

        mpBackground.pause();

    }



    @Override
    public void onBackPressed() {
        if(countOfPressedBack==0) {
            onPause();
            Toast.makeText(GameActivity.this,
                    "Press again to exit", Toast.LENGTH_SHORT).show();
            countOfPressedBack++;
        }else{
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x -= (int) event.values[0];
            if (x >= 0 && x < screenwidth - player.getWidth()) {
                player.setX(x);
            }
            else if(x<0 ){
                x=0;
            }else if(x>screenwidth - player.getWidth()){
                x=screenwidth - player.getWidth();
            }

        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void bonusAnimate(){
        bonusArr[0] = ValueAnimator.ofInt(-260, screenHeight + 400);
        bonusArr[0].setDuration(speed).setRepeatCount(Animation.INFINITE);
        bonusArr[0].setStartDelay(10000);
        bonusArr[0].start();
        bonusArr[0].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                int animatedValue = (int) updatedAnimation.getAnimatedValue();
                bonus_staff[0].setTranslationY(animatedValue);
                if (isCollision(bonus_staff[0], player)) {
                    addBonusScore();
                    bonus_staff[0].setY(-130);
                    updatedAnimation.start();
                }
            }
        });

        bonusArr[1] = ValueAnimator.ofInt(-260, screenHeight + 400);
        bonusArr[1].setDuration(speed).setRepeatCount(Animation.INFINITE);
        bonusArr[1].setStartDelay(22000);
        bonusArr[1].start();
        bonusArr[1].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                int animatedValue = (int) updatedAnimation.getAnimatedValue();
                bonus_staff[1].setTranslationY(animatedValue);
                if (isCollision(bonus_staff[1], player)) {
                    addBonusScore();
                    bonus_staff[1].setY(-130);
                    updatedAnimation.start();
                }
            }
        });

        bonusArr[2] = ValueAnimator.ofInt(-260, screenHeight + 400);
        bonusArr[2].setDuration(speed).setRepeatCount(Animation.INFINITE);
        bonusArr[2].setStartDelay(10000);
        bonusArr[2].start();
        bonusArr[2].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                int animatedValue = (int) updatedAnimation.getAnimatedValue();
                bonus_staff[2].setTranslationY(animatedValue);
                if (isCollision(bonus_staff[2], player)) {
                    addBonusScore();
                    bonus_staff[2].setY(-130);
                    updatedAnimation.start();
                }
            }
        });

        bonusArr[3] = ValueAnimator.ofInt(-260, screenHeight + 400);
        bonusArr[3].setDuration(speed).setRepeatCount(Animation.INFINITE);
        bonusArr[3].setStartDelay(17000);
        bonusArr[3].start();
        bonusArr[3].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                int animatedValue = (int) updatedAnimation.getAnimatedValue();
                bonus_staff[3].setTranslationY(animatedValue);
                if (isCollision(bonus_staff[3], player)) {
                    addBonusScore();
                    bonus_staff[3].setY(-130);
                    updatedAnimation.start();
                }
            }
        });

        bonusArr[4] = ValueAnimator.ofInt(-260, screenHeight + 400);
        bonusArr[4].setDuration(speed).setRepeatCount(Animation.INFINITE);
        bonusArr[4].setStartDelay(12000);
        bonusArr[4].start();
        bonusArr[4].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                int animatedValue = (int) updatedAnimation.getAnimatedValue();
                bonus_staff[4].setTranslationY(animatedValue);
                if (isCollision(bonus_staff[4], player)) {
                    addBonusScore();
                    bonus_staff[4].setY(-130);
                    updatedAnimation.start();
                }
            }
        });
    }

    private void enemiesAnimate() {
        enemyArr[0] = ValueAnimator.ofInt(-130,screenHeight+400);
        enemyArr[0].setDuration(speed).setRepeatCount(Animation.INFINITE);
        enemyArr[0].setStartDelay(200);
        enemyArr[0].start();
        enemyArr[0].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                int animatedValue = (int)updatedAnimation.getAnimatedValue();
                enemies[0].setTranslationY(animatedValue);
                if(isCollision(enemies[0],player)) {
                    enemies[0].setY(-130);
                    hitCheck();
                    updatedAnimation.start();
                }
                addScore(enemies[0],updatedAnimation);

            }
        });

        enemyArr[1] = ValueAnimator.ofInt(-130,screenHeight +400);
        enemyArr[1].setDuration(speed).setRepeatCount(Animation.INFINITE);
        enemyArr[1].setStartDelay(2200);
        enemyArr[1].start();
        enemyArr[1].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                int animatedValue = (int)updatedAnimation.getAnimatedValue();

                enemies[1].setTranslationY(animatedValue);
                if(isCollision(enemies[1],player)) {
                    enemies[1].setY(-130);
                    hitCheck();
                    updatedAnimation.start();
                }
                addScore(enemies[1],updatedAnimation);
            }
        });

        enemyArr[2] = ValueAnimator.ofInt(-130,screenHeight +400);
        enemyArr[2].setDuration(speed).setRepeatCount(Animation.INFINITE);
        enemyArr[2].setStartDelay(1100);
        enemyArr[2].start();
        enemyArr[2].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                int animatedValue = (int)updatedAnimation.getAnimatedValue();
                enemies[2].setTranslationY(animatedValue);
                if(isCollision(enemies[2],player)) {
                    enemies[2].setY(-130);
                    hitCheck();
                    updatedAnimation.start();
                }
                addScore(enemies[2],updatedAnimation);
            }
        });


        enemyArr[3] = ValueAnimator.ofInt(-130,screenHeight +400);
        enemyArr[3].setDuration(speed).setRepeatCount(Animation.INFINITE);
        enemyArr[3].setStartDelay(1500);
        enemyArr[3].start();
        enemyArr[3].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                int animatedValue = (int)updatedAnimation.getAnimatedValue();
                enemies[3].setTranslationY(animatedValue);
                if(isCollision(enemies[3],player)) {
                    enemies[3].setY(-130);
                    hitCheck();
                    updatedAnimation.start();
                }
                addScore(enemies[3],updatedAnimation);
            }
        });



        enemyArr[4] = ValueAnimator.ofInt(-130,screenHeight +400);
        enemyArr[4].setDuration(speed).setRepeatCount(Animation.INFINITE);
        enemyArr[4].setStartDelay(1300);
        enemyArr[4].start();
        enemyArr[4].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                int animatedValue = (int)updatedAnimation.getAnimatedValue();
                enemies[4].setTranslationY(animatedValue);
                if(isCollision(enemies[4],player)) {
                    enemies[4].setY(-130);
                    hitCheck();
                    updatedAnimation.start();
                }
                addScore(enemies[4],updatedAnimation);
            }
        });

    }
}


