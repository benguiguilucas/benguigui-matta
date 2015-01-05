package com.game.kevin.onlinepong.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.ImageButton;


import games.thenewpong.newpong.R;


public class mainactivity extends Activity implements OnClickListener {

    String sharedName = "listHighScore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedpreferences = getSharedPreferences(sharedName, Activity.MODE_PRIVATE);
        if(!sharedpreferences.contains("first")) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("first", "Kevin");
            editor.putString("second", "Mario");
            editor.putString("third", "Bob");
            editor.putInt("firstS", 15);
            editor.putInt("secondS", 10);
            editor.putInt("thirdS", 0);
            editor.commit();
        }
        ImageButton newGame = (ImageButton) findViewById(R.id.newGame);
        newGame.setOnClickListener(this);
        ImageButton highScore = (ImageButton) findViewById(R.id.highScore);
        highScore.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        if(arg0.getId() == R.id.newGame){
            Intent intent = new Intent(this,gameactivity.class);
            this.startActivity(intent);
        }
        if(arg0.getId() == R.id.highScore){
            Intent intent = new Intent(this,highscoreactivity.class);
            this.startActivity(intent);
        }
    }
}
