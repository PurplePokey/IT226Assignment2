package com.example.androidalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;





public class MainActivity extends AppCompatActivity {

    //variables for alarm creation
    String message;
    boolean monday;
    boolean tuesday;
    boolean wednesday;
    boolean thursday;
    boolean friday;
    boolean saturday;
    boolean sunday;
    int hours;
    int minutes;
    //Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
    LinearLayout alarmNotification;



    public MainActivity(){}

    public String getMessage() {
        return message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set Recursive PopUp invisible on start
        final LinearLayout popUp = (LinearLayout) findViewById(R.id.RecursiveAddPopUp);
        popUp.setVisibility(View.GONE);

        //set alarm notification invisible on start
        alarmNotification = (LinearLayout) findViewById(R.id.AlarmPopUp);
        alarmNotification.setVisibility(View.GONE);

        //Create variables for data fields in recursive alarm
        final TextInputEditText alarmMessage = (TextInputEditText) findViewById(R.id.RecursiveAlarmMessage);
        final TextInputEditText hour = (TextInputEditText) findViewById(R.id.RecursiveAlarmHours);
        final TextInputEditText minute = (TextInputEditText) findViewById(R.id.RecursiveAlarmMinutes);
        final CheckBox mon = (CheckBox) findViewById(R.id.checkBoxM);
        final CheckBox tue = (CheckBox) findViewById(R.id.checkBoxTu);
        final CheckBox wed = (CheckBox) findViewById(R.id.checkBoxW);
        final CheckBox thu = (CheckBox) findViewById(R.id.checkBoxTh);
        final CheckBox fri = (CheckBox) findViewById(R.id.checkBoxF);
        final CheckBox sat = (CheckBox) findViewById(R.id.checkBoxSat);
        final CheckBox sun = (CheckBox) findViewById(R.id.checkBoxSun);
        final TextView messageString = (TextView) findViewById(R.id.AlarmMessageDisplay);
        final TextView locationString = (TextView) findViewById(R.id.LocationAlarmTextBox);

        //Add Alarm recursive
        Button recursiveAdd =(Button) findViewById(R.id.AddRecursiveAlarmButton);
        recursiveAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set all fields to empty and values to false
                alarmMessage.setText(" ");
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

                popUp.setVisibility(View.VISIBLE);
            }
        });
        //Add
        Button add =(Button) findViewById(R.id.RecursiveAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //store data in variables
                message = alarmMessage.getText().toString();
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
                startAlert(hours, minutes, message);


                //close popup when done
                popUp.setVisibility(View.GONE);
            }
        });

        //Cancel - closes popup when cancel button is clicked
        Button cancel =(Button) findViewById(R.id.RecursiveCancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popUp.setVisibility(View.GONE);
            }
        });

    }
    public void startAlert(int hours, int minutes, String message) {

        Intent intent = new Intent(this, Broadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ((minutes * 1000 *60) + (hours *1000*60*60)), pendingIntent);
        Toast.makeText(this, "Alarm set in " + hours + " hours and " + minutes + " minutes", Toast.LENGTH_LONG).show();
}
}
