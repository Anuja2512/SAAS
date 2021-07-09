package com.example.pokedoc;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReciever extends BroadcastReceiver {
    String med, dosage,description;
    @Override
    public void onReceive(Context context, Intent intent) {
        med=PrescriptionPatient.med;
        dosage=PrescriptionPatient.dosage;
        description=PrescriptionPatient.description;
        Intent tohome = new Intent(context, PatientDashboard.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,tohome,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Myalarm")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Medicine Reminder for: "+med)
                .setContentText(description)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());
    }
}
