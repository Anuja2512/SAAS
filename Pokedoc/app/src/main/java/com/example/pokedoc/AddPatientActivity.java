package com.example.pokedoc;

import android.content.res.Configuration;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.HashMap;

public class AddPatientActivity extends AppCompatActivity {
    String docUid, patUid, patusern;
    HashMap<String, String> map;
    EditText text;
    Button add;
    DatabaseReference puserreference, docreference;
    FirebaseUser user;
    Boolean b=true;
    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

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
        text=findViewById(R.id.patusern);
        user = FirebaseAuth.getInstance().getCurrentUser();
        docUid = user.getUid();
        add=findViewById(R.id.button2);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patusern=text.getText().toString();
                puserreference = FirebaseDatabase.getInstance().getReference().child("Users");
                puserreference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot snapshot1 : snapshot.getChildren())
                            {
                                if(snapshot1.exists())
                                {
                                    for(DataSnapshot snapshot2 : snapshot1.getChildren())
                                    {
                                        if(snapshot2.exists())
                                        {
                                            if(snapshot2.getKey().equals("Username")&&snapshot2.getValue().equals(patusern))
                                            {
                                                patUid = snapshot1.getKey();
                                                if(patUid.equals(docUid))
                                                {
                                                    Toast.makeText(AddPatientActivity.this, "Enter the patient's Username", Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    docreference=FirebaseDatabase.getInstance().getReference().child("Users").child(docUid).child("Patients");
                                                    docreference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                for(DataSnapshot snapshot3: snapshot.getChildren())
                                                                {
                                                                    if(snapshot3.exists())
                                                                    {
                                                                        for(DataSnapshot snapshot4: snapshot3.getChildren())
                                                                        {
                                                                            if(snapshot4.getValue().equals(patusern))
                                                                            {
                                                                                Toast.makeText(AddPatientActivity.this, "Patient is already registered", Toast.LENGTH_SHORT).show();
                                                                                b=false;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                if(b)
                                                                {
                                                                    Toast.makeText(AddPatientActivity.this, "blah", Toast.LENGTH_SHORT).show();
                                                                    map=new HashMap<String, String>();
                                                                    map.put(patUid, patusern);
                                                                    docreference.push().setValue(map);
                                                                    puserreference.child(patUid).child("Doctors");
                                                                    puserreference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(docUid);
                                                                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                    if(snapshot.exists())
                                                                                    {
                                                                                        for(DataSnapshot snapshot3: snapshot.getChildren())
                                                                                        {
                                                                                            if(snapshot3.exists())
                                                                                            {
                                                                                                if(snapshot3.getKey().equals("Username"))
                                                                                                {
                                                                                                    String docusername = snapshot3.getValue(String.class);
                                                                                                    map = new HashMap<>();
                                                                                                    map.put(docUid, docusername);
                                                                                                    puserreference.child(patUid).child("Doctors").push().setValue(map);
                                                                                                    Toast.makeText(AddPatientActivity.this, "Patient Added Successfully", Toast.LENGTH_SHORT).show();
                                                                                                    finish();
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                }
                                                                            });

                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });
                                                                }
                                                            }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddPatientActivity.this, "Failed to add Patient", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}