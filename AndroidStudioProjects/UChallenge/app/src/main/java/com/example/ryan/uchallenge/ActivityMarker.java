package com.example.ryan.uchallenge;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by ryan on 2/15/15.
 */
//http://www.rogcg.com/blog/2014/04/20/android-working-with-google-maps-v2-and-custom-markers
public class ActivityMarker implements Serializable
{
    private String mTitle;
    private String mDescription;
    private Double mLatitude;
    private Double mLongitude;

    private String key;

    //private static final long serialVersionUID = 0x9AF22BF5;

    public ActivityMarker(String title, String description, Double latitude, Double longitude)
    {
        this.mTitle = title;
        this.mDescription = description;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public String getTitle(){
        return mTitle;
    }

    public void setTitle(String title){
        this.mTitle = title;
    }

    public String getDescription(){
        return mDescription;
    }

    public void setDescription(String description){
        this.mDescription = description;
    }

    public Double getLatitude(){
        return mLatitude;
    }

    public void setLatitude(Double lat){
        this.mLatitude = lat;
    }

    public Double getLongitude(){
        return mLongitude;
    }

    public void setLongitude(Double longitude){
        this.mLongitude = longitude;
    }

    public void setKey(String inKey) { this.key = inKey; }

    public String getKey() { return key; }

    /**
     * Always treat de-serialization as a full-blown constructor, by
     * validating the final state of the de-serialized object.
     */
    private void readObject(
            ObjectInputStream aInputStream
    ) throws ClassNotFoundException, IOException {
        //always perform the default de-serialization first
        aInputStream.defaultReadObject();
    }

    /**
     * This is the default implementation of writeObject.
     * Customise if necessary.
     */
    private void writeObject(
            ObjectOutputStream aOutputStream
    ) throws IOException {
        //perform the default serialization for all non-transient, non-static fields
        aOutputStream.defaultWriteObject();
    }
}
