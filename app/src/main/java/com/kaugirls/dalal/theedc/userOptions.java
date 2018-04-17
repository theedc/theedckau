package com.kaugirls.dalal.theedc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class userOptions extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_options);
        ImageView home = findViewById(R.id.home);
        home.bringToFront();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(userOptions.this, home.class);
                startActivity(myIntent);
            }
        });

        ImageView call = findViewById(R.id.call);
        call.bringToFront();
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(userOptions.this, call.class);
                startActivity(myIntent);
            }
        });


        ImageView video = findViewById(R.id.video);
        video.bringToFront();
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(userOptions.this, video.class);
                startActivity(myIntent);
            }
        });

        ImageView aboutEDC = findViewById(R.id.aboutEDC);
        aboutEDC.bringToFront();
        aboutEDC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(userOptions.this, aboutEDC.class);
                startActivity(myIntent);
            }
        });


        ImageView who = findViewById(R.id.who);
        who.bringToFront();
        who.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(userOptions.this, aboutUs.class);
                startActivity(myIntent);
            }
        });


        ImageView map = findViewById(R.id.map);
        map.bringToFront();
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData();
            }
        });


    }


    public void GetData() {


        new BackgroundTaskWS(userOptions.this) {
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


                    Intent myIntent = new Intent(userOptions.this, MapsActivityCurrentPlace.class);
                    myIntent.putExtra("Points", result);
                    startActivity(myIntent);


                } catch (Exception ex) {

                    ex.printStackTrace();
                }


            }
        }.execute("GetAllPoints");


    }
}




