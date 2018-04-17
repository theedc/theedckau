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

public class Update_Delete_ShelterActivity extends AppCompatActivity {
    static final LatLng KAU = new LatLng(21.5013069, 39.2422412); //Your LatLong
    Switch IsEnable;
    String latLng = "";
    Button Update;
    Button Delete;
    EditText Name, type, status, capacity, description;
    String ID, shelter_name, shelter_location, shelter_type, shelter_status, shelter_description, shelter_capacity, shelter_isEnable;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__delete_shelter);


        Intent intent = getIntent();
        ID = intent.getStringExtra("id");
        shelter_name = intent.getStringExtra("name");
        shelter_location = intent.getStringExtra("Location");
        shelter_type = intent.getStringExtra("type");
        shelter_status = intent.getStringExtra("status");
        shelter_description = intent.getStringExtra("description");
        shelter_capacity = intent.getStringExtra("capacity");
        shelter_isEnable = intent.getStringExtra("Enable");


        latLng = shelter_location;
        Name = findViewById(R.id.shelter_Name);
        status = findViewById(R.id.status);
        type = findViewById(R.id.type);
        capacity = findViewById(R.id.capacity);
        description = findViewById(R.id.description);
        IsEnable = findViewById(R.id.isEnable);

        Name.setText(shelter_name);
        status.setText(shelter_status);
        type.setText(shelter_type);
        capacity.setText(shelter_capacity);
        description.setText(shelter_description);

        if (shelter_isEnable.equals("1")) {
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
                    String[] gpsVal = shelter_location.split(",");
                    double lat = Double.parseDouble(gpsVal[0]);
                    double lon = Double.parseDouble(gpsVal[1]);
                    Marker hamburg = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
                }

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mMap.clear();
                        Marker hamburg = mMap.addMarker(new MarkerOptions().position(latLng));
                        Update_Delete_ShelterActivity.this.latLng = latLng.latitude + "," + latLng.longitude;
                    }
                });

                // Rest of the stuff you need to do with the map
            }
        });

        Update = findViewById(R.id.Update);


        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Name.getText().toString().equals("")) {
                    Toast.makeText(Update_Delete_ShelterActivity.this, "Please Write Name .", Toast.LENGTH_LONG).show();
                } else if (type.getText().toString().equals("")) {
                    Toast.makeText(Update_Delete_ShelterActivity.this, "Please Write Type .", Toast.LENGTH_LONG).show();
                } else if (status.getText().toString().equals("")) {
                    Toast.makeText(Update_Delete_ShelterActivity.this, "Please Write Status .", Toast.LENGTH_LONG).show();
                } else if (capacity.getText().toString().equals("")) {
                    Toast.makeText(Update_Delete_ShelterActivity.this, "Please Write Capacity .", Toast.LENGTH_LONG).show();
                } else if (description.getText().toString().equals("")) {
                    Toast.makeText(Update_Delete_ShelterActivity.this, "Please Write Description .", Toast.LENGTH_LONG).show();
                } else if (latLng.equals("")) {

                    Toast.makeText(Update_Delete_ShelterActivity.this, "Please Select Location .", Toast.LENGTH_LONG).show();


                } else {

                    String IsEnableVal = "0";
                    if (IsEnable.isChecked()) {
                        IsEnableVal = "1";
                    }

                    UpdateData(ID, Name.getText().toString(), latLng, type.getText().toString(), status.getText().toString(), description.getText().toString(), capacity.getText().toString(), IsEnableVal);

                }


            }
        });


        Delete = findViewById(R.id.Delete);

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(Update_Delete_ShelterActivity.this, android.R.style.Theme_Material_Dialog_Alert);

                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete Shelter ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteData(ID);

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


        new BackgroundTaskWS(Update_Delete_ShelterActivity.this) {
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
                    String shelter_ID = parms[1];


                    data = URLEncoder.encode("shelter_ID", "UTF-8") + "=" + URLEncoder.encode(shelter_ID, "UTF-8");

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
        }.execute("Delete_Shelter", ID);


    }

    public void UpdateData(String ID, String shelter_name, String shelter_location, String shelter_type, String shelter_status, String shelter_description, String shelter_capacity, String shelter_isEnable) {


        new BackgroundTaskWS(Update_Delete_ShelterActivity.this) {
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
                    String shelter_id = parms[1];
                    String shelter_name = parms[2];
                    String shelter_location = parms[3];
                    String shelter_type = parms[4];
                    String shelter_status = parms[5];
                    String shelter_description = parms[6];
                    String shelter_capacity = parms[7];
                    String shelter_isEnable = parms[8];


                    data = URLEncoder.encode("shelter_id", "UTF-8") + "=" + URLEncoder.encode(shelter_id, "UTF-8")
                            + "&" +
                            URLEncoder.encode("shelter_name", "UTF-8") + "=" + URLEncoder.encode(shelter_name, "UTF-8")
                            + "&" +
                            URLEncoder.encode("shelter_location", "UTF-8") + "=" + URLEncoder.encode(shelter_location, "UTF-8")
                            + "&" +
                            URLEncoder.encode("shelter_type", "UTF-8") + "=" + URLEncoder.encode(shelter_type, "UTF-8")
                            + "&" +
                            URLEncoder.encode("shelter_status", "UTF-8") + "=" + URLEncoder.encode(shelter_status, "UTF-8")
                            + "&" +
                            URLEncoder.encode("shelter_description", "UTF-8") + "=" + URLEncoder.encode(shelter_description, "UTF-8")
                            + "&" +
                            URLEncoder.encode("shelter_capacity", "UTF-8") + "=" + URLEncoder.encode(shelter_capacity, "UTF-8")
                            + "&" +
                            URLEncoder.encode("shelter_isEnable", "UTF-8") + "=" + URLEncoder.encode(shelter_isEnable, "UTF-8");


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

                    ex.printStackTrace();
                }


            }
        }.execute("Update_Shelter", ID, shelter_name, shelter_location, shelter_type, shelter_status, shelter_description, shelter_capacity, shelter_isEnable);

    }
}
