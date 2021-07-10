package com.example.pokedoc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsPatientFragment extends Fragment
{
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference patref;
    String patuid;
    TextInputEditText patusername, patname, patphn, patgender;
    TextView noofdoctors,patemail, patage;
    Button patapplyChangesBtn, patDeleteAccBtn, logoutbtn;
    ImageView imageView;
    /*private Activity view;*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_pat_settings, container, false);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        logoutbtn=rootview.findViewById(R.id.button9);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
            }
        });
        patuid = mUser.getUid();
        patemail = rootview.findViewById(R.id.patemail);
        patage=rootview.findViewById(R.id.pateAge);
        patusername=rootview.findViewById(R.id.patientUname);
        patname=rootview.findViewById(R.id.patientName);
        patphn=rootview.findViewById(R.id.patientPhn);
        patgender=rootview.findViewById(R.id.patientGender);
        noofdoctors=rootview.findViewById(R.id.no_of_doctors);
        patapplyChangesBtn = (Button)rootview.findViewById(R.id.updatePatacc);
        patDeleteAccBtn = (Button)rootview.findViewById(R.id.deletePatacc);
        patref= FirebaseDatabase.getInstance().getReference().child("Users").child(patuid);
        imageView=(ImageView)rootview.findViewById(R.id.help);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openaboutus();
            }
        });

        /*int nightModeFlags = this.getActivity().getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                ConstraintLayout layout = (ConstraintLayout)rootview.findViewById(R.id.layout);
                // Resources layout =getResources();
                //  layout.(R.drawable.backgrounddark);
                layout.setBackgroundResource(R.drawable.backdark);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                ConstraintLayout layoutlight = (ConstraintLayout)rootview.findViewById(R.id.layout);
                layoutlight.setBackgroundResource(R.drawable.back);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                ConstraintLayout layoutd = (ConstraintLayout)rootview.findViewById(R.id.layout);
                // Resources layout =getResources();
                //  layout.(R.drawable.backgrounddark);
                layoutd.setBackgroundResource(R.drawable.backdark);
                break;
        }*/
        //return rootview;

        patref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                   /* if (snap.getKey().equals("Email ID")) {
                        email.setText(snap.getValue().toString());
                    }*/


                    if (snap.getKey().equals("Gender")) {
                        patgender.setText(snap.getValue().toString());
                    }


                    if (snap.getKey().equals("Username")) {
                        patusername.setText(snap.getValue().toString());
                        patusername.setClickable(false);
                        patusername.setEnabled(false);
                        patusername.setTextIsSelectable(false);
                    }

                    if (snap.getKey().equals("Phone Number")) {
                        patphn.setText(snap.getValue().toString());
                    }


                    if (snap.getKey().equals("Name")) {
                        patname.setText(snap.getValue().toString());
                    }
                    if (snap.getKey().equals("Email ID")) {
                        patemail.setText("Email Id : "+snap.getValue().toString());
                    }
                    if (snap.getKey().equals("Age")) {
                        patage.setText("Age : "+snap.getValue().toString());
                    }

                    if (snap.getKey().equals("Doctors")) {

                        Long count = snap.getChildrenCount();
                        Integer cnt = count.intValue();
                        noofdoctors.setText("No of Doctors : "+cnt.toString());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        patapplyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "heyyyy", Toast.LENGTH_SHORT).show();

                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Alert");
                alertDialog.setCancelable(true);
                alertDialog.setMessage("Do You want to apply the changes?\n");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                patref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for(DataSnapshot snapshot1: snapshot.getChildren())
                                        {
                           /* if(snapshot1.getKey().equals("Email ID"))
                            {
                                String mail = email.getText().toString();
                                if(!mail.isEmpty()) {
                                    DatabaseReference ref = snapshot1.getRef();
                                    ref.setValue(mail);
                                }
                                else{
                                    email.setError("Field cannot be Empty");
                                }
                            }*/
                                            if(snapshot1.getKey().equals("Gender"))
                                            {
                                                String Gender = patgender.getText().toString();
                                                if(!Gender.isEmpty()) {
                                                    DatabaseReference ref = snapshot1.getRef();
                                                    ref.setValue(Gender);
                                                }
                                                else{

                                                    patgender.setError("Field cannot be Empty");
                                                }

                                            }
                                            if(snapshot1.getKey().equals("Username"))
                                            {
                                                String Username = patusername.getText().toString();
                                                if(!Username.isEmpty()) {
                                                    DatabaseReference ref = snapshot1.getRef();
                                                    ref.setValue(Username);
                                                }
                                                else{
                                                    patusername.setError("Field cannot be Empty");
                                                }

                                            }
                                            if(snapshot1.getKey().equals("Phone Number"))
                                            {
                                                String PhoneNo = patphn.getText().toString();
                                                if(!PhoneNo.isEmpty()) {
                                                    DatabaseReference ref = snapshot1.getRef();
                                                    ref.setValue(PhoneNo);
                                                }
                                                else{
                                                    patphn.setError("Field cannot be Empty");
                                                }
                                            }
                                            if(snapshot1.getKey().equals("Name"))
                                            {
                                                String Name = patname.getText().toString();
                                                if(!Name.isEmpty()) {
                                                    DatabaseReference ref = snapshot1.getRef();
                                                    ref.setValue(Name);
                                                }
                                                else{
                                                    patname.setError("Field cannot be Empty");
                                                }

                                            }


                                        }
                                        Toast.makeText(getActivity(), "Profile Successfully Updated", Toast.LENGTH_SHORT).show();


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });


        patDeleteAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will result in completely removing your " +
                        "account from the app and you won't able to access the app!");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteUserData(patuid);
                        mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getActivity(), "Account Deleted", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getActivity(),RoleActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();;
            }
        });




        return rootview;
    }

    private void deleteUserData(String patuid) {
        DatabaseReference deUsers = FirebaseDatabase.getInstance().getReference("Users").child(patuid);
        deUsers.removeValue();
    }

    public void openaboutus(){
        Intent intent=new Intent(getActivity(),PatientAboutUs.class);
        startActivity(intent);
    }
}