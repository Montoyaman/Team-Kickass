package com.example.ryan.uchallenge;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainGUI extends FragmentActivity {

    private GoogleMap mMap;

    LatLng curPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Setup the theme and map*/
        setTheme(R.style.SplashTheme);
        setContentView(R.layout.activity_main_gui);
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
//                //TextView Title = (TextView) v.findViewById(R.id.Title);
//
//                // Getting reference to the TextView to set longitude
//                //TextView Description = (TextView) v.findViewById(R.id.Description_);
//
//                // Setting the latitude
//                //tvLat.setText("Latitude:" + latLng.latitude);
//
//                // Setting the longitude
//                //tvLng.setText("Longitude:"+ latLng.longitude);
//
//                // Returning the view containing InfoWindow contents
//                return v;
//
//            }
//        });

        //Check when the info window is clicked
//        mMap.setOnInfoWindowClickListener(
//                new GoogleMap.OnInfoWindowClickListener() {
//                    @Override
//                    public void onInfoWindowClick(Marker marker) {
//                        marker.setTitle("Blah");
//                        marker.showInfoWindow();
//                    }
//                }
//        );

        //Add a dialog box
        final Dialog dialog = new Dialog(MainGUI.this);
        dialog.setContentView(R.layout.marker_add_dialog);



        //final AddMarkerDialogFragment dialogAdd = new AddMarkerDialogFragment();

        //Setup the map click listener
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                /*Pass marker parameters into dialog*/
                //Bundle markBundle = new Bundle();
                //markBundle.putDouble("Longitude",point.longitude);
                //markBundle.putDouble("Latitude",point.latitude);

                curPoint = point;
                dialog.setTitle("Add a Challenge");
                dialog.show();

                Button OKButton = (Button) dialog.findViewById(R.id.OK);
                OKButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MarkerOptions options = new MarkerOptions();

                        EditText title = (EditText)dialog.findViewById(R.id.addTitle);
                        EditText description = (EditText) dialog.findViewById(R.id.addDescription);

                        options.title(title.getText().toString());
                        options.snippet(description.getText().toString());

                        // Setting position on the MarkerOptions
                        options.position(curPoint);

                        // Animating to the currently touched position
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(curPoint));

                        // Adding marker on the GoogleMap
                        Marker marker = mMap.addMarker(options);

                        // Showing InfoWindow on the GoogleMap
                        marker.showInfoWindow();

                        dialog.dismiss();

                        curPoint = null;
                    }
                });
                //dialogAdd.setArguments(markBundle);
                //Display the dialog!
                //dialogAdd.set
                //dialogAdd.show(getFragmentManager(), "Marker");
            }
        });
    }

    //User has determined to create a marker, so make it!
    public void onUserSelectValue(String selectedValue, LatLng point)
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