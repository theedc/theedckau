package com.kaugirls.dalal.theedc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class supOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_options);
        ImageView n = findViewById(R.id.notification_app);
        n.bringToFront();
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(supOptions.this, notif.class);
                startActivity(myIntent);
            }
        });

        ImageButton homePageButton = findViewById(R.id.homePage);
        homePageButton.bringToFront();
        homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(supOptions.this, home.class);
                startActivity(myIntent);
            }
        });
        ImageView editShelter = findViewById(R.id.edit_shelter);
        editShelter.bringToFront();
        editShelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(supOptions.this, shelterAndAssembly.class);
                startActivity(myIntent);
            }
        });


    }


}
