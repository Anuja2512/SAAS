package com.example.pokedoc;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PrescriptionDetails extends AppCompatActivity {
    String patuid, medicine, dose, description;
    Button submit, addmed;
    EditText med, desc, dosage;
    DatabaseReference reference, reference1, reference2, reference3;

    HashMap<String, String> map= new HashMap<>();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_details);
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

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
        med = findViewById(R.id.medtext);
        dosage=findViewById(R.id.dosetext);
        desc=findViewById(R.id.destext);
        addmed=findViewById(R.id.button10);

        addmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(med.getText().toString().equals(""))
                    {
                        med.requestFocus();
                        med.setError("Enter name of the Medicine");
                    }
                    else if(dosage.getText().toString().equals(""))
                    {
                        dosage.requestFocus();
                        dosage.setError("Enter dosage");
                    }
                    else
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(PrescriptionDetails.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setCancelable(true);
                        alertDialog.setMessage("Medicine Details: \nName: "+med.getText().toString()+"\nDosage: "+dosage.getText().toString()+"\nDescription: "+desc.getText().toString()+"\nDo you wish to submit?\n");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        reference=FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Prescriptions").child(patuid);
                                        reference1=FirebaseDatabase.getInstance().getReference().child("Users").child(patuid).child("Prescriptions").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                        medicine=med.getText().toString();
                                        dose=dosage.getText().toString();
                                        description=desc.getText().toString();
                                        reference2=FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot snapshot1: snapshot.getChildren())
                                                {
                                                    if(snapshot1.getKey().equals("Username"))
                                                    {
                                                        String docusername = snapshot1.getValue(String.class);
                                                        map.put("Doctor", docusername);
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        reference3=FirebaseDatabase.getInstance().getReference().child("Users").child(patuid);
                                        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                                for(DataSnapshot snapshot1: snapshot.getChildren())
                                                {
                                                    if(snapshot1.getKey().equals("Username"))
                                                    {
                                                        String patusername = snapshot1.getValue(String.class);
                                                        map.put("Patient", patusername);
                                                      //  Toast.makeText(PrescriptionDetails.this, snapshot1.getValue(String.class), Toast.LENGTH_SHORT).show();
                                                        map.put("Medicine", medicine);
                                                        map.put("Dosage", dose);
                                                        map.put("Description", description);
                                                        map.put("Date", date);
                                                        map.put("Reminder", "Not Set");
                                                    }

                                                }
                                                reference.push().setValue(map);
                                                reference1.push().setValue(map);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        med.setText("");
                                        dosage.setText("");
                                        desc.setText("");
                                        Toast.makeText(PrescriptionDetails.this, "Medicine added Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }

            }
        });

    }
}