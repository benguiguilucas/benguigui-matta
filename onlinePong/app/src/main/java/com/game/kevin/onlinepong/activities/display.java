package com.game.kevin.onlinepong.activities;

import games.thenewpong.newpong.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class display extends View{
    private Paint p;

    private Rect ballBounds = new Rect(0,0,0,0);
    private Rect paddle1Bounds = new Rect(0,0,0,0);
    private Rect paddle2Bounds = new Rect(0,0,0,0);
    private Point ball;
    private Point paddle1;
    private Point paddle2;
    private Bitmap bitmapBall = null;
    private Bitmap bitmapPaddle1 = null;
    private Bitmap bitmapPaddle2 = null;
    //Collision flag and point
    private boolean collisionDetected = false;

    synchronized public void setBall(int x, int y) {
        ball =new Point(x,y);
    }
    synchronized public int getBallX() { return ball.x;}
    synchronized public int getBallY() {
        return ball.y;
    }

    synchronized public Bitmap getBitmapBall() {
        return bitmapBall;
    }
    synchronized public Bitmap getBitmapPaddle1() {
        return bitmapPaddle1;
    }
    synchronized public Bitmap getBitmapPaddle2() {
        return bitmapPaddle2;
    }

    synchronized public void setPaddle1(int x, int y) {paddle1 =new Point(x,y);}
    synchronized public int getPaddle1X() {
        return paddle1.x;
    }
    synchronized public int getPaddle1Y() {
        return paddle1.y;
    }

    synchronized public void setPaddle2(int x, int y) {
        paddle2 =new Point(x,y);
    }
    synchronized public int getPaddle2X() {
        return paddle2.x;
    }
    synchronized public int getPaddle2Y() {
        return paddle2.y;
    }


    synchronized public int getBallWidth() {
        return ballBounds.width();
    }
    synchronized public int getBallHeight() {
        return ballBounds.height();
    }

    synchronized public int getPaddle1Width() { return paddle1Bounds.width(); }
    synchronized public int getPaddle1Height() {
        return paddle1Bounds.height();
    }

    synchronized public int getPaddle2Width() {
        return paddle2Bounds.width();
    }
    synchronized public int getPaddle2Height() {return paddle2Bounds.height(); }

    synchronized public boolean wasCollisionDetected() {
        return collisionDetected;
    }

    public display(Context context, AttributeSet aSet) {
        super(context, aSet);
        p = new Paint();
        ball = new Point(-1,-1);
        paddle1 = new Point(-1,-1);
        paddle2 = new Point(-1,-1);

        p = new Paint();
        bitmapBall = BitmapFactory.decodeResource(getResources(),R.drawable.ball);
        bitmapPaddle1 = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);
        bitmapPaddle2 = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);
        ballBounds = new Rect(0,0, bitmapBall.getWidth(), bitmapBall.getHeight());
        paddle1Bounds = new Rect(0,0, bitmapPaddle1.getWidth(), bitmapPaddle1.getHeight());
        paddle2Bounds = new Rect(0,0, bitmapPaddle2.getWidth(), bitmapPaddle2.getHeight());
    }

    private boolean checkForCollision() {
        if (ball.x<0 && paddle1.x<0 && ball.y<0 && paddle1.y<0) return false;
        int sprite1MaxY = findViewById(R.id.the_canvas).getHeight() - ((display)findViewById(R.id.the_canvas)).getBallHeight();
        return ball.y > sprite1MaxY || ball.y < 5;
    }
    public boolean wasBounceDetected() {
        Rect r1 = new Rect(ball.x, ball.y, ball.x
                + ((display) findViewById(R.id.the_canvas)).getBallWidth(), ball.y + ((display) findViewById(R.id.the_canvas)).getBallHeight());
        Rect r2 = new Rect(paddle1.x, paddle1.y, paddle1.x +
                ((display) findViewById(R.id.the_canvas)).getPaddle1Width(), paddle1.y + ((display) findViewById(R.id.the_canvas)).getPaddle1Height());
        Rect r3 = new Rect(r1);

        if (r1.intersect(r2)) {
            for (int i = r1.left; i < r1.right; i++) {
                for (int j = r1.top; j < r1.bottom; j++) {
                    if (((display) findViewById(R.id.the_canvas)).getBitmapBall().getPixel(i - r3.left, j - r3.top) !=
                            Color.TRANSPARENT) {
                        if (((display) findViewById(R.id.the_canvas)).getBitmapPaddle1().getPixel(i - r2.left, j - r2.top) !=
                                Color.TRANSPARENT) {
                            return true;
                        }
                    }
                }
            }
        }


        r1 = new Rect(ball.x, ball.y, ball.x
                + ((display) findViewById(R.id.the_canvas)).getBallWidth(), ball.y + ((display) findViewById(R.id.the_canvas)).getBallHeight());
        r2 = new Rect(paddle2.x, paddle2.y, paddle2.x +
                ((display) findViewById(R.id.the_canvas)).getPaddle2Width(), paddle2.y + ((display) findViewById(R.id.the_canvas)).getPaddle2Height());
        r3 = new Rect(r1);

        if (r1.intersect(r2)) {
            for (int i = r1.left; i < r1.right; i++) {
                for (int j = r1.top; j < r1.bottom; j++) {
                    if (((display) findViewById(R.id.the_canvas)).getBitmapBall().getPixel(i - r3.left, j - r3.top) !=
                            Color.TRANSPARENT) {
                        if (((display) findViewById(R.id.the_canvas)).getBitmapPaddle2().getPixel(i - r2.left, j - r2.top) !=
                                Color.TRANSPARENT) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }



    @Override
    synchronized public void onDraw(Canvas canvas) {
        p.setColor(Color.rgb(252, 245, 182));
       // p.setAlpha(255);

       // p.setStrokeWidth(1);
        canvas.drawRect(0, 0, getWidth(), getHeight(), p);

        if (ball.x>=0) {
            canvas.drawBitmap(bitmapBall, ball.x, ball.y, null);
        }
        if (paddle1.x>=0) {
            canvas.drawBitmap(bitmapPaddle1, paddle1.x, paddle1.y, null);
        }
        if (paddle2.x>=0) {
            canvas.drawBitmap(bitmapPaddle2, paddle2.x, paddle2.y, null);
        }

        collisionDetected = checkForCollision();
    }
}