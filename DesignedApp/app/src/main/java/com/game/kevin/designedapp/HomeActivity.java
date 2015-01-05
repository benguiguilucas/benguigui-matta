package com.game.kevin.designedapp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.Button;


public class HomeActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button button = (Button)findViewById(R.id.showList);
        button.setOnClickListener(this);

        ActionBar actionBar = getActionBar(); //Create the back button, to go back to the previous activity
        actionBar.setDisplayHomeAsUpEnabled(true);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        menu.findItem(R.id.action_search).getActionView();
        
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_search) { //TODO: find how to validate from the action bar
            search();
        }
        if (id == R.id.action_help) { //Launch the help activity
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    //Couldn't find how...
    public void search() {
        Intent intent = new Intent(this, SearchActivity.class);
       // EditText editText = (EditText) findViewById(R.id.edit_message);
       // String message = editText.getText().toString();
       // intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ViewActivity.class);
        startActivity(intent);
    }
}
