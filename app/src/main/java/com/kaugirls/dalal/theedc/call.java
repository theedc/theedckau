package com.kaugirls.dalal.theedc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class call extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        ImageButton mEmail = findViewById(R.id.emailbutt);
        mEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "edc@kau.edu.sa", null));
                intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");

                startActivity(Intent.createChooser(intent, "Send Email"));

            }
        });


        ImageButton mPhone = findViewById(R.id.callbutt);
        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "0126951111";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);

            }
        });
    }
}
