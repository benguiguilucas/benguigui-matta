package com.game.kevin.onlinepong.activities;

import games.thenewpong.newpong.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.graphics.Point;
import android.os.SystemClock;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.content.Context;

import java.util.Random;

public class gameactivity extends Activity implements OnClickListener, SensorEventListener {
    private Handler frame = new Handler();
    //Velocity includes the speed and the direction of our sprite motion
    private Point ballVelocity;
    private int ballMaxX;
    private int ballMaxY;
    private int paddle1MaxX;
    private int paddle1MaxY;
    private boolean collision=true;
    private long startTime = 0L;
    private int score=1;

    //Data for checking highscore and saving it
    private String sharedName ="listHighScore";
    private String newPlayerName;
    private float tmpSec;

    //Timer
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private int secs = 0;
    private Handler customHandler = new Handler();


    //Sensor
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mLastX, mLastY, mLastZ;

    private boolean mInitialized, newHighscore=false;

    private static final int FRAME_RATE = 10; //100 frames per second

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Handler h = new Handler();
        ((Button)findViewById(R.id.restart_button)).setOnClickListener(this);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                initGfx();
            }
        }, 1000);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            mInitialized = true;
        } else {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    protected void onStop() {
        super.onStop();
    }


    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedTime / 1000);
            customHandler.postDelayed(this, 0);
        }
    };

    synchronized public void initGfx() {
        Point p1, p2,p3;
        secs=0;
        score=1;
        collision=true;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedTime = 0L;
        startTime = 0L;
        startTime = SystemClock.uptimeMillis();
        newHighscore=false;

         //Set our boundaries for the sprites
        ballMaxX = findViewById(R.id.the_canvas).getWidth() -
                ((display)findViewById(R.id.the_canvas)).getBallWidth();
        ballMaxY = findViewById(R.id.the_canvas).getHeight() -
                ((display)findViewById(R.id.the_canvas)).getBallHeight();

        paddle1MaxX = findViewById(R.id.the_canvas).getWidth() -
                ((display)findViewById(R.id.the_canvas)).getPaddle1Width();
        paddle1MaxY = findViewById(R.id.the_canvas).getHeight() -
                ((display)findViewById(R.id.the_canvas)).getPaddle1Height();
        do {
           p1 = new Point((int)(ballMaxX /2),(int)(ballMaxY /2));
            p3 = new Point(0,20);
            p2 = new Point((int)(paddle1MaxX /2), paddle1MaxY -20);
        } while (Math.abs(p1.x - p2.x) <
                ((display)findViewById(R.id.the_canvas)).getBallWidth());
        ((display)findViewById(R.id.the_canvas)).setBall(p1.x, p1.y);
        ((display)findViewById(R.id.the_canvas)).setPaddle1(p2.x, p2.y);
        ((display)findViewById(R.id.the_canvas)).setPaddle2(p3.x, p3.y);
        ballVelocity = new Point(1,1); //speed of the ball
       ((Button)findViewById(R.id.restart_button)).setEnabled(true);
        frame.removeCallbacks(frameUpdate);
        ((display)findViewById(R.id.the_canvas)).invalidate();
        frame.postDelayed(frameUpdate, FRAME_RATE);
    }
    @Override
    synchronized public void onClick(View v) {
        initGfx();
    }

    private Runnable frameUpdate = new Runnable() {
        @Override
        synchronized public void run() {
            ((TextView) findViewById(R.id.score)).setText("Time : "+secs);
            //Before we do anything else check for a collision
            if (((display) findViewById(R.id.the_canvas)).wasCollisionDetected()) {
               final SharedPreferences sharedpreferences = getSharedPreferences(sharedName, Activity.MODE_PRIVATE);
                score=secs;
                if(score>sharedpreferences.getInt("firstS",-1) || score>sharedpreferences.getInt("secondS",-1) || score>sharedpreferences.getInt("thirdS",-1) && (sharedpreferences.getInt("firstS",-1)!=-1 && sharedpreferences.getInt("secondS",-1)!=-1 && sharedpreferences.getInt("thirdS",-1)!=-1) )
                {
                    newHighscore=true;
                }

                if(newHighscore) {
                    //Prompt alert for getting the name
                    final SharedPreferences.Editor editor = sharedpreferences.edit();
                    AlertDialog.Builder alert = new AlertDialog.Builder(gameactivity.this);
                    alert.setTitle("New High Score: " + secs + " !" + "\n");
                    alert.setMessage("Write your name !");
                    alert.setCancelable(false);
                    alert.setInverseBackgroundForced(true);
                    final EditText input = new EditText(gameactivity.this);
                    alert.setView(input);
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Editable name  = input.getText();
                            newPlayerName = name.toString();
                            newPlayerName.replace("\n","");
                            if(newPlayerName==null)
                                newPlayerName="No Name";

                            if(score>sharedpreferences.getInt("firstS",-1) && newHighscore) {
                                editor.remove("third");editor.remove("thirdS");
                                editor.putString("third",sharedpreferences.getString("second",null));
                                editor.putInt("thirdS",sharedpreferences.getInt("secondS",-1));
                                editor.putString("second",sharedpreferences.getString("first",null));
                                editor.putInt("secondS",sharedpreferences.getInt("firstS",-1));
                                editor.putString("first",newPlayerName);
                                editor.putInt("firstS",score);
                                editor.commit();
                                newHighscore=false;
                            }
                            if(score>sharedpreferences.getInt("secondS",-1)&& newHighscore) {
                                editor.putString("third",sharedpreferences.getString("second",null));
                                editor.putInt("thirdS",sharedpreferences.getInt("secondS",-1));
                                editor.putString("second",newPlayerName);
                                editor.putInt("secondS",score);
                                editor.commit();
                                newHighscore=false;
                            }
                            if(score>sharedpreferences.getInt("thirdS",-1)&& newHighscore) {
                                editor.remove("third");editor.remove("thirdS");
                                editor.putString("third",newPlayerName);
                                editor.putInt("thirdS",score);
                                editor.commit();
                                newHighscore=false;
                            }
                        }
                    });
                    alert.show();
                }
                return;
            }

            if (((display) findViewById(R.id.the_canvas)).wasBounceDetected() && collision) {
                ballVelocity.y *= -1;
                ballVelocity.x += (new Random().nextInt(10))*(-1) + (new Random().nextInt(15)) ;
                tmpSec=secs;
                collision=false;
            }

            if(secs>tmpSec+0.2f)
                collision=true;

            frame.removeCallbacks(frameUpdate);
            Point ball = new Point(((display) findViewById(R.id.the_canvas)).getBallX(), ((display) findViewById(R.id.the_canvas)).getBallY());
            Point paddle1 = new Point(((display) findViewById(R.id.the_canvas)).getPaddle1X(), ((display) findViewById(R.id.the_canvas)).getPaddle1Y());
            Point paddle2 = new Point(((display) findViewById(R.id.the_canvas)).getPaddle2X(), ((display) findViewById(R.id.the_canvas)).getPaddle2Y());

            ball.x = ball.x + ballVelocity.x;
            if (ball.x > ballMaxX -2 || ball.x < 6) {
                ballVelocity.x *= -1;

            }

            ball.y = ball.y + ballVelocity.y;
            if (ball.y > ballMaxY || ball.y < 5) {
                ballVelocity.y *= -1;
            }
