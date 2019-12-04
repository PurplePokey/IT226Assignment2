package com.example.androidalarm;

import android.app.Activity;
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
    public static final String CHANNEL_ID= "NOTIFICATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Toast.makeText(context, formatter.format(currentTime) + " Message: " + intent.getStringExtra("MESSAGE") , Toast.LENGTH_LONG).show();
        //NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
        //        .setContentTitle("Alarm")
        //        .setContentText(intent.getStringExtra("MESSAGE")
        //        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

}
