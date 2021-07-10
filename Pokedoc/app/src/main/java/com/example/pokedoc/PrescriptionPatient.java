package com.example.pokedoc;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class PrescriptionPatient extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference ref;
    Button settime, setreminder, delete;
    FloatingActionButton addreminder;
    public static String myuid, docuid, med, dosage,description, docusename;
    TextView medtext, desctext, dosagetext, docusernameext, remindertext,datetext;
    String desc, reminder, hash, date;
    CharSequence name;
    AlertDialog.Builder builder;
    private PendingIntent pendingIntent;
    private MaterialTimePicker timePicker;
    private AlarmManager alarmManager;
    public MaterialDatePicker datePicker;
    long today;
    static Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_patient);
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
        myuid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        docuid=getIntent().getStringExtra("uid");
        med=getIntent().getStringExtra("med");
        date=getIntent().getStringExtra("date");
        dosage=getIntent().getStringExtra("dosage");
        description=getIntent().getStringExtra("desc");
        docusename=getIntent().getStringExtra("docusername");
        reminder=getIntent().getStringExtra("reminder");
        hash=getIntent().getStringExtra("hash");
        datetext=findViewById(R.id.dateee);
        datetext.setText("Date: "+date);
        docusernameext=findViewById(R.id.textView29);
        remindertext=findViewById(R.id.textView32);
        medtext=findViewById(R.id.textView26);
        dosagetext=findViewById(R.id.textView27);
        desctext=findViewById(R.id.textView28);
        medtext.setText("Medicine: "+ med);
        desctext.setText("Description: " + description);
        dosagetext.setText("Dosage: " +dosage);
        docusernameext.setText("Doctor: "+docusename);
        remindertext.setText("Reminder: "+ reminder);
        addreminder=findViewById(R.id.floatingActionButton);
        addreminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reminder.equals("Set")) {
                    builder = new AlertDialog.Builder(PrescriptionPatient.this);

                    //Setting message manually and performing action on button click
                    builder.setMessage("Do you wish to cancel the reminder ?\n\n")
                            .setTitle("Alert")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                   ref= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Prescriptions").child(docuid).child(hash);
                                   ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                           for(DataSnapshot snapshot1: snapshot.getChildren())
                                           {
                                              // Toast.makeText(PrescriptionPatient.this, snapshot1.getKey(), Toast.LENGTH_SHORT).show();
                                                   if(snapshot1.getKey().equals("Reminder"))
                                                   {
                                                       DatabaseReference ref = snapshot1.getRef();
                                                       ref.setValue("Not Set");
                                                       Intent tohome = new Intent(PrescriptionPatient.this, PatientDashboard.class);
                                                       startActivity(tohome);
                                                       finish();
                                                     //  Toast.makeText(PrescriptionPatient.this, ref.toString(), Toast.LENGTH_SHORT).show();
                                                   }

                                           }
                                       }

                                       @Override
                                       public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                       }
                                   });
                                    cancelAlarm();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("AlertDialogExample");
                    alert.show();
                } else showTimePicker();
            }

        });
        }

    private void showTimePicker()
    {
        timePicker=new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Set Reminder for: \n"+medtext.getText()+"\n" +dosagetext.getText()+ "\n")
                .build();
        timePicker.show(getSupportFragmentManager(), "MyAlarm");
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long today = MaterialDatePicker.todayInUtcMilliseconds();
                calendar=Calendar.getInstance();
                calendar.clear();
                calendar.setTimeInMillis(today);
                calendar.set(Calendar.MONTH, Calendar.JANUARY);
                CalendarConstraints.Builder calenderconstraint = new CalendarConstraints.Builder();
                calenderconstraint.setValidator(DateValidatorPointForward.now());
                MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
                materialDateBuilder.setTitleText("Set Reminder");
                materialDateBuilder.setCalendarConstraints(calenderconstraint.build());
                datePicker=materialDateBuilder.build();
                datePicker.show(getSupportFragmentManager(), "MyDate");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        calendar.setTimeInMillis(selection);
                        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                        calendar.set(Calendar.MINUTE, timePicker.getMinute());
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND,0);
                        creatNotificationChannel();
                        setAlarm();
                        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Prescriptions").child(docuid).child(hash);
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for(DataSnapshot snapshot1: snapshot.getChildren())
                                {
                                   // Toast.makeText(PrescriptionPatient.this, snapshot1.getKey(), Toast.LENGTH_SHORT).show();

                                    if(snapshot1.getKey().equals("Reminder"))
                                    {
                                        DatabaseReference ref = snapshot1.getRef();
                                        ref.setValue("Set");
                                        Intent tohome = new Intent(PrescriptionPatient.this, PatientDashboard.class);
                                        startActivity(tohome);
                                        finish();
                                    }



                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });

                    }
                });

            }
        });
        timePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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

}