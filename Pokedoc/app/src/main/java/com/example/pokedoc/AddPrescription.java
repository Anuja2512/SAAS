package com.example.pokedoc;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

public class AddPrescription extends AppCompatActivity {
    Button addprescbtn;
    String patuid, medicine, dosage, description;
    LinearLayout mainlayout;
    DatabaseReference reference, reference1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prescription);
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
        patuid=getIntent().getStringExtra("uid");
        mainlayout=findViewById(R.id.mainlayout);
        addprescbtn = findViewById(R.id.button7);
        addprescbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddPrescription.this, PrescriptionDetails.class);
                intent.putExtra("uid", patuid);
                startActivity(intent);
                finish();
            }
        });

        reference= FirebaseDatabase.getInstance().getReference().child("Users").child(patuid).child("Prescriptions");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        if(snapshot1.exists())
                        {
                            for(DataSnapshot snapshot2: snapshot1.getChildren())
                            {
                                if(snapshot1.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                {
                                    LinearLayout layout = new LinearLayout(getApplicationContext());
                                    layout.setOrientation(LinearLayout.VERTICAL);
                                    LinearLayout emptylayout = new LinearLayout(getApplicationContext());
                                    emptylayout.setOrientation(LinearLayout.VERTICAL);
                                    emptylayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 60));
                                    TextView medtext = new TextView(getApplicationContext());
                                    TextView dosetext = new TextView(getApplicationContext());
                                    TextView desctext = new TextView(getApplicationContext());
                                    TextView doctext = new TextView(getApplicationContext());
                                    TextView pattext = new TextView(getApplicationContext());
                                    TextView datetext = new TextView(getApplicationContext());
                                    mainlayout.addView(layout);
                                    layout.addView(pattext);
                                    layout.addView(medtext);
                                    layout.addView(dosetext);
                                    layout.addView(desctext);
                                    layout.addView(datetext);
                                    layout.addView(emptylayout);
                                    for(DataSnapshot snapshot3: snapshot2.getChildren())
                                    {

                                        if(snapshot3.exists())
                                           {
                                                if(snapshot3.getKey().equals("Medicine"))
                                                {
                                                    medtext.setText("Medicine " +snapshot3.getValue(String.class));
                                                    medtext.setTextColor(Color.parseColor("#03A9F4"));
                                                }
                                                if(snapshot3.getKey().equals("Dosage"))
                                                {
                                                    dosetext.setText("Dosage "+snapshot3.getValue(String.class));
                                                    dosetext.setTextColor(Color.parseColor("#03A9F4"));
                                                }
                                                if(snapshot3.getKey().equals("Description"))
                                                {
                                                    desctext.setText("Description "+snapshot3.getValue(String.class));
                                                    desctext.setTextColor(Color.parseColor("#03A9F4"));
                                                }
                                                if(snapshot3.getKey().equals("Doctor"))
                                                {
                                                    doctext.setText("Doctor "+ snapshot3.getValue(String.class));
                                                    doctext.setTextColor(Color.parseColor("#03A9F4"));
                                                }
                                                if(snapshot3.getKey().equals("Patient"))
                                                {
                                                    pattext.setText("Patient "+snapshot3.getValue(String.class));
                                                    pattext.setTextColor(Color.parseColor("#03A9F4"));
                                                }
                                               if(snapshot3.getKey().equals("Date"))
                                               {
                                                   datetext.setText("Date:  "+snapshot3.getValue(String.class));
                                                   datetext.setTextColor(Color.parseColor("#03A9F4"));
                                               }

                                           }

                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}