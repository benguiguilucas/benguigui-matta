package com.game.kevin.designedapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import java.util.ArrayList;

/**
 * Created by kevin on 03/01/2015.
 */

public class ViewActivity extends HomeActivity implements View.OnClickListener {
    TextView etResponse;
    ListView listview;

    String[] values = new String[] { "", "", "", "", "", "", "", "","", ""};
    int id=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork() // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        etResponse = (TextView) findViewById(R.id.etResponse);
        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask().execute("http://binouze.fabrigli.fr/bieres/1.json");
        new HttpAsyncTask().execute("http://binouze.fabrigli.fr/bieres/2.json");
        new HttpAsyncTask().execute("http://binouze.fabrigli.fr/bieres/3.json");
        new HttpAsyncTask().execute("http://binouze.fabrigli.fr/bieres/4.json");
        new HttpAsyncTask().execute("http://binouze.fabrigli.fr/bieres/5.json");
        new HttpAsyncTask().execute("http://binouze.fabrigli.fr/bieres/6.json");
        new HttpAsyncTask().execute("http://binouze.fabrigli.fr/bieres/7.json");
        new HttpAsyncTask().execute("http://binouze.fabrigli.fr/bieres/8.json");
        new HttpAsyncTask().execute("http://binouze.fabrigli.fr/bieres/9.json");
        new HttpAsyncTask().execute("http://binouze.fabrigli.fr/bieres/10.json");

        listview = (ListView) findViewById(R.id.beerView);



        ActionBar actionBar = getActionBar(); //Create the back button, to go back to the previous activity
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view, menu);
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
            sendMessage();
        }
        if (id == R.id.action_help) { //Launch the help activity
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ViewActivity.class);
        startActivity(intent);
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public void implementList() {


        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }

        });



    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            try {
                JSONObject mainObject = new JSONObject(result);
               // JSONObject imageObject = mainObject.getJSONObject("image");
                String  imageName = mainObject.getString("name");
                values[id]=imageName;id++;
                implementList();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
