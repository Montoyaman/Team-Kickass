package com.example.ryan.uchallenge;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ryan on 2/15/15.
 */
//This class is responsible for the AddMarkerDialog
public class AddMarkerFrame {

    private FrameLayout Frame;
    private EditText Title;
    private EditText Description;
    private Button AddChallenge;

    private Context context;

    private LatLng point;

    //Constructor for the add_marker_frame, must pass calling context as well as the frame container
    public AddMarkerFrame(FrameLayout container, Context callContext)
    {
        this.Frame = container;
        this.Title = (EditText)Frame.findViewById(R.id.addTitle);
        this.Description = (EditText)Frame.findViewById(R.id.addDescription);
        this.context = callContext;

        //Create the button listener
        this.AddChallenge = (Button)Frame.findViewById(R.id.OK);
        AddChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We want to trigger a function in the calling routine that will handle the creation of a new marker
                ((MainGUI)context).onAddMarker();
                point = null;
            }
        });
    }

    public void openFrameWindow(LatLng mPoint)
    {
        this.point = mPoint;
        Frame.setVisibility(View.VISIBLE);
    }

    public void clearFrameWindow()
    {
        Title.setText("");
        Description.setText("");
        Frame.setVisibility(View.INVISIBLE);
        point = null;
    }

    public boolean isVisible()
    {
        if (Frame.getVisibility() == View.INVISIBLE)
        {
            return false;
        }

        return true;
    }

    public String getFrameTitle()
    {
        return Title.getText().toString();
    }

    public void setFrameTitle(String inTitle)
    {
        Title.setText(inTitle);
    }

    public String getFrameDescription()
    {
        return Description.getText().toString();
    }

    public void setFrameDescription(String inString)
    {
        Description.setText(inString);
    }

    public LatLng getFramePoint()
    {
        return point;
    }

    public void setFramePoint(LatLng Point)
    {
        this.point = Point;
    }
}
