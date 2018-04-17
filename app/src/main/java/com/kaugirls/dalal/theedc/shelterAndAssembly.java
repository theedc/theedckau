package com.kaugirls.dalal.theedc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class shelterAndAssembly extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_assembly);


        ImageView editAssembly = findViewById(R.id.Assemblybtn);
        editAssembly.bringToFront();
        editAssembly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(shelterAndAssembly.this, AssemblyPointsActivity.class);
                startActivity(myIntent);
            }
        });


        ImageView editShelter = findViewById(R.id.Shelterbtn);
        editShelter.bringToFront();
        editShelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(shelterAndAssembly.this, shelterActivity.class);
                startActivity(myIntent);
            }
        });


    }

}
