package com.example.ryan.uchallenge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ryan on 2/5/15.
 */
public class AddMarkerDialogFragment extends DialogFragment {

    private EditText editTitle;
    private EditText editDescription;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        editTitle = new EditText(getActivity());
        editTitle.setInputType(InputType.TYPE_CLASS_TEXT);

        editDescription = new EditText(getActivity());
        editDescription.setInputType(InputType.TYPE_CLASS_TEXT);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(editTitle);
        builder.setView(editDescription);
        builder.setMessage(R.string.dialog_add_title)
                .setPositiveButton(R.string.enter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddMarker(dialog, id);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Don't do shit
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void AddMarker(DialogInterface dialog, int position) {
        String name = editTitle.getText().toString();
        Log.d("Title:", name);

        Bundle markBundle = getArguments();
        LatLng point = new LatLng(markBundle.getDouble("Latitude"),markBundle.getDouble("Longitude"));

        MainGUI callingActivity = (MainGUI) getActivity();
        callingActivity.onUserSelectValue(name, point);
        dialog.dismiss();
    }
}