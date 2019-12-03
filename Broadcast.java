package com.example.androidalarm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Broadcast extends BroadcastReceiver {

    MainActivity main = new MainActivity();
    @Override
    public void onReceive(Context context, Intent intent) {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Toast.makeText(context, formatter.format(currentTime) + " Message: " + main.getMessage(), Toast.LENGTH_LONG).show();
    }

}
