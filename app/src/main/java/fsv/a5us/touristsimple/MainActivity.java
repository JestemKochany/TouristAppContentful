package fsv.a5us.touristsimple;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.internal.FusedLocationProviderResult;

import java.math.BigDecimal;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private FusedLocationProviderApi locationProviderApi = LocationServices.FusedLocationApi;
    private Double mLatitude;
    private Double mLongitude;
    private Location mLastLocation;

    private ArrayList<Attraction> attractions;
    private ListView listView;
    private ProgressBar mProgressBar;
    private GlobalClass global;


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

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        locationRequest = new LocationRequest();
        locationRequest.setInterval(2 * 1000); // 2 secs
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Log.d("onStart ", mGoogleApiClient.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        requestLocationUpdates();
        Log.d("On connected ", "OK");
    }

    private void requestLocationUpdates() {
        Log.i("requestLocationUpdates" , "start");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("onLocationChanged" , "start");
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        for(Attraction a: attractions){
            float [] dist = new float[1];
            Log.d("Moja pozycja: ", mLatitude + " " + mLongitude);
            Log.d("Odcz pozycja: ", a.getLatitude() + " " + a.getLongitude());
            Location.distanceBetween(location.getLatitude(), location.getLongitude(), a.getLatitude(), a.getLongitude(), dist);
            a.setDistacne(round(dist[0]/1000, 1));
        }
        CustomListAdapter adapter = new CustomListAdapter(getApplicationContext(), R.layout.custom_list_layout, attractions);
        listView.setAdapter(adapter);
        Log.d("requestLocation", "Updated");
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
                Log.d("Field: ", attraction.getField("coordinate").toString());
                String loc = attraction.getField("coordinate").toString();
                Log.d("String loc: ", loc);
                float lon2 = Float.parseFloat(loc.substring(loc.indexOf("lon=")+4, loc.indexOf(",")));
                float lat2 = Float.parseFloat(loc.substring(loc.indexOf("lat=")+4, loc.indexOf("}")));
                float dist;
                dist = Float.valueOf("9");

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

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
