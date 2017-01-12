package fsv.a5us.touristsimple;

import java.io.Serializable;

/**
 * Created by A5US on 06.01.2017.
 */

public class Attraction implements Serializable{

    private String attractionType;
    private String name;
    private String shortDescription;
    private String longDescription;
    private String photoUrl;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoURL(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttractionType() {
        return attractionType;
    }

    public void setAttractionType(String attractionType) {
        this.attractionType = attractionType;
    }

    public Attraction(String attractionType, String name, String shortDescription, String longDescription, String photoUrl) {
        this.attractionType = attractionType;
        this.name = name;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.photoUrl = photoUrl;
    }




}
