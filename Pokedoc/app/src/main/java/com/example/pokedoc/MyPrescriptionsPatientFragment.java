package com.example.pokedoc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.storage.*;
import org.jetbrains.annotations.NotNull;

import javax.xml.transform.Result;

public class MyPrescriptionsPatientFragment extends Fragment{
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference ref;
    Button addPatients;
    String myuid, docuid, pattext, remindertext;
    LinearLayout mainlayout;
    Button btn_filePicker;
    Intent myfileIntent;
    EditText editPDFName;
    Button btn_upload;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_pat_myprescriptions, container, false);
        mainlayout=rootview.findViewById(R.id.mainlayout);
        myuid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(myuid).child("Prescriptions");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                 for(DataSnapshot snapshot1:snapshot.getChildren())
                    {
                         for(DataSnapshot snapshot2: snapshot1.getChildren())
                            {
                                String hash = snapshot2.getKey();
                                Button btn=new Button(getActivity());
                                Intent toSetReminder = new Intent(getActivity(), PrescriptionPatient.class);
                                for(DataSnapshot snapshot3: snapshot2.getChildren())
                                {
                                    if(snapshot3.getKey().equals("Doctor"))
                                    {
                                        String doctext=snapshot3.getValue(String.class);
                                        btn.setText(doctext);
                                        mainlayout.addView(btn);
                                        toSetReminder.putExtra("docusername",doctext);

                                    }
                                    if(snapshot3.getKey().equals("Dosage"))
                                    {
                                        String dosage=snapshot3.getValue(String.class);
                                        toSetReminder.putExtra("dosage", dosage);
                                    }
                                    if(snapshot3.getKey().equals("Medicine"))
                                    {
                                        String med=snapshot3.getValue(String.class);
                                        toSetReminder.putExtra("med", med);
                                    }
                                    if(snapshot3.getKey().equals("Description"))
                                    {
                                        String description=snapshot3.getValue(String.class);
                                        toSetReminder.putExtra("desc", description);
                                    }
                                    if(snapshot3.getKey().equals("Reminder"))
                                    {
                                        String reminder=snapshot3.getValue(String.class);
                                        toSetReminder.putExtra("reminder", reminder);
                                    }
                                    btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            toSetReminder.putExtra("uid", docuid);
                                            toSetReminder.putExtra("hash", hash);
                                            startActivity(toSetReminder);

                                        }
                                    });


                                }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return rootview;
    }
}
