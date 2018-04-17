package com.kaugirls.dalal.theedc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity {
    EditText UseridEt, PasswordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UseridEt = findViewById(R.id.username);
        PasswordEt = findViewById(R.id.passwordtext);
    }

    public void cancel(View v) {
        Button button = (Button) v;
        startActivity(new Intent(login.this, select.class));
    }

    public void OnLogin(View view) {

        String username = UseridEt.getText().toString();
        String password = PasswordEt.getText().toString();
        String method = "Login_supervisor";

        BackgroundTask backgroundtask = new BackgroundTask(this);
        backgroundtask.execute(method, username, password);
    }
}
