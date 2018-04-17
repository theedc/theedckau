package com.kaugirls.dalal.theedc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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


public class Add_AssemblyActivity extends AppCompatActivity {
    static final LatLng KAU = new LatLng(21.5013069, 39.2422412); //Your LatLong
    EditText Name;
    Switch IsEnable;
    String latLng = "";
    Button Save;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assembly);


        Name = findViewById(R.id.Assembly_Name);
        IsEnable = findViewById(R.id.isEnable);


        ((SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {

                mMap = googleMap;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(KAU, 15));  //move camera to location
                if (mMap != null) {
                }

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mMap.clear();
                        Marker hamburg = mMap.addMarker(new MarkerOptions().position(latLng));
                        Add_AssemblyActivity.this.latLng = latLng.latitude + "," + latLng.longitude;
                    }
                });

                // Rest of the stuff you need to do with the map
            }
        });


        Save = findViewById(R.id.Save);


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String method = "Add_Assembly";


                if (Name.getText().toString().equals("")) {
                    Toast.makeText(Add_AssemblyActivity.this, "Please Write Name .", Toast.LENGTH_LONG).show();
                } else if (latLng.equals("")) {

                    Toast.makeText(Add_AssemblyActivity.this, "Please Select Location .", Toast.LENGTH_LONG).show();


                } else {

                    String IsEnableVal = "0";
                    if (IsEnable.isChecked()) {
                        IsEnableVal = "1";
                    }
                    new BackgroundTaskWS(Add_AssemblyActivity.this) {
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
                                String assembly_name = parms[1];
                                String assembly_location = parms[2];
                                String assembly_isEnable = parms[3];


                                data = URLEncoder.encode("assembly_name", "UTF-8") + "=" + URLEncoder.encode(assembly_name, "UTF-8")
                                        + "&" +
                                        URLEncoder.encode("assembly_location", "UTF-8") + "=" + URLEncoder.encode(assembly_location, "UTF-8")
                                        + "&" +
                                        URLEncoder.encode("assembly_isEnable", "UTF-8") + "=" + URLEncoder.encode(assembly_isEnable, "UTF-8");


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
                                    Add_AssemblyActivity.this.finish();

                                } else {

                                    Toast.makeText(ctx, reader.getString("Message"), Toast.LENGTH_LONG).show();


                                }


                            } catch (Exception ex) {

                                ex.printStackTrace();
                            }


                        }
                    }.execute(method, Name.getText().toString(), latLng, IsEnableVal);


                }


            }
        });


    }
}
