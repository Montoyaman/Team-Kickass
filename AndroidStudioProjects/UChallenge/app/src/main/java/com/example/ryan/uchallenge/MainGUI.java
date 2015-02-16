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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainGUI extends FragmentActivity {

    /*Google map*/
    private GoogleMap mMap;

    /*Hashmap for storing markers*/
    private HashMap<Marker, ActivityMarker> MarkerMap;

    /*Add frame classes*/
    private AddMarkerFrame addMarkerFrame;
    private EditMarkerFrame editMarkerFrame;

    /*Array for hashing?*/
//    private ArrayList<ActivityMarker> markerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Setup the theme and map*/
        setTheme(R.style.SplashTheme);
        setContentView(R.layout.activity_main_gui);

        MarkerMap = new HashMap<Marker, ActivityMarker>();

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
        //Create the marker and place on the map
        MarkerOptions options = new MarkerOptions();
        options.title(addMarkerFrame.getFrameTitle());
        options.snippet(addMarkerFrame.getFrameDescription());
        // Setting position on the MarkerOptions
        options.position(addMarkerFrame.getFramePoint());
        // Adding marker on the GoogleMap
        Marker marker = mMap.addMarker(options);
        marker.showInfoWindow();
        //Clear text inputs
        addMarkerFrame.clearFrameWindow();
    }

    //http://www.rogcg.com/blog/2014/04/20/android-working-with-google-maps-v2-and-custom-markers
    //Iterate through the Hash and create markers
    private void hashMarkers(ArrayList<ActivityMarker> markers)
    {
        if(markers.size() > 0)
        {
            for (ActivityMarker myMarker : markers)
            {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getLatitude(), myMarker.getLongitude()));
//                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));
                //markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation_icon));

                Marker currentMarker = mMap.addMarker(markerOption);
                MarkerMap.put(currentMarker, myMarker);

                //mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }

    private void saveMap() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("Test.txt");
            ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(MarkerMap);
            objectOutputStream.close();
            fileOutputStream.close();

//            FileOutputStream fos = openFileOutput("Test", Context.MODE_PRIVATE);
//            ObjectOutputStream os = new ObjectOutputStream(fos);
//            os.writeObject(MarkerMap);
//            os.close();
//            fos.close();
        } catch (IOException e) {
            Log.e("MyApp", "IO Exception: " + e);

        }
    }

    private HashMap loadMap()
    {
        try {
            Context context = getApplicationContext();
            FileInputStream fis = context.openFileInput("Test");
            ObjectInputStream is = new ObjectInputStream(fis);
            HashMap simpleClass = (HashMap) is.readObject();
            is.close();
            fis.close();
            return simpleClass;
        }
        catch(ClassNotFoundException e) {
            Log.e("MyApp", "ClassNotFoundException: " + e);
        }
        catch(IOException e)
        {
            Log.e("MyApp", "IO Exception: " + e);
        }

        return null;
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
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
    }
}