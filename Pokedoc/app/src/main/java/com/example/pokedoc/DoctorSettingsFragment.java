package com.example.pokedoc;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.HashMap;

public class DoctorSettingsFragment extends Fragment{
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference ref;
    String docuid;
    TextInputEditText username, name, email, phn, gender;
    TextView noofpatients;
    Button applyChangesBtn, DeleteAccBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_doc_settings, container, false);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        docuid = mUser.getUid();
        username=view.findViewById(R.id.usernametext);
        name=view.findViewById(R.id.nametext);
        email=view.findViewById(R.id.emailtext);
        phn=view.findViewById(R.id.phonetext);
        gender=view.findViewById(R.id.gendertext);
        noofpatients=view.findViewById(R.id.textView14);
        applyChangesBtn = (Button)view.findViewById(R.id.applyChange);
        DeleteAccBtn = (Button)view.findViewById(R.id.deleteAcc);

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "heyyyy", Toast.LENGTH_SHORT).show();
                ref= FirebaseDatabase.getInstance().getReference().child("Users").child(docuid);

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot snapshot1: snapshot.getChildren())
                            {
                                Toast.makeText(getActivity(), email.getText().toString(), Toast.LENGTH_SHORT).show();

                                if(snapshot1.getKey().equals("Email ID"))
                                    {
                                        email.setText("Email ID :   " + snapshot1.getValue().toString());
                                        String mail = email.getText().toString();
                                        DatabaseReference ref = snapshot1.getRef();
                                        ref.setValue(mail);
                                    }
                                    if(snapshot1.getKey().equals("Gender"))
                                    {
                                        gender.setText("Gender :    " +snapshot1.getValue().toString());
                                        String Gender = gender.getText().toString();
                                        DatabaseReference ref = snapshot1.getRef();
                                        ref.setValue(Gender);

                                    }
                                    if(snapshot1.getKey().equals("Username"))
                                    {
                                        username.setText("Username :  " +snapshot1.getValue(String.class));
                                        String Username = username.getText().toString();
                                        DatabaseReference ref = snapshot1.getRef();
                                        ref.setValue(Username);

                                    }
                                    if(snapshot1.getKey().equals("Phone Number"))
                                    {
                                        phn.setText("Phone No : "+snapshot1.getValue(String.class));
                                        String PhoneNo = phn.getText().toString();
                                        DatabaseReference ref = snapshot1.getRef();
                                        ref.setValue(PhoneNo);
                                    }
                                    if(snapshot1.getKey().equals("Name"))
                                    {
                                        name.setText("Name :      " +snapshot1.getValue(String.class));
                                        String Name = name.getText().toString();
                                        DatabaseReference ref =snapshot1.getRef();
                                        ref.setValue(Name);

                                    }
                                    if(snapshot1.getKey().equals("Patients"))
                                    {

                                        Long count = snapshot1.getChildrenCount();
                                        Integer cnt = count.intValue();
                                        noofpatients.setText("Patients :   " +cnt.toString());
                                    }

                                }
                            //Toast.makeText(DoctorSettingsFragment.this, "Account not Created, Enter valid data", Toast.LENGTH_SHORT).show();


                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });/*
        DeleteAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.removeValue();
                Intent intent = new Intent(getActivity(), RoleActivity.class);
                startActivity(intent);
            }
        });*/
        return view;
    }
}