//Moving according to X
            if(mLastX<-5){
                mLastX=-5;
            }
            if(mLastX>5){
                mLastX=5;
            }
            paddle2.x = (int) ((-mLastX+5.0f)*paddle1MaxX/10.0f);
            paddle1.x = (int) ((-mLastX+5.0f)*paddle1MaxX/10.0f);

            int xDir = (ballVelocity.x > 0) ? 1 : -1;
            int yDir = (ballVelocity.y > 0) ? 1 : -1;

            //Changing the difficulty based on the time
            switch (secs){
                case 6:
                    ballVelocity.x = (5) * xDir;
                    ballVelocity.y = (5) * yDir;
                    break;
                case 10:
                    ballVelocity.x = (8) * xDir;
                    ballVelocity.y = (8) * yDir;
                    break;
                case 15:
                    if(paddle1.y > (int) paddle1MaxY *1/5) {
                        paddle1.y=paddle1.y-1;
                        paddle2.y=paddle2.y+1;
                    }
                    break;
                case 25:
                    if(paddle1.y < (int) paddle1MaxY -20) {
                        paddle1.y=paddle1.y+1;
                        paddle2.y=paddle2.y-1;
                    }
                    break;
                case 35:
                    ballVelocity.x = (14) * xDir;
                    ballVelocity.y = (14) * yDir;
                    break;
                case 45:
                    if(paddle1.y > (int) paddle1MaxY *4/5) {
                        paddle1.y=paddle1.y-1;
                        paddle2.y=paddle2.y+1;
                    }
                    break;
                case 60:
                    if(paddle1.y < (int) paddle1MaxY -20) {
                        paddle1.y=paddle1.y+1;
                        paddle2.y=paddle2.y-1;
                    }
                    break;
            }


            ((display)findViewById(R.id.the_canvas)).setBall(ball.x, ball.y);
            ((display)findViewById(R.id.the_canvas)).setPaddle1(paddle1.x, paddle1.y);
            ((display)findViewById(R.id.the_canvas)).setPaddle2(paddle2.x, paddle2.y);
            ((display)findViewById(R.id.the_canvas)).invalidate();
            frame.postDelayed(frameUpdate, FRAME_RATE);
        }
    };
}