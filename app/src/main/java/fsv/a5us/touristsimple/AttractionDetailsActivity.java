package fsv.a5us.touristsimple;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class AttractionDetailsActivity extends AppCompatActivity {

    Attraction attraction;
    TextView textViewLongDesc;
    TextView attractionName;
    ImageView imageView;
    ImageButton buttonInfo;
    ImageButton buttonCamera;
    ImageButton buttonMap;
    MapView mapView;
    Button buttonTakePhoto;
    Button buttonOpenMaps;
    ImageView newPhoto;
    ImageButton shareButton;
    ImageButton deleteButton;
    TextView distanceInfo;

    private static final int CAMERA_REQUEST = 1888;



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
        imageView = (ImageView) findViewById(R.id.imageViewAttratcionDetails);


        try{
            Picasso.with(this).load(attraction.getPhotoUrl()).into(imageView);
        }catch(Exception e){
            Log.d("Błąd wczytywania linku", "Link pewnie nie dziala");
            e.printStackTrace();
            imageView.setImageResource(R.mipmap.ic_launcher);
        }

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        int pixel = bitmap.getPixel(1, bitmap.getHeight() - 10);

        attractionName = (TextView) findViewById(R.id.textViewNameAttratcionDetails);
        attractionName.setText(attraction.getName());
        attractionName.setTextColor(getContrastColor(pixel));

        newPhoto = (ImageView) findViewById(R.id.imageViewNewPhoto);
        newPhoto.setImageDrawable(null);
        buttonInfo = (ImageButton) findViewById(R.id.ImageButton2);
        buttonCamera = (ImageButton) findViewById(R.id.ImageButton1);
        buttonMap = (ImageButton) findViewById(R.id.ImageButton3);
        buttonTakePhoto = (Button) findViewById(R.id.buttonTakePhoto);
        textViewLongDesc = (TextView) findViewById(R.id.textViewAttractionLongDesc);
        textViewLongDesc.setText(attraction.getLongDescription());
        shareButton = (ImageButton) findViewById(R.id.shareButton);
        deleteButton = (ImageButton) findViewById(R.id.deleteButton);
        buttonOpenMaps = (Button) findViewById(R.id.buttonOpenMaps);
        //mapView = (MapView) findViewById(R.id.mapView);

        distanceInfo = (TextView) findViewById(R.id.distanceInfo);
        textViewLongDesc.setVisibility(View.VISIBLE);


        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        try{
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } catch ( Exception e){
            e.printStackTrace();
        }

            }
        });

       // Toast.makeText(this.getApplicationContext(), "Location: " + attraction.getLongitude() + " " + attraction.getLatitude(), Toast.LENGTH_SHORT).show();
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
        textViewLongDesc.setVisibility(View.INVISIBLE);
        buttonOpenMaps.setVisibility(View.INVISIBLE);



        if(attraction.getDistacne() <= 0.500){
            distanceInfo.setVisibility(View.INVISIBLE);
            if(newPhoto.getDrawable() == null){
                buttonTakePhoto.setVisibility(View.VISIBLE);
                shareButton.setVisibility(View.INVISIBLE);
                deleteButton.setVisibility(View.INVISIBLE);
                newPhoto.setVisibility(View.INVISIBLE);
            }else {
                buttonTakePhoto.setVisibility(View.INVISIBLE);
                shareButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                newPhoto.setVisibility(View.VISIBLE);
            }
        } else{
            if(newPhoto.getDrawable() == null){
                distanceInfo.setVisibility(View.VISIBLE);
                buttonTakePhoto.setVisibility(View.INVISIBLE);
                shareButton.setVisibility(View.INVISIBLE);
                deleteButton.setVisibility(View.INVISIBLE);
                newPhoto.setVisibility(View.INVISIBLE);
            }else {
                distanceInfo.setVisibility(View.INVISIBLE);
                buttonTakePhoto.setVisibility(View.INVISIBLE);
                shareButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                newPhoto.setVisibility(View.VISIBLE);
            }
        }


    }

    public void onClickInfo(View view) {
        textViewLongDesc.setText(attraction.getLongDescription());
        buttonInfo.setColorFilter(Color.parseColor("#7649A7"));
        buttonCamera.setColorFilter(Color.parseColor("#A1A1A1"));
        buttonMap.setColorFilter(Color.parseColor("#A1A1A1"));

        buttonOpenMaps.setVisibility(View.INVISIBLE);
        distanceInfo.setVisibility(View.INVISIBLE);
        buttonTakePhoto.setVisibility(View.INVISIBLE);
        shareButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);
        buttonTakePhoto.setVisibility(View.INVISIBLE);
        textViewLongDesc.setVisibility(View.VISIBLE);
        newPhoto.setVisibility(View.INVISIBLE);
        //mapView.setVisibility(View.INVISIBLE);
    }

    public void onClickMap(View view) {

        buttonInfo.setColorFilter(Color.parseColor("#A1A1A1"));
        buttonCamera.setColorFilter(Color.parseColor("#A1A1A1"));
        buttonMap.setColorFilter(Color.parseColor("#7649A7"));

        distanceInfo.setVisibility(View.INVISIBLE);
        buttonTakePhoto.setVisibility(View.INVISIBLE);
        shareButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);
        buttonTakePhoto.setVisibility(View.INVISIBLE);
        textViewLongDesc.setVisibility(View.INVISIBLE);
        newPhoto.setVisibility(View.INVISIBLE);
        buttonOpenMaps.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            buttonTakePhoto.setVisibility(View.INVISIBLE);
            shareButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            newPhoto.setVisibility(View.VISIBLE);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            newPhoto.setImageBitmap(photo);
        }
    }

    public void onCliockDelete(View view) {
        newPhoto.setImageDrawable(null);
        buttonTakePhoto.setVisibility(View.VISIBLE);
        shareButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);
        newPhoto.setVisibility(View.INVISIBLE);
    }

    public void onClickMapss(View view) {
        try{
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + attraction.getLatitude() + "," + attraction.getLongitude()));
            startActivity(intent);
        } catch (Exception e){
            Log.d("errr maps", e.toString());
        }

    }

    public void onClickShare(View view) {
        Intent intent=new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");

    }
}
