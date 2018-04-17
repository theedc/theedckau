package com.kaugirls.dalal.theedc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class select extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button d = findViewById(R.id.super_but);
        d.bringToFront();
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(select.this, login.class);
                startActivity(myIntent);
            }
        });

        Button r = findViewById(R.id.member_but);
        r.bringToFront();
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(select.this, userOptions.class);
                startActivity(myIntent);
            }
        });
    }


}
