package fsv.a5us.touristsimple;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDACallback;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static fsv.a5us.touristsimple.R.drawable.ic_refresh_white;

public class MainActivity extends AppCompatActivity {

    ArrayList<Attraction> attractions;
    ListView listView;
    ProgressBar mProgressBar;
    GlobalClass global;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        global = (GlobalClass) getApplicationContext();
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        listView = (ListView) findViewById(R.id.listViewAttractions);
        attractions = global.getAttractions();
        if( attractions.size() < 1 ) {
            LoadAttractions();
            global.setAttractions(attractions);
        } else {
            CustomListAdapter adapter = new CustomListAdapter(getApplicationContext(), R.layout.custom_list_layout, attractions);
            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d("Klik position: " + position, view.toString());
                Intent ActivityAttractionDetails = new Intent(MainActivity.this,AttractionDetailsActivity.class);
                Attraction clickedAttraction = attractions.get(position);
                ActivityAttractionDetails.putExtra("attraction", clickedAttraction);
                startActivity(ActivityAttractionDetails);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        global.setAttractions(new ArrayList<Attraction>());
        attractions = global.getAttractions();
        attractions.clear();
        listView.setAdapter(null);
        LoadAttractions();
        return super.onOptionsItemSelected(item);
    }

    public void LoadAttractions(){
        if( isOnline() ){
            LoadCMS task = new LoadCMS();
            task.execute();
        } else {
            Toast.makeText(MainActivity.this.getApplicationContext(), "Aby pobrać atrakcje, musisz połączyć się z siecią.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    class LoadCMS extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            CDAClient client;
            client = CDAClient.builder()
                    .setSpace("4o6al86apcwg")
                    .setToken("293cbb4fce22f7a74f71cce1aa4bdaa544d0832e29ecc19d748c4a19945997b8")
                    .build();

            CDAArray result = client.fetch(CDAEntry.class).all();

            for (CDAResource a : result.items()) {
                CDAEntry attraction = client.fetch(CDAEntry.class).one(a.id());
                attractions.add(new Attraction(
                        attraction.getField("attractionType").toString(),
                        attraction.getField("name").toString(),
                        attraction.getField("shortDescription").toString(),
                        attraction.getField("longDescription").toString(),
                        attraction.getField("photoUrl").toString()
                ));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
            CustomListAdapter adapter = new CustomListAdapter(getApplicationContext(), R.layout.custom_list_layout, attractions);
            listView.setAdapter(adapter);
            Toast.makeText(MainActivity.this.getApplicationContext(), "Znalazłem " + attractions.size() + " atrakcje.", Toast.LENGTH_SHORT).show();
        }
    }
}
