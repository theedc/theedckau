package com.kaugirls.dalal.theedc;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class sendApp extends AppCompatActivity {
    EditText ed1, ed2, ed3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_app);

        ed1 = findViewById(R.id.editText);
        ed2 = findViewById(R.id.editText2);


        Button b1 = findViewById(R.id.button);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showNotification();

            }
        });


    }

    private void showNotification() {
        String title = ed1.getText().toString().trim();
        String body = ed2.getText().toString().trim();
        String id = "main_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence name = "Channel Name";
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_HIGH;


            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.WHITE);
            channel.enableVibration(false);
            if (channel != null) {
                notif.createNotificationChannel(channel);
            }


        }
        NotificationCompat.Builder builer = new NotificationCompat.Builder(this, id);
        builer.setSmallIcon(R.mipmap.ic_launcher);
        builer.setContentTitle(title);
        builer.setContentText(body);
        builer.setLights(Color.CYAN, 500, 5000);
        builer.setColor(Color.RED);
        builer.setDefaults(Notification.DEFAULT_SOUND);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1000, builer.build());


    }


}


