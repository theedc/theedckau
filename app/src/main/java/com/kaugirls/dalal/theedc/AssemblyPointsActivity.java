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

public class AssemblyPointsActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<Assembly_item> items;
    TextView Add_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assembly_points);

        Add_point = findViewById(R.id.Add_point);


        Add_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AssemblyPointsActivity.this, Add_AssemblyActivity.class);
                startActivityForResult(i, 10);

            }
        });

        listView = findViewById(R.id.Assembly_List);

        GetData();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(AssemblyPointsActivity.this, Delete_AssemblyActivity.class);

                i.putExtra("id", items.get(position).ID);
                i.putExtra("name", items.get(position).assembly_name);
                i.putExtra("Location", items.get(position).assembly_location);
                i.putExtra("Enable", items.get(position).assembly_isEnable);
                startActivityForResult(i, 10);
            }
        });


        //DeleteData(String ID);

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


        new BackgroundTaskWS(AssemblyPointsActivity.this) {
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
                    items = new ArrayList<Assembly_item>();

                    for (int i = 0; i < reader.length(); i++) {
                        JSONObject Data = (JSONObject) reader.get(i);
                        items.add(new Assembly_item(Data.getString("assembly_id"), Data.getString("assembly_name"), Data.getString("assembly_location"), Data.getString("isEnable")));
                    }

                    CustomAdapter adapter = new CustomAdapter(items, AssemblyPointsActivity.this);

                    listView.setAdapter(adapter);


                } catch (Exception ex) {
                    ex.printStackTrace();

                }


            }
        }.execute("Get_Assembly");


    }


}
