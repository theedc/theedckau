package com.kaugirls.dalal.theedc.tasks;

/**
 * Created by hoangpq on 12/25/2016.
 */

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


class PolylineHolder {
    private static Polyline polyline = null;

    static void setLine(Polyline polyline) {
        PolylineHolder.polyline = polyline;
    }

    static void removeLine() {
        if (PolylineHolder.polyline != null) {
            PolylineHolder.polyline.remove();
        }
    }
}


//
//class CircleHolder {
//    private static Circle circle = null;
//
//    static void setLine(Circle circle) {
//        CircleHolder.circle = circle;
//    }
//
//    static void removeLine() {
//        if (CircleHolder.circle != null) {
//            CircleHolder.circle.remove();
//        }
//    }
//}
public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    String Current;
    String Dist;
    private GoogleMap mMap;

    public ParserTask(GoogleMap mMap, String Current, String Dist) {
        this.mMap = mMap;
        this.Current = Current;
        this.Dist = Dist;
    }

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;
        try {
            jObject = new JSONObject(jsonData[0]);
            DirectionsJSONParser parser = new DirectionsJSONParser();
            // Starts parsing data
            routes = parser.parse(jObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    // Executes in UI thread, after the parsing process
    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        PolylineHolder.removeLine();
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;
        // Traversing through all the routes

        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();

            lineOptions = new PolylineOptions();
            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);
            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);
                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);
                points.add(position);
            }
            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(4);
            lineOptions.color(Color.BLUE);
        }
        // Drawing polyline in the Google Map for the i-th route
        PolylineHolder.setLine(mMap.addPolyline(lineOptions));


    }
}