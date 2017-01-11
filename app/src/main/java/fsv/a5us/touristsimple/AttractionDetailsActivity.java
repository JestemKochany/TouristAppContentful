package fsv.a5us.touristsimple;

import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class AttractionDetailsActivity extends AppCompatActivity {

    Attraction attraction;
    TextView textViewLongDesc;
    ImageView imageView;
    ImageButton buttonInfo;
    ImageButton buttonCamera;
    ImageButton buttonMap;

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
        Log.d("Recieved attraction: ", attraction.getPhotoURL());

        imageView = (ImageView) findViewById(R.id.imageViewAttratcionDetails);
        Picasso.with(this).load(attraction.getPhotoURL()).into(imageView);
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

    public void onClickInfo(View view) {
        textViewLongDesc.setText(attraction.getLongDescription());
        buttonInfo.setColorFilter(Color.parseColor("#7649A7"));
        buttonCamera.setColorFilter(Color.GRAY);
        buttonMap.setColorFilter(Color.GRAY);

        //textViewLongDesc.setVisibility(View.VISIBLE);
    }

    public void onClickImage(View view) {

    }

    public void onClickCamera(View view) {
        buttonInfo.setColorFilter(Color.GRAY);
        buttonCamera.setColorFilter(Color.parseColor("#7649A7"));
        buttonMap.setColorFilter(Color.GRAY);
    }

    public void onClickMap(View view) {

        buttonInfo.setColorFilter(Color.GRAY);
        buttonCamera.setColorFilter(Color.GRAY);
        buttonMap.setColorFilter(Color.parseColor("#7649A7"));
    }
}
