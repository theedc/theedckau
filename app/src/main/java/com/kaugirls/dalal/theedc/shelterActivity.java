package com.kaugirls.dalal.theedc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class shelterActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<Shelter_item> items;
    TextView Add_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter);

        Add_point = findViewById(R.id.Add_point);


        Add_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(shelterActivity.this, Add_ShelterActivity.class);
                startActivityForResult(i, 10);

            }
        });
        listView = findViewById(R.id.Shelter_List);

        GetData();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(shelterActivity.this, Update_Delete_ShelterActivity.class);

                i.putExtra("id", items.get(position).ID);
                i.putExtra("name", items.get(position).shelter_name);
                i.putExtra("Location", items.get(position).shelter_location);
                i.putExtra("type", items.get(position).shelter_type);
                i.putExtra("status", items.get(position).shelter_status);
                i.putExtra("description", items.get(position).shelter_description);
                i.putExtra("capacity", items.get(position).shelter_capacity);
                i.putExtra("Enable", items.get(position).shelter_isEnable);
                startActivityForResult(i, 10);
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                GetData();
            }
        }
    }

    public void GetData() {


        new BackgroundTaskWS(shelterActivity.this) {
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
                    JSONArray reader = new JSONArray(result);
                    items = new ArrayList<Shelter_item>();

                    for (int i = 0; i < reader.length(); i++) {
                        JSONObject Data = (JSONObject) reader.get(i);
                        items.add(
                                new Shelter_item(Data.getString("shelter_id"), Data.getString("shelter_name"), Data.getString("shelter_location"), Data.getString("shelter_type")
                                        , Data.getString("shelter_status"), Data.getString("description"), Data.getString("capacity"), Data.getString("isEnable")));
                    }

                    CustomShelterAdapter adapter = new CustomShelterAdapter(items, shelterActivity.this);

                    listView.setAdapter(adapter);


                } catch (Exception ex) {

                    ex.printStackTrace();
                }


            }
        }.execute("Get_Shelter");


    }


}
