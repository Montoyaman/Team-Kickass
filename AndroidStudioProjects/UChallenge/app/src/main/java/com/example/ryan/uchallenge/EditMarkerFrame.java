package com.example.ryan.uchallenge;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by ryan on 2/15/15.
 */
//This class is responsible for the AddMarkerDialog
public class EditMarkerFrame {

    private FrameLayout Frame;
    private EditText Title;
    private EditText Description;
    private Button EditChallenge;
    private Button Cancel;
    private Button Remove;
    private Context context;
    private LatLng point;

    private Marker marker;

    //Constructor for the add_marker_frame, must pass calling context as well as the frame container
    public EditMarkerFrame(FrameLayout container, Context callContext)
    {
        this.Frame = container;
        this.Title = (EditText)Frame.findViewById(R.id.editTitle);
        this.Description = (EditText)Frame.findViewById(R.id.editDescription);
        this.context = callContext;

        //Create the button listener for the edit field
        this.EditChallenge = (Button)Frame.findViewById(R.id.Edit);
        EditChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We want to trigger a function in the calling routine that will handle the creation of a new marker
                ((MainGUI)context).onEditMarker("Edit");
                point = null;
            }
        });

        //Create the button listener for the remove field
        this.Remove = (Button)Frame.findViewById(R.id.Remove);
        Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We want to trigger a function in the calling routine that will handle the creation of a new marker
                ((MainGUI)context).onEditMarker("Remove");
                point = null;
            }
        });

        //Create the button listener for the cancel field
        this.Cancel = (Button)Frame.findViewById(R.id.Cancel);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We want to trigger a function in the calling routine that will handle the creation of a new marker
                ((MainGUI)context).onEditMarker("Cancel");
                point = null;
            }
        });
    }

    public void openFrameWindow(Marker mMarker)
    {
        this.marker = mMarker;
        this.point = mMarker.getPosition();
        setFrameTitle(mMarker.getTitle().toString());
        setFrameDescription(mMarker.getSnippet().toString());
        Frame.setVisibility(View.VISIBLE);
    }

    public void clearFrameWindow()
    {
        Title.setText("");
        Description.setText("");
        Frame.setVisibility(View.INVISIBLE);
        marker = null;
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

    public void setFrameMarker(Marker mMarker){ this.marker = mMarker; };

    public void getMarkerKey() { };

    public Marker getFrameMarker(){ return marker; };
}
