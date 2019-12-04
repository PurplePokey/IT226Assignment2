package com.example.androidalarm;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Broadcast extends BroadcastReceiver {

   public static final String MESSAGE = "MESSAGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Toast.makeText(context, formatter.format(currentTime) + " Message: " + intent.getStringExtra("MESSAGE") + "\nLocation: " + intent.getStringExtra("location") , Toast.LENGTH_LONG).show();



    }

}
