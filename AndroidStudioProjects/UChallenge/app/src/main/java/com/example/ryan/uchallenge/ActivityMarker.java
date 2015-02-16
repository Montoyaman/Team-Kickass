package com.example.ryan.uchallenge;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
/**
 * Created by ryan on 2/15/15.
 */
//http://www.rogcg.com/blog/2014/04/20/android-working-with-google-maps-v2-and-custom-markers
public class ActivityMarker
{
    private String mTitle;
    private String mDescription;
    private Double mLatitude;
    private Double mLongitude;

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
}
