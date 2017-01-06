package fsv.a5us.touristsimple;

/**
 * Created by A5US on 06.01.2017.
 */

public class Attraction{

    private String attractionType;
    private String name;
    private String shortDescription;
    private String longDescription;
    private String photoURL;

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
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

    public Attraction(String attractionType, String name, String shortDescription, String longDescription, String photoURL) {
        this.attractionType = attractionType;
        this.name = name;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.photoURL = photoURL;
    }




}
