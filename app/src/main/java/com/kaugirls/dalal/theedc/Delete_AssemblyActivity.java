package com.kaugirls.dalal.theedc;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Delete_AssemblyActivity extends AppCompatActivity {
    static final LatLng KAU = new LatLng(21.5013069, 39.2422412); //Your LatLong
    EditText Name;
    Switch IsEnable;
    String latLng = "";
    Button Update;
    Button Delete;
    String id;
    String name;
    String Location;
    String Enable;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_assembly);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        Location = intent.getStringExtra("Location");
        Enable = intent.getStringExtra("Enable");
        latLng = Location;
        Name = findViewById(R.id.Assembly_Name);
        IsEnable = findViewById(R.id.isEnable);
        Name.setText(name);

        if (Enable.equals("1")) {
            IsEnable.setChecked(true);
        } else {
            IsEnable.setChecked(false);
        }


        ((SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {

                mMap = googleMap;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(KAU, 15));  //move camera to location
                if (mMap != null) {
                    String[] gpsVal = Location.split(",");
                    double lat = Double.parseDouble(gpsVal[0]);
                    double lon = Double.parseDouble(gpsVal[1]);
                    Marker hamburg = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
                }

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mMap.clear();
                        Marker hamburg = mMap.addMarker(new MarkerOptions().position(latLng));
                        Delete_AssemblyActivity.this.latLng = latLng.latitude + "," + latLng.longitude;
                    }
                });

                // Rest of the stuff you need to do with the map
            }
        });

        Update = findViewById(R.id.update);


        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Name.getText().toString().equals("")) {
                    Toast.makeText(Delete_AssemblyActivity.this, "Please Write Name .", Toast.LENGTH_LONG).show();
                } else if (latLng.equals("")) {

                    Toast.makeText(Delete_AssemblyActivity.this, "Please Select Location .", Toast.LENGTH_LONG).show();


                } else {

                    String IsEnableVal = "0";
                    if (IsEnable.isChecked()) {
                        IsEnableVal = "1";
                    }

                    UpdateData(id, Name.getText().toString(), latLng, IsEnableVal);

                }


            }
        });


        Delete = findViewById(R.id.Delete);

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(Delete_AssemblyActivity.this, android.R.style.Theme_Material_Dialog_Alert);

                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete assembly ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteData(id);

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });


    }


    public void DeleteData(String ID) {


        new BackgroundTaskWS(Delete_AssemblyActivity.this) {
            @Override
            protected String doInBackground(String... parms) {

                String result = "";
                String type = parms[0];
                String urlLogin = "https://asmaa73.000webhostapp.com/connection/" + type + ".php";

                try {
                    URL url = new URL(urlLogin);

                    HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
                    httpUrlConn.setRequestMethod("POST");
                    httpUrlConn.setDoOutput(true);
                    httpUrlConn.setDoInput(true);
                    OutputStream OS = httpUrlConn.getOutputStream();

                    String data = "";
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String assembly_ID = parms[1];


                    data = URLEncoder.encode("assembly_ID", "UTF-8") + "=" + URLEncoder.encode(assembly_ID, "UTF-8");

                    bw.write(data);
                    bw.flush();
                    bw.close();
                    OS.close();

                    InputStream IS = httpUrlConn.getInputStream();

                    BufferedReader br = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));

                    String line;
                    while ((line = br.readLine()) != null) {

                        result += line;
                        System.out.println(result);

                    }
                    br.close();
                    IS.close();
                    httpUrlConn.disconnect();


                    return result;


                } catch (Exception e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                dialog.dismiss();
                Log.d("Return is ", result);


                try {
                    JSONObject reader = new JSONObject(result);


                    if (reader.getString("Code").equals("1")) {
                        Toast.makeText(ctx, reader.getString("Message"), Toast.LENGTH_LONG).show();
                        Intent resultData = new Intent();
                        resultData.putExtra("valueName", "1");
                        setResult(Activity.RESULT_OK, resultData);
                        finish();
                    } else {

                        Toast.makeText(ctx, reader.getString("Message"), Toast.LENGTH_LONG).show();


                    }


                } catch (Exception ex) {
                    ex.printStackTrace();

                }


            }
        }.execute("Delete_Assembly", ID);


    }


    public void UpdateData(String ID, String Name, String Location, String Enable) {


        new BackgroundTaskWS(Delete_AssemblyActivity.this) {
            @Override
            protected String doInBackground(String... parms) {

                String result = "";
                String type = parms[0];
                String urlLogin = "https://asmaa73.000webhostapp.com/connection/" + type + ".php";

                try {
                    URL url = new URL(urlLogin);

                    HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
                    httpUrlConn.setRequestMethod("POST");
                    httpUrlConn.setDoOutput(true);
                    httpUrlConn.setDoInput(true);
                    OutputStream OS = httpUrlConn.getOutputStream();

                    String data = "";
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String assembly_ID = parms[1];
                    String assembly_name = parms[2];
                    String assembly_location = parms[3];
                    String assembly_isEnable = parms[4];


                    data = URLEncoder.encode("assembly_ID", "UTF-8") + "=" + URLEncoder.encode(assembly_ID, "UTF-8")
                            + "&" +
                            URLEncoder.encode("assembly_name", "UTF-8") + "=" + URLEncoder.encode(assembly_name, "UTF-8")
                            + "&" +
                            URLEncoder.encode("assembly_location", "UTF-8") + "=" + URLEncoder.encode(assembly_location, "UTF-8")
                            + "&" +
                            URLEncoder.encode("assembly_isEnable", "UTF-8") + "=" + URLEncoder.encode(assembly_isEnable, "UTF-8");
//
                    bw.write(data);
                    bw.flush();
                    bw.close();
                    OS.close();

                    InputStream IS = httpUrlConn.getInputStream();

                    BufferedReader br = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));

                    String line;
                    while ((line = br.readLine()) != null) {

                        result += line;
                        System.out.println(result);

                    }
                    br.close();
                    IS.close();
                    httpUrlConn.disconnect();


                    return result;


                } catch (Exception e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                dialog.dismiss();
                Log.d("Return is ", result);


                try {
                    JSONObject reader = new JSONObject(result);


                    if (reader.getString("Code").equals("1")) {
                        Toast.makeText(ctx, reader.getString("Message"), Toast.LENGTH_LONG).show();
                        //AssemblyPointsActivity.this.finish();

                        Intent resultData = new Intent();
                        resultData.putExtra("valueName", "1");
                        setResult(Activity.RESULT_OK, resultData);
                        finish();

                    } else {

                        Toast.makeText(ctx, reader.getString("Message"), Toast.LENGTH_LONG).show();


                    }


                } catch (Exception ex) {


                }


            }
        }.execute("Update_Assembly", ID, Name, Location, Enable);


    }

}
