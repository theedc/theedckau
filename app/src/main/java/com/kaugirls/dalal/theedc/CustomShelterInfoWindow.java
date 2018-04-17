package com.kaugirls.dalal.theedc;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomShelterInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomShelterInfoWindow(Context ctx) {
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.marker_info, null);

        TextView shelter_name = view.findViewById(R.id.shelter_name);

        final InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
        Button moreinfo = view.findViewById(R.id.Share);
        shelter_name.setText(marker.getTitle());


        if (infoWindowData.shelterorAssemblty.equals("s")) {
            shelter_name.setText(infoWindowData.shelter_name);

            moreinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {


            moreinfo.setVisibility(TextView.GONE);

        }


        return view;
    }


}