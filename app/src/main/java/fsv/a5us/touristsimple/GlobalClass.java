package fsv.a5us.touristsimple;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by A5US on 12.01.2017.
 */

public class GlobalClass extends Application {

    private ArrayList<Attraction> attractions;

    public GlobalClass() {
        this.attractions = new ArrayList<Attraction>();
    }

    public ArrayList<Attraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(ArrayList<Attraction> attractions) {
        this.attractions = attractions;
    }

    public int getSize(){
        return attractions.size();
    }
}
