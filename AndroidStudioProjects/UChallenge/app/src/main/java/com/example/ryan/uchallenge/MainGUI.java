package com.example.ryan.uchallenge;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainGUI extends FragmentActivity {

    private GoogleMap mMap;
    private HashMap<Marker, Marker> MarkerMap;
    FrameLayout addMarker;
    FrameLayout editMarker;

    ArrayList<Marker> markerList = new ArrayList<>();

    Marker curMark;
    LatLng curPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Setup the theme and map*/
        setTheme(R.style.SplashTheme);
        setContentView(R.layout.activity_main_gui);

        MarkerMap = new HashMap<Marker, Marker>();

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

        editMarker = (FrameLayout) findViewById(R.id.EditMarkerFrame);
        addMarker = (FrameLayout) findViewById(R.id.MarkerFrame);
        setUpMapIfNeeded();

        // Setting a custom info window adapter for the map
//        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//
//            // Use default InfoWindow frame
//            @Override
//            public View getInfoWindow(Marker arg0) {
//                return null;
//            }
//
//            // Defines the contents of the InfoWindow
//            @Override
//            public View getInfoContents(Marker arg0) {
//
//                // Getting view from the layout file info_window_layout
//                View v = getLayoutInflater().inflate(R.layout.windowlayout, null);
//
//                // Getting the position from the marker
//                LatLng latLng = arg0.getPosition();
//
//                // Getting reference to the TextView to set latitude
//                TextView Title = (TextView) v.findViewById(R.id.Title);
//
//                // Getting reference to the TextView to set longitude
//                TextView Description = (TextView) v.findViewById(R.id.Description);
//
//                // Setting the latitude
//                tvLat.setText("Latitude:" + latLng.latitude);
//
//                // Setting the longitude
//                tvLng.setText("Longitude:"+ latLng.longitude);
//
//                // Returning the view containing InfoWindow contents
//                return v;
//
//            }
//        });

        //Check when the info window is clicked
        mMap.setOnInfoWindowClickListener(
                new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        if (addMarker.getVisibility() == View.VISIBLE) {
                            //Reset and collapse the frame
                            //Clear text inputs
                            EditText title = (EditText) addMarker.findViewById(R.id.addTitle);
                            EditText description = (EditText) addMarker.findViewById(R.id.addDescription);
                            title.setText("");
                            description.setText("");
                            addMarker.setVisibility(View.INVISIBLE);

                            curPoint = null;
                        }

                        curMark = marker;

                        String tHandle = curMark.getTitle();
                        String dHandle = curMark.getSnippet();

                        EditText title = (EditText) editMarker.findViewById(R.id.editTitle);
                        EditText description = (EditText) editMarker.findViewById(R.id.editDescription);
                        title.setText(tHandle);
                        description.setText(dHandle);

                        if (editMarker.getVisibility() == View.VISIBLE) {
                            //Reset and collapse the frame
                            //Clear text inputs
                            title.setText("");
                            description.setText("");
                            editMarker.setVisibility(View.INVISIBLE);

                            curMark = null;
                        } else {
                            editMarker.setVisibility(View.VISIBLE);

                            Button EditButton = (Button) editMarker.findViewById(R.id.Edit);
                            EditButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EditText title = (EditText) editMarker.findViewById(R.id.editTitle);
                                    EditText description = (EditText) editMarker.findViewById(R.id.editDescription);

                                    curMark.setTitle(title.getText().toString());
                                    curMark.setSnippet(description.getText().toString());

                                    // Showing InfoWindow on the GoogleMap
                                    curMark.showInfoWindow();

                                    //Clear text inputs
                                    title.setText("");
                                    description.setText("");
                                    editMarker.setVisibility(View.INVISIBLE);

                                    curMark = null;
                                }
                            });

                            Button RemoveButton = (Button) editMarker.findViewById(R.id.Remove);
                            RemoveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Remove the marker from the map
                                    curMark.remove();

                                    EditText title = (EditText) editMarker.findViewById(R.id.editTitle);
                                    EditText description = (EditText) editMarker.findViewById(R.id.editDescription);
                                    //Clear text inputs
                                    title.setText("");
                                    description.setText("");
                                    editMarker.setVisibility(View.INVISIBLE);

                                    curMark = null;
                                }
                            });

                            Button CancelButton = (Button) editMarker.findViewById(R.id.Cancel);
                            CancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EditText title = (EditText) editMarker.findViewById(R.id.editTitle);
                                    EditText description = (EditText) editMarker.findViewById(R.id.editDescription);
                                    //Clear text inputs
                                    title.setText("");
                                    description.setText("");
                                    editMarker.setVisibility(View.INVISIBLE);

                                    curMark = null;
                                }
                            });
                        }
                    }
                }
        );

        //Setup the map click listener
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                //Clear necessary framelayouts and clear
                if (editMarker.getVisibility() == View.VISIBLE) {
                    EditText title = (EditText) editMarker.findViewById(R.id.editTitle);
                    EditText description = (EditText) editMarker.findViewById(R.id.editDescription);
                    //Reset and collapse the frame
                    //Clear text inputs
                    title.setText("");
                    description.setText("");
                    editMarker.setVisibility(View.INVISIBLE);

                    curMark = null;
                } else if (addMarker.getVisibility() == View.VISIBLE) {
                    //Reset and collapse the frame
                    //Clear text inputs
                    EditText title = (EditText) addMarker.findViewById(R.id.addTitle);
                    EditText description = (EditText) addMarker.findViewById(R.id.addDescription);
                    title.setText("");
                    description.setText("");
                    addMarker.setVisibility(View.INVISIBLE);

                    curPoint = null;
                } else {
                    addMarker.setVisibility(View.VISIBLE);

                    curPoint = point;

                    // Animating to the currently touched position
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(curPoint));

                    Button OKButton = (Button) addMarker.findViewById(R.id.OK);
                    OKButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MarkerOptions options = new MarkerOptions();

                            EditText title = (EditText) addMarker.findViewById(R.id.addTitle);
                            EditText description = (EditText) addMarker.findViewById(R.id.addDescription);

                            options.title(title.getText().toString());
                            options.snippet(description.getText().toString());

                            // Setting position on the MarkerOptions
                            options.position(curPoint);

                            // Adding marker on the GoogleMap
                            Marker marker = mMap.addMarker(options);

                            //Adding to list and hashmap
                            markerList.add(marker);
                            hashMarkers(markerList);

                            // Showing InfoWindow on the GoogleMap
                            marker.showInfoWindow();

                            //Clear text inputs
                            title.setText("");
                            description.setText("");
                            addMarker.setVisibility(View.INVISIBLE);

                            curPoint = null;

                            saveMap();
                        }
                    });
                }
            }
        });
    }
    //http://www.rogcg.com/blog/2014/04/20/android-working-with-google-maps-v2-and-custom-markers
    private void hashMarkers(ArrayList<Marker> markers)
    {
        if(markers.size() > 0)
        {
            for (Marker myMarker : markers)
            {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(myMarker.getPosition());
//                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));
                //markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation_icon));

                Marker currentMarker = mMap.addMarker(markerOption);
                MarkerMap.put(currentMarker, myMarker);

                //mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }

    public void saveMap() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("Test.txt");
            ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(MarkerMap);
            objectOutputStream.close();

//            FileOutputStream fos = openFileOutput("Test", Context.MODE_PRIVATE);
//            ObjectOutputStream os = new ObjectOutputStream(fos);
//            os.writeObject(MarkerMap);
//            os.close();
//            fos.close();
        } catch (IOException e) {
            Log.e("MyApp", "IO Exception: " + e);

        }
    }

    public HashMap loadMap()
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

    //User has determined to create a marker, so make it!
    public void onUserSelectValue(LatLng point, String title, String description)
    {
        MarkerOptions options = new MarkerOptions();
        // Setting position on the MarkerOptions
        options.position(point);

        // Animating to the currently touched position
        mMap.animateCamera(CameraUpdateFactory.newLatLng(point));

        // Adding marker on the GoogleMap
        Marker marker = mMap.addMarker(options);

        // Showing InfoWindow on the GoogleMap
        marker.showInfoWindow();
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