package fsv.a5us.touristsimple;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.test.mock.MockPackageManager;
import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Attraction> attractions;
    private ListView listView;
    private ProgressBar mProgressBar;
    private GlobalClass global;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    // GPSTracker class
    GPSTracker gps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                // execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        global = (GlobalClass) getApplicationContext();
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        listView = (ListView) findViewById(R.id.listViewAttractions);
        attractions = global.getAttractions();
        if (attractions.size() < 1) {
            LoadAttractions();
            global.setAttractions(attractions);
        } else {

            CustomListAdapter adapter = new CustomListAdapter(getApplicationContext(), R.layout.custom_list_layout, attractions);
            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent ActivityAttractionDetails = new Intent(MainActivity.this, AttractionDetailsActivity.class);
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

        switch (item.getItemId()) {
            case R.id.itemRefresh:
                global.setAttractions(new ArrayList<Attraction>());
                attractions = global.getAttractions();
                attractions.clear();
                listView.setAdapter(null);
                LoadAttractions();
                break;
            case R.id.itemGps:
                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                            + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
                break;
        }
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
            Location myLocation = new Location("myl");
            gps = new GPSTracker(MainActivity.this);
/*            float lon1 = (float)gps.getLongitude();
            float lat1 = (float) gps.getLatitude();*/
            myLocation.setLatitude(gps.getLatitude());
            myLocation.setLongitude(gps.getLongitude());
            myLocation.setAltitude(0);
            myLocation.setBearing(0);
            myLocation.setExtras(null);
            myLocation.setAccuracy(0);
            myLocation.setTime(0);
            myLocation.setAccuracy(0);
            myLocation.setElapsedRealtimeNanos(0);
            myLocation.setSpeed(0);
            myLocation.setProvider(null);

            for (CDAResource a : result.items()) {
                CDAEntry attraction = client.fetch(CDAEntry.class).one(a.id());

                String loc = attraction.getField("coordinate").toString();
                float lon2 = Float.parseFloat(loc.substring(loc.indexOf("lon=")+4, loc.indexOf(",")));
                float lat2 = Float.parseFloat(loc.substring(loc.indexOf("lat=")+4, loc.indexOf("}")));
                Location atrLocation = new Location("atrloc");
                atrLocation.setLongitude(lon2);
                atrLocation.setLatitude(lat2);
                atrLocation.setAltitude(0);
                atrLocation.setBearing(0);
                atrLocation.setExtras(null);
                atrLocation.setAccuracy(0);
                atrLocation.setTime(0);
                atrLocation.setAccuracy(0);
                atrLocation.setElapsedRealtimeNanos(0);
                atrLocation.setSpeed(0);
                atrLocation.setProvider(null);
                float dist;
                if(myLocation!=null){
                    dist = myLocation.distanceTo(atrLocation);
                    Log.d("Distance: ", String.valueOf(dist));
                    dist = dist / 1000000;
/*                    DecimalFormat df = new DecimalFormat("0.00");
                    dist  = new Float(df.format(dist)).floatValue();*/
                } else{
                    dist = Float.valueOf("9.9");
                }

/*                int R = 6371; // km
                float x = (float) ((lon2 - lon1) * Math.cos((lat1 + lat2) / 2));
                float y = (lat2 - lat1);
                float dist = (float)(Math.sqrt(x * x + y * y) * R);*/


                attractions.add(new Attraction(
                        attraction.getField("attractionType").toString(),
                        attraction.getField("name").toString(),
                        attraction.getField("shortDescription").toString(),
                        attraction.getField("longDescription").toString(),
                        attraction.getField("photoUrl").toString(),
                        lon2,
                        lat2,
                        dist
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
            Toast.makeText(MainActivity.this.getApplicationContext(), attractions.size() + " attraction available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
