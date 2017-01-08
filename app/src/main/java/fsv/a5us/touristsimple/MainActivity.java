package fsv.a5us.touristsimple;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    ArrayList<Attraction> attractions;
    ListView listView;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        attractions = new ArrayList<>();
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        listView = (ListView) findViewById(R.id.listViewAttractions);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Klik position: " + position, view.toString());

                Intent ActivityAttractionDetails = new Intent(MainActivity.this,AttractionDetailsActivity.class);
                Attraction clickedAttraction = attractions.get(position);
                ActivityAttractionDetails.putExtra("attraction", clickedAttraction);
                startActivity(ActivityAttractionDetails);
                finish();
            }
        });

        LoadAttractions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        attractions.clear();
        listView.setAdapter(null);
        LoadAttractions();
        return super.onOptionsItemSelected(item);
    }

    private void LoadAttractions(){
        mProgressBar.setVisibility(View.VISIBLE);
        if( isOnline() ){
            ReadJSON task = new ReadJSON();
            task.execute("https://quarkbackend.com/getfile/tbuslowski/json1");
        } else{
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this.getApplicationContext(), "Aby pobrać zdjęcia, musisz się połączyć z siecią.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    class ReadJSON extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... params) {
            return readURL(params[0]);
        }

        @Override
        protected void onPostExecute(String content) {
            try {
                JSONObject jsonObject = new JSONObject(content);
                JSONArray jsonArray = jsonObject.getJSONArray("attraction");
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject attractionObject = jsonArray.getJSONObject(i);
                    attractions.add(new Attraction(
                            attractionObject.getString("attractionType"),
                            attractionObject.getString("name"),
                            attractionObject.getString("shortDescription"),
                            attractionObject.getString("longDescription"),
                            attractionObject.getString("photoURL")
                    ));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            CustomListAdapter adapter = new CustomListAdapter(getApplicationContext(), R.layout.custom_list_layout, attractions);
            listView.setAdapter(adapter);
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this.getApplicationContext(), "Znalazłem " + attractions.size() + " atrakcje.", Toast.LENGTH_SHORT).show();
        }
    }


    private static String readURL(String jsonUrl){
        StringBuilder content = new StringBuilder();
        HttpURLConnection connection = null;
        try{
            URL dataUrl = new URL(jsonUrl);
            connection = (HttpURLConnection) dataUrl.openConnection();
            connection.connect();
            int status = connection.getResponseCode();
            if( status == 200 ){
                InputStream is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while( (line = reader.readLine()) != null ){
                    content = content.append(line + "\n");
                }
            }
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return content.toString();
    }
}
