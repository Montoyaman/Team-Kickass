package com.example.ryan.uchallenge;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainGUI extends FragmentActivity {

    private String FILENAME = "HashMap";

    /*Google map*/
    private GoogleMap mMap;

    /*Hashmap for storing markers*/
    private HashMap<String, ActivityMarker> MarkerHash;

    /*Add frame classes*/
    private AddMarkerFrame addMarkerFrame;
    private EditMarkerFrame editMarkerFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Setup the theme and map*/
        setTheme(R.style.SplashTheme);
        setContentView(R.layout.activity_main_gui);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (savedInstanceState == null) {
            // First incarnation of this activity.
            mapFragment.setRetainInstance(true);
        } else {
            // Reincarnated activity. The obtained map is the same map instance in the previous
            // activity life cycle. There is no need to reinitialize it.
            mMap = mapFragment.getMap();
        }

        setUpMapIfNeeded();

        /*Instantiate the frames using the appropriate views and calling context*/
        addMarkerFrame = new AddMarkerFrame((FrameLayout) findViewById(R.id.add_marker_frame), this);
        editMarkerFrame = new EditMarkerFrame((FrameLayout) findViewById(R.id.edit_marker_frame), this);

        //Check when the info window is clicked
        mMap.setOnInfoWindowClickListener(
            new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    if (addMarkerFrame.isVisible())
                    {
                        addMarkerFrame.clearFrameWindow();
                    }

                    if (editMarkerFrame.isVisible())
                    {
                        editMarkerFrame.clearFrameWindow();
                    }

                    editMarkerFrame.openFrameWindow(marker);
                }
            }
        );

        //Setup the map click listener
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
            mMap.animateCamera(CameraUpdateFactory.newLatLng(point));

            //Clear necessary frame layouts
            if (addMarkerFrame.isVisible())
            {
                addMarkerFrame.clearFrameWindow();
            }

            else if (editMarkerFrame.isVisible())
            {
                editMarkerFrame.clearFrameWindow();
            }

            else
            {
                addMarkerFrame.openFrameWindow(point);
            }
            }
        });
    }

    /*This function is called by the EditMarkerFrame class when a button has been pressed*/
    public void onEditMarker(String OPcode)
    {
        Marker mark = editMarkerFrame.getFrameMarker();

        switch (OPcode) {
            case "Edit":
                mark.setTitle(editMarkerFrame.getFrameTitle());
                mark.setSnippet(editMarkerFrame.getFrameDescription());

                //Remove the hash, and then re-add it

                mark.showInfoWindow();
                break;
            case "Remove":
                mark.remove();
                break;
        }

        editMarkerFrame.clearFrameWindow();
    }

    /*This function is called by the AddMarkerFrame class when the create button has been placed*/
    public void onAddMarker()
    {
        //Add the marker to the array
        ActivityMarker newMark = new ActivityMarker(addMarkerFrame.getFrameTitle(),addMarkerFrame.getFrameDescription(),addMarkerFrame.getFramePoint().latitude,addMarkerFrame.getFramePoint().longitude);

        plotMarker(newMark);

        addMarkerFrame.clearFrameWindow();

        //Add to the hashmap
        hashMarker(newMark);

    }

    private void plotMarker(ActivityMarker marker)
    {
        if(marker != null)
        {
            MarkerOptions markerOption = new MarkerOptions().position(new LatLng(marker.getLatitude(), marker.getLongitude()));
            markerOption.title(marker.getTitle());
            markerOption.snippet(marker.getDescription());
            Marker Mark = mMap.addMarker(markerOption);
            Mark.showInfoWindow();
        }
    }

    private void plotMarkers(ArrayList<ActivityMarker> markers)
    {
        if(markers.size() > 0)
        {
            for (ActivityMarker myMarker : markers)
            {
                plotMarker(myMarker);
            }
        }
    }

    //http://www.rogcg.com/blog/2014/04/20/android-working-with-google-maps-v2-and-custom-markers
    //Add a hash
    private void hashMarker(ActivityMarker marker)
    {
        if(marker != null)
        {
            SecureRandom random = new SecureRandom();
            byte bytes[] = new byte[8];
            random.nextBytes(bytes);

            String keyString = bytes.toString();

            //Check generate unique ID
            while (MarkerHash.get(keyString) != null)
            {
                random.nextBytes(bytes);
                keyString = bytes.toString();
            }

            //Enter the hash
            marker.setKey(keyString);
            MarkerHash.put(keyString, marker);

            //Save the hashmap file
            saveMap(MarkerHash);
        }
    }

    private void saveMap(HashMap hash) {
        try {
            FileOutputStream fos = openFileOutput("HashMap", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(hash);
            os.close();
            fos.close();

        } catch (IOException e) {
            Log.e("MyApp", "IO Exception: " + e);

        }
    }

    private HashMap loadMap()
    {
        //File file = new File(FILENAME);
        //if(file.exists())
        //{
            try {
                Context context = getApplicationContext();
                FileInputStream fis = context.openFileInput(FILENAME);
                ObjectInputStream is = new ObjectInputStream(fis);
                HashMap simpleClass = (HashMap) is.readObject();
                is.close();
                fis.close();
                return simpleClass;
            }
            catch(ClassNotFoundException e) {
                Log.e("MyApp", "ClassNotFoundException: " + e);
            }
            catch(FileNotFoundException e) {
                Log.e("MyApp", "FileNotFoundException: " + e);
            }
            catch(IOException e)
            {
                Log.e("MyApp", "IO Exception: " + e);
            }
        //}

        return new HashMap<String, ActivityMarker>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);

        /*Try to load the hashmap*/
        MarkerHash = loadMap();

        /*Build and plot the hashed markers*/
        if (MarkerHash.size() != 0) {
            ArrayList<ActivityMarker> markerList = new ArrayList<>(MarkerHash.values());
            plotMarkers(markerList);
        }
    }
}