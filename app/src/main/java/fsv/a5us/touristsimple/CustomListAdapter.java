package fsv.a5us.touristsimple;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A5US on 06.01.2017.
 */

public class CustomListAdapter extends ArrayAdapter<Attraction> {

    List<Attraction> attractions;
    Context context;
    int resource;

    public CustomListAdapter(Context context, int resource, List<Attraction> attractions) {
        super(context, resource, attractions);
        this.attractions = attractions;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_list_layout, null, true);
        }
        Attraction attraction = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewAttraction);
        Picasso.with(context).load(attraction.getPhotoURL()).into(imageView);

        TextView textName = (TextView) convertView.findViewById(R.id.txtName);
        textName.setText(attraction.getName());

        TextView textDesc = (TextView) convertView.findViewById(R.id.txtDesc);
        textDesc.setText(attraction.getShortDescription());

        return convertView;
    }
}
