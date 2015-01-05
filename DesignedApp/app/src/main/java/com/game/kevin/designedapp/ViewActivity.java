package com.game.kevin.designedapp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import android.os.AsyncTask;
import android.util.Log;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by kevin on 03/01/2015.
 */

public class ViewActivity extends HomeActivity implements View.OnClickListener {
    ListView listview;boolean isFull=false;
    ImageView imageBeer,imageFullScreen;
    String[] values = new String[] { "", "", "", "", "", "", "", "","", ""};
    int id=0;boolean isImage=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        imageBeer = (ImageView) findViewById(R.id.imageView);
        imageFullScreen = (ImageView) findViewById(R.id.imageView2);
        View backgroundImage = findViewById(R.id.background);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(50);

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

        imageBeer.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) { //Fullscreen
                 isFull=true;
                 imageFullScreen.setVisibility(View.VISIBLE);
                 imageBeer.setVisibility(View.INVISIBLE);
                 listview.setVisibility(View.INVISIBLE);
             }
        });

        imageFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //not fullscreen
                if(isFull) {
                    imageFullScreen.setVisibility(View.INVISIBLE);
                    imageBeer.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.VISIBLE);
                    isFull=false;
                }
            }
        });

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
            search();
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
                isImage=true;int tmp=position+1;
                //Get the picture of the clicked name and show it
                new HttpAsyncTask().execute("http://binouze.fabrigli.fr/bieres/"+tmp+".json");

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
            if(!isImage) {
                try {
                    JSONObject mainObject = new JSONObject(result);
                    // JSONObject imageObject = mainObject.getJSONObject("image");
                    String imageName = mainObject.getString("name");
                    values[id] = imageName;
                    id++;
                    implementList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    JSONObject mainObject = new JSONObject(result);
                    JSONObject imageObject = mainObject.getJSONObject("image");
                    JSONObject imageObject2 = imageObject.getJSONObject("image");
                    String imageUrl = imageObject2.getString("url");
                   // values[1] = imageUrl;
                    GetXMLTask task = new GetXMLTask();
                    // Execute the task
                    task.execute(new String[] { "http://binouze.fabrigli.fr" + imageUrl });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            imageBeer.setImageBitmap(result);
            imageFullScreen.setImageBitmap(result);imageFullScreen.setVisibility(View.INVISIBLE);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }
}
