package fsv.a5us.touristsimple;

import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.MapView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class AttractionDetailsActivity extends AppCompatActivity {

    Attraction attraction;
    TextView textViewLongDesc;
    ImageView imageView;
    ImageButton buttonInfo;
    ImageButton buttonCamera;
    ImageButton buttonMap;
    MapView mapView;
    Button buttonTakePhoto;
    Uri photoPath;
    ImageView takenPhoto;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_attraction_details);

        attraction = (Attraction) getIntent().getSerializableExtra("attraction");
        Log.d("Recieved attraction: ", attraction.getPhotoUrl());

        imageView = (ImageView) findViewById(R.id.imageViewAttratcionDetails);
        Log.d("photoUrl", attraction.getPhotoUrl());
        try{
            Picasso.with(this).load(attraction.getPhotoUrl()).into(imageView);
        }catch(Exception e){
            e.printStackTrace();
            imageView.setImageResource(R.mipmap.ic_launcher);
        }

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        int pixel = bitmap.getPixel(1, bitmap.getHeight() - 10);

        TextView textView = (TextView) findViewById(R.id.textViewNameAttratcionDetails);
        textView.setText(attraction.getName());
        textView.setTextColor(getContrastColor(pixel));

        textViewLongDesc = (TextView) findViewById(R.id.textViewAttractionLongDesc);
        textViewLongDesc.setText(attraction.getLongDescription());

        buttonInfo = (ImageButton) findViewById(R.id.ImageButton2);
        buttonCamera = (ImageButton) findViewById(R.id.ImageButton1);
        buttonMap = (ImageButton) findViewById(R.id.ImageButton3);
        buttonTakePhoto = (Button) findViewById(R.id.buttonTakePhoto);
        //mapView = (MapView) findViewById(R.id.mapView);

        textViewLongDesc.setVisibility(View.VISIBLE);
        //mapView.setVisibility(View.INVISIBLE);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    public static int getContrastColor(int color) {
        double y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
        return y >= 128 ? Color.BLACK : Color.WHITE;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent MainActvity = new Intent(AttractionDetailsActivity.this,MainActivity.class);
        startActivity(MainActvity);
        finish();
    }

    public void onClickImage(View view) {

    }

    public void onClickCamera(View view) {
        buttonInfo.setColorFilter(Color.parseColor("#A1A1A1"));
        buttonCamera.setColorFilter(Color.parseColor("#7649A7"));
        buttonMap.setColorFilter(Color.parseColor("#A1A1A1"));

        buttonTakePhoto.setVisibility(View.VISIBLE);
        textViewLongDesc.setVisibility(View.INVISIBLE);
        //mapView.setVisibility(View.INVISIBLE);
    }

    public void onClickInfo(View view) {
        textViewLongDesc.setText(attraction.getLongDescription());
        buttonInfo.setColorFilter(Color.parseColor("#7649A7"));
        buttonCamera.setColorFilter(Color.parseColor("#A1A1A1"));
        buttonMap.setColorFilter(Color.parseColor("#A1A1A1"));

        buttonTakePhoto.setVisibility(View.INVISIBLE);
        textViewLongDesc.setVisibility(View.VISIBLE);
        //mapView.setVisibility(View.INVISIBLE);
    }

    public void onClickMap(View view) {

        buttonInfo.setColorFilter(Color.parseColor("#A1A1A1"));
        buttonCamera.setColorFilter(Color.parseColor("#A1A1A1"));
        buttonMap.setColorFilter(Color.parseColor("#7649A7"));

        buttonTakePhoto.setVisibility(View.INVISIBLE);
        textViewLongDesc.setVisibility(View.INVISIBLE);
        //mapView.setVisibility(View.VISIBLE);
    }

    public void onClickTakePhoto(View view) {



    }
}
