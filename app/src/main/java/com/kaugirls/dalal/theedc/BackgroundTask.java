package com.kaugirls.dalal.theedc;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class BackgroundTask extends AsyncTask<String, Void, String> {

    public login delegate = null;
    Context ctx;
    AlertDialog alertD;
    String type;


    BackgroundTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... parms) {

        String result = "";
        type = parms[0];
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

            if (type.equals("Login_supervisor")) {
                String supervisor_id = parms[1];
                String supervisor_paasword = parms[2];

                data = URLEncoder.encode("supervisor_id", "UTF-8") + "=" + URLEncoder.encode(supervisor_id, "UTF-8") + "&" +
                        URLEncoder.encode("supervisor_password", "UTF-8") + "=" + URLEncoder.encode(supervisor_paasword, "UTF-8");


            }

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
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {
        alertD = new AlertDialog.Builder(ctx).create();
        alertD.setTitle("كلمة المرور");
        alertD.setButton("حسنا", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

    }

    @Override
    protected void onPostExecute(String result) {


        if (result.equals("logged in successfully")) {

            BackgroundTask.this.ctx.startActivity(new Intent(BackgroundTask.this.ctx, supOptions.class));
        } else {

            alertD.setMessage("عذرا ، كلمة المرور غير صحيحة");
            alertD.show();


        }
    }
}


