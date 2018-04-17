package com.kaugirls.dalal.theedc;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class video extends AppCompatActivity {
    Button clk;
    VideoView video;
    Button clk1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        clk = findViewById(R.id.button);
        video = findViewById(R.id.videoView);
        clk1 = findViewById(R.id.button1);


    }


    public void videoplay(View v) {
        String videopath = "android.resource://com.kaugirls.dalal.theedc/" + R.raw.flods;

        Uri uri = Uri.parse(videopath);

        video.setVideoURI(uri);
        video.start();
    }

    public void videoplay1(View v) {

        String videopath = "android.resource://com.kaugirls.dalal.theedc/" + R.raw.earthquek;
        Uri uri = Uri.parse(videopath);

        video.setVideoURI(uri);
        video.start();
    }

    public void videoplay2(View v) {

        String videopath = "android.resource://com.kaugirls.dalal.theedc/" + R.raw.evacuation;
        Uri uri = Uri.parse(videopath);

        video.setVideoURI(uri);
        video.start();
    }
}




