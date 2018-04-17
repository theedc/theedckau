package com.kaugirls.dalal.theedc;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


public class BackgroundTaskWS extends AsyncTask<String, Void, String> {

    Context ctx;


    ProgressDialog dialog;

    BackgroundTaskWS(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... parms) {
        return "";
    }

    @Override
    protected void onProgressUpdate(Void... values) {

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {

        dialog = ProgressDialog.show(ctx, "",
                "Loading. Please wait...", true);


    }

    @Override
    protected void onPostExecute(String result) {


    }
}


