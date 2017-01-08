package fsv.a5us.touristsimple;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class AttractionDetailsActivity extends AppCompatActivity {

    Attraction attraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_details);

        attraction = (Attraction) getIntent().getSerializableExtra("attraction");
        Log.d("Recieved attraction: ", attraction.getPhotoURL());

        ImageView imageView = (ImageView) findViewById(R.id.imageViewAttratcionDetails);
        Picasso.with(this).load(attraction.getPhotoURL()).into(imageView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent MainActivity = new Intent(this,MainActivity.class);
        startActivity(MainActivity);
        finish();
    }
}
