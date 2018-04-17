package com.kaugirls.dalal.theedc.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


// Fetches data from url passed
public class GetShortestDir extends AsyncTask<String, Void, String> {


    ProgressDialog dialogp;
    Context con;
    String[] shelter;
    String CurrentLocation;
    private GoogleMap mMap;

    public GetShortestDir(Context con, String CurrentLocation, GoogleMap mMap) {
        this.mMap = mMap;
        this.con = con;
        dialogp = ProgressDialog.show(con, "",
                "Loading. Please wait...", true);
        this.CurrentLocation = CurrentLocation;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "mode=walking&sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service

        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyCG4Dg9QkxR-qaiGoIttydrZHErVj7QuRY";
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Downloading data in non-ui thread
    @Override
    protected String doInBackground(String... url) {
        // For storing data from web service
        String data = "";
        try {
            // Fetching the data from web service
            String Dis = "";
            shelter = url;
            //http://maps.googleapis.com/maps/api/distancematrix/json?origins=21.796954,39.1402045&destinations=21.5027301,39.2666877|21.7922051,39.1491649&mode=walking&sensor=false
            for (int i = 0; i < url.length; i++) {
                Dis += url[i] + "|";
            }

            data = downloadUrl("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + CurrentLocation + "&destinations=" + Dis + "&mode=walking&sensor=false&key=AIzaSyCG4Dg9QkxR-qaiGoIttydrZHErVj7QuRY");
            Log.d("URl", "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + CurrentLocation + "&destinations=" + Dis + "&mode=walking&sensor=false&key=AIzaSyCG4Dg9QkxR-qaiGoIttydrZHErVj7QuRY");

        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    // Executes in UI thread, after the execution of
    // doInBackground()
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            Log.d("Error Get", "1");

            JSONObject reader = new JSONObject(result);

            Log.d("Error Get", "2");

            JSONArray rows = reader.getJSONArray("rows");
            Log.d("Error Get", "3");

            JSONObject location = (JSONObject) rows.get(0);
            Log.d("Error Get", "4");

            JSONArray elements = location.getJSONArray("elements");
            int Max = 100000000;

            int index = 0;
            Log.d("Error Get", "5");
            Log.d("Result", result);

            for (int i = 0; i < elements.length(); i++) {
                Log.d("Error Get", "6" + "(" + elements.length() + ")");

                JSONObject distance = ((JSONObject) elements.get(i)).getJSONObject("distance");
                Log.d("Error Get", "7" + "(" + distance.getInt("value") + ")==>");
                Log.d("Error Get", "" + "(" + distance.getInt("value") + ")==>" + distance.getInt("value") + "<" + Max);

                if (distance.getInt("value") < Max) {

                    Max = distance.getInt("value");
                    index = i;
                }
                Log.d("Error Get", "" + "Max =" + Max);


            }

            Log.d("Error Get", "8" + "(" + Max + ")");


            double lat = Double.parseDouble(CurrentLocation.split(",")[0]);
            double lon = Double.parseDouble(CurrentLocation.split(",")[1]);
            LatLng currentLatLng = new LatLng(lat, lon);
            double latD = Double.parseDouble(shelter[index].split(",")[0]);
            double lonD = Double.parseDouble(shelter[index].split(",")[1]);
            LatLng destLatLng = new LatLng(latD, lonD);


            String url = getDirectionsUrl(currentLatLng, destLatLng);
            DownloadTask downloadTask = new DownloadTask(con, mMap, dialogp, CurrentLocation, shelter[index]);
            // Start downloading json data from Google Directions API
            Log.d("Error Get", "9" + "(" + url + ")");

            downloadTask.execute(url);


        } catch (Exception e) {

            Log.d("Error Get", result);
            dialogp.dismiss();

        }


    }
}