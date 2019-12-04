package com.example.androidalarm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    //location alarm
    private TextView locationAlarmTimer;
    private TextView latitude;
    private TextView longitude;
    private LocationManager locationManager;
    private Criteria criteria;
    private MyLocationListener mylistener;
    private Location location;
    private long locationAlarmDurrarion=60000*2;
    private String locationAlarmNote;
    private double startLat;
    private double startLng;
    private Button addLocAlarm;
    private CountDownTimer count;
    private boolean locationChanged;
    private boolean start=false;
    private EditText durration;
    private EditText messageLoc;
    private String alarmMessage;
    private Button recursiveAdd;
    private Button oneTimeAdd;



    //variables for alarm creation
    String messageRec;
    String messageOT;
    boolean monday;
    boolean tuesday;
    boolean wednesday;
    boolean thursday;
    boolean friday;
    boolean saturday;
    boolean sunday;
    int hours;
    int minutes;
    int hoursOT;
    int minutesOT;


    LinearLayout alarmNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //set Recursive PopUp invisible on start
        final LinearLayout recPopUp = (LinearLayout) findViewById(R.id.RecursiveAddPopUp);
        recPopUp.setVisibility(View.GONE);

        //set Location PopUp invisible on start
        final LinearLayout locPopUp = (LinearLayout) findViewById(R.id.LocationAlarmPopUp);
        locPopUp.setVisibility(View.GONE);

        //set OneTime PopUp invisible on start
        final LinearLayout oneTimePopUp = (LinearLayout) findViewById(R.id.OneTimeAddPopUp);
        oneTimePopUp.setVisibility(View.GONE);

        //Create variables for data fields in recursive alarm
        final TextInputEditText alarmMessageRec = (TextInputEditText) findViewById(R.id.RecursiveAlarmMessage);
        final TextInputEditText hour = (TextInputEditText) findViewById(R.id.RecursiveAlarmHours);
        final TextInputEditText minute = (TextInputEditText) findViewById(R.id.RecursiveAlarmMinutes);
        final CheckBox mon = (CheckBox) findViewById(R.id.checkBoxM);
        final CheckBox tue = (CheckBox) findViewById(R.id.checkBoxTu);
        final CheckBox wed = (CheckBox) findViewById(R.id.checkBoxW);
        final CheckBox thu = (CheckBox) findViewById(R.id.checkBoxTh);
        final CheckBox fri = (CheckBox) findViewById(R.id.checkBoxF);
        final CheckBox sat = (CheckBox) findViewById(R.id.checkBoxSat);
        final CheckBox sun = (CheckBox) findViewById(R.id.checkBoxSun);

        //OneTime alarm
        final TextView alarmMessageOneTime = (TextView) findViewById(R.id.OneTimeAlarmMessage);
        final TextInputEditText hourOT = (TextInputEditText) findViewById(R.id.OneTimeAlarmHours);
        final TextInputEditText minuteOT = (TextInputEditText) findViewById(R.id.OneTimeAlarmMinutes);


        //set alarm notification invisible on start
        alarmNotification = (LinearLayout) findViewById(R.id.AlarmPopUp);
        alarmNotification.setVisibility(View.GONE);

        latitude = (TextView) findViewById(R.id.lat);
        longitude = (TextView) findViewById(R.id.lon);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Define the criteria how to select the location provider
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);    //default
        mylistener = new MyLocationListener();

        // Check if the app has permission to access location data
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        11);
            }
        } else {
            Toast.makeText(this, "" + Manifest.permission.ACCESS_FINE_LOCATION + " is already granted.", Toast.LENGTH_SHORT).show();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mylistener);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }

        // Check if GPS is enabled.
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (location != null) {
                latitude.setText("Latitude: " + String.valueOf(location.getLatitude()));
                longitude.setText("Longitude: " + String.valueOf(location.getLongitude()));
            }
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Goto Settings Page To Enable GPS", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(callGPSSettingIntent);
                        }
                    });
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }

        //
        //RECURSIVE ALARM BUTTONS
        //
        //Add Alarm Button recursive
        recursiveAdd =(Button) findViewById(R.id.AddRecursiveAlarmButton);
        recursiveAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recPopUp.setVisibility(View.VISIBLE);
                //set all fields to empty and values to false
                alarmMessageRec.setText(" ");
                hour.setText("0");
                minute.setText("0");
                mon.setChecked(false);
                tue.setChecked(false);
                wed.setChecked(false);
                thu.setChecked(false);
                fri.setChecked(false);
                sat.setChecked(false);
                sun.setChecked(false);
                monday = false;
                tuesday = false;
                wednesday = false;
                thursday = false;
                friday = false;
                saturday  = false;
                sunday = false;


            }
        });
        //Add
        Button add =(Button) findViewById(R.id.RecursiveAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //store data in variables
                messageRec = alarmMessageRec.getText().toString();
                if(mon.isChecked()){
                    monday=true;
                }
                if(tue.isChecked()){
                    tuesday=true;
                }
                if(wed.isChecked()){
                    wednesday=true;
                }
                if(thu.isChecked()){
                    thursday=true;
                }
                if(fri.isChecked()){
                    friday=true;
                }
                if(sat.isChecked()){
                    saturday=true;
                }
                if(sun.isChecked()){
                    sunday=true;
                }
                hours= Integer.parseInt(hour.getText().toString());
                minutes= Integer.parseInt(minute.getText().toString());


                //create the alarm
                startAlert(hours, minutes, messageRec, 0);


                //close popup when done
                recPopUp.setVisibility(View.GONE);
            }
        });

        //Cancel - closes popup when cancel button is clicked
        Button cancel =(Button) findViewById(R.id.RecursiveCancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                recPopUp.setVisibility(View.GONE);
            }
        });

        //
        //ONETIME ALARM BUTTONS
        //
        //Add Alarm Button One Time
        oneTimeAdd =(Button) findViewById(R.id.AddOneTimeAlarmButton);
        oneTimeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set all fields to empty and values to false
                alarmMessageOneTime.setText(" ");
                hourOT.setText("0");
                minuteOT.setText("0");

                oneTimePopUp.setVisibility(View.VISIBLE);
            }
        });
        //Add Button on One Time popup
        Button addOneTime =(Button) findViewById(R.id.OneTimeAdd);
        addOneTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //store data in variables
                messageOT = alarmMessageOneTime.getText().toString();
                hoursOT= Integer.parseInt(hourOT.getText().toString());
                minutesOT= Integer.parseInt(minuteOT.getText().toString());


                //create the alarm
                startAlert(hoursOT, minutesOT, messageOT, 1);


                //close popup when done
                oneTimePopUp.setVisibility(View.GONE);
            }
        });

        //Cancel button on one time popup
        Button cancelOneTime =(Button) findViewById(R.id.OneTimeCancel);
        cancelOneTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                oneTimePopUp.setVisibility(View.GONE);
            }
        });

        //timer
        locationAlarmTimer= (TextView) findViewById(R.id.locationAlarmTimer);
        locationAlarmTimer.setText("0:00 No Alarm Set");//default Time

        durration = (EditText) findViewById(R.id.Durration);
        durration.setText("2");
        messageLoc = (EditText) findViewById(R.id.LocationAlarmmessage);
        addLocAlarm =(Button) findViewById(R.id.AddLocationAlarm);
        addLocAlarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //popUpWindow
                start = false;
                locPopUp.setVisibility(View.VISIBLE);
                startLat=location.getLatitude();
                startLng=location.getLongitude();
                locationChanged=false;


                Button locationAlarmDismiss = (Button) findViewById(R.id.LocationAlarmDismiss);
                locationAlarmDismiss.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        start=true;
                        alarmMessage= messageLoc.getText().toString();
                        String value= durration.getText().toString();
                        double inputValue= Double.parseDouble(value);
                        locationAlarmDurrarion= (long) inputValue*60000;
                        locPopUp.setVisibility(View.GONE);

                        //Starting Timer
                        if(start==true) {
                            count = new CountDownTimer(locationAlarmDurrarion, 1000) {
                                @Override
                                public void onTick(long l) {
                                    locationAlarmDurrarion = l;
                                    updateTimer(locationAlarmDurrarion + 1000);
                                    if (locationChanged == true) {
                                        resetTimer();
                                    }


                                }

                                @Override
                                public void onFinish() {
                                    alert(alarmMessage.toString());

                                    count.cancel();
                                    locationChanged = false;
                                    locationAlarmTimer.setText("0:00 No Alarm Set");
                                }

                            }.start();
                        }
                    }
                });

            }

        });

    }
    public void alert(String m){
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setMessage("Get up and Walk!  \n"+ m)
                .setCancelable(false)
                .setPositiveButton("Reset Alarm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        count.cancel();
                        locationChanged=false;
                        count.start();

                    }
                });
        al.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
            }
        });
        AlertDialog alert = al.create();
        alert.show();


    }



    public void updateTimer(long t){
        int minutes= (int) t / 60000;
        int seconds = (int) t % 60000 /1000;

        String currentTime;
        currentTime = ""+minutes;
        currentTime = currentTime +":";

        if(seconds<10) currentTime = currentTime + "0";


        currentTime = currentTime + seconds;

        locationAlarmTimer.setText(currentTime);
    }
    public void resetTimer(){
        count.cancel();
        locationChanged=false;
        count.start();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 11:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)    {
                    Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No permission !", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            // Initialize the location fields
            latitude.setText("Latitude: " + String.valueOf(location.getLatitude()));
            longitude.setText("Longitude: " + String.valueOf(location.getLongitude()));
            Toast.makeText(MainActivity.this, "Location changed!",
                    Toast.LENGTH_SHORT).show();
            locationChanged=true;
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(MainActivity.this, provider + "'s status changed to " + status + "!",
                    Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(MainActivity.this, "Provider " + provider + " enabled!",
                    Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(MainActivity.this, "Provider " + provider + " disabled!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void startAlert(int hours, int minutes, String message, int value) {
        //value 0 will be for recursive, value 1 is for one time.

        Intent intent = new Intent(this, Broadcast.class);
        intent.putExtra(Broadcast.MESSAGE, messageOT);



        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        if (value==1) {

            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ((minutes * 1000 * 60) + (hours * 1000 * 60 * 60)), pendingIntent);
            Toast.makeText(this, "Alarm set in " + hours + " hours and " + minutes + " minutes", Toast.LENGTH_LONG).show();
        }
        else{

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, minutes);
            calendar.set(Calendar.SECOND, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Alarm set for " + hours + ":"+ minutes, Toast.LENGTH_LONG).show();
        }

    }
}

