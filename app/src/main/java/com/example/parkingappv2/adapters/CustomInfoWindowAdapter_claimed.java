package com.example.parkingappv2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parkingappv2.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

public class CustomInfoWindowAdapter_claimed extends AppCompatActivity implements GoogleMap.InfoWindowAdapter {

    private static final String TAG = "CustomInfoWindowAdapter_claimed";
    private Context mContext;
    private View infoButtonListener;
    private Context context_claimed;


    public CustomInfoWindowAdapter_claimed(Context context_claimed) {

        this.context_claimed=context_claimed;
    }

    @Override
    public View getInfoWindow(Marker marker_claimed) {
        View infoView=LayoutInflater.from(context_claimed).inflate(R.layout.marker_window_layout_claimed,null);
        TextView title=infoView.findViewById(R.id.title_claimed);
        TextView snippet=infoView.findViewById(R.id.snippet_claimed);
        title.setText(marker_claimed.getTitle());
        snippet.setText(marker_claimed.getSnippet());
        return infoView;
        //rendowWindowText(marker_claimed, mWindow_claimed);
    }

    @Override
    public View getInfoContents(Marker marker_claimed) {
        return null;
    }

}