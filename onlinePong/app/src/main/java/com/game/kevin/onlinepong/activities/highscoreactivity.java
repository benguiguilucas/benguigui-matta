package com.game.kevin.onlinepong.activities;

import android.app.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import games.thenewpong.newpong.R;


public class highscoreactivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        SharedPreferences sharedpreferences = getSharedPreferences("listHighScore", Activity.MODE_PRIVATE);

        if (sharedpreferences.contains("first"))
        {
            ((TextView) findViewById(R.id.listplayer    )).setText("\n"+sharedpreferences.getString("first", null)+"\n"+sharedpreferences.getString("second", null)+"\n"+sharedpreferences.getString("third", null));
            ((TextView) findViewById(R.id.listscore)).setText("\n"+sharedpreferences.getInt("firstS",-1)+"\n"+sharedpreferences.getInt("secondS",-1)+"\n"+sharedpreferences.getInt("thirdS",-1));
        }
        else {
            ((TextView) findViewById(R.id.listscore)).setText("Something went wrong");
        }
    }
}
