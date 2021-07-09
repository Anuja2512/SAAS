package com.example.pokedoc;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

public class SetReminder extends AppCompatActivity{
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference ref;
    Button settime, setreminder, delete;
    public static String myuid, med, dosage,description, docuid;
    TextView timetext;
    String desc;
    CharSequence name;
    private PendingIntent pendingIntent;
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);
        int nightModeFlags = this.getApplicationContext().getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.layout);
                // Resources layout =getResources();
                //  layout.(R.drawable.backgrounddark);
                layout.setBackgroundResource(R.drawable.backdark);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                ConstraintLayout layoutlight = (ConstraintLayout)findViewById(R.id.layout);
                layoutlight.setBackgroundResource(R.drawable.back);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                ConstraintLayout layoutd = (ConstraintLayout)findViewById(R.id.layout);
                // Resources layout =getResources();
                //  layout.(R.drawable.backgrounddark);
                layoutd.setBackgroundResource(R.drawable.backdark);
                break;
        }

        timetext=findViewById(R.id.textView19);
        settime=findViewById(R.id.button3);
        setreminder=findViewById(R.id.button4);
        delete=findViewById(R.id.button5);
        myuid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        med=getIntent().getStringExtra("med");
        dosage=getIntent().getStringExtra("dosage");
        description=getIntent().getStringExtra("desc");
        docuid=getIntent().getStringExtra("uid");

        creatNotificationChannel();
        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });
        setreminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarm();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm();
            }
        });


    }

    private void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), AlarmReciever.class);
        intent.putExtra("med", med);
        intent.putExtra("desc", desc);
        intent.putExtra("dosage", dosage);
        pendingIntent=PendingIntent.getBroadcast(this, 0, intent, 0);
        if(alarmManager==null)
        {
            alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Reminder Cancelled", Toast.LENGTH_SHORT).show();
    }

    private void creatNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            name = med;
            desc = description;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Myalarm", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showTimePicker()
    {
        timePicker=new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Set Reminder Time")
                .build();
        timePicker.show(getSupportFragmentManager(), "MyAlarm");
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timePicker.getHour()>12)
                {
                   Integer hour = timePicker.getHour()-12;
                   String hr = hour.toString();
                   Integer min = timePicker.getMinute();
                   String minute = min.toString();
                   timetext.setText((hr)+" : " +minute);
                }
                else
                {
                    Integer hour = timePicker.getHour();
                    String hr = hour.toString();
                    Integer min = timePicker.getMinute();
                    String minute = min.toString();
                    timetext.setText((hr)+" : " +minute);
                }
                calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());//new
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND,0);

            }
        });
    }

    private void setAlarm()
    {
        alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReciever.class);
        intent.putExtra("med", med);
        intent.putExtra("desc", desc);
        intent.putExtra("dosage", dosage);
        intent.putExtra("uid",docuid);
        pendingIntent=PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(this, "Reminder has been set for "+med, Toast.LENGTH_SHORT).show();
    }
}