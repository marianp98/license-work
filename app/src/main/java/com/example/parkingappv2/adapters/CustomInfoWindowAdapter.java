package com.example.parkingappv2.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parkingappv2.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter extends AppCompatActivity implements GoogleMap.InfoWindowAdapter {
    //constants
    private static final int CLAIMED = 1;

    private final View mWindow;
    private final Context mContext;
    private final int mParkingSpotType;

    public CustomInfoWindowAdapter(Context context, int parkingSpotType) {
        mContext = context;
        mParkingSpotType = parkingSpotType;

        /*parkingSpotType differentiates b/w claimed & un-claimed spots, hence we will inflate the info window layout accordingly*/
        if (parkingSpotType == CLAIMED) {
            mWindow = LayoutInflater.from(context).inflate(R.layout.marker_window_layout_claimed, null);
        } else {
            mWindow = LayoutInflater.from(context).inflate(R.layout.marker_window_layout, null);
        }
    }//constructor

    private void renderWindowText(Marker marker, View view) {

        String title = marker.getTitle();
        String snippet = marker.getSnippet();

        TextView tvTitle = view.findViewById(R.id.title);
        TextView tvSnippet = view.findViewById(R.id.snippet);

        if (title != null && !title.isEmpty()) {
            tvTitle.setText(title);
        }

        if (snippet != null && !snippet.isEmpty()) {
            tvSnippet.setText(snippet);
        }

    }//renderWindowText

    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return mWindow;
    }
}