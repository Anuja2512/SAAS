package com.example.pokedoc;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    TextView noofpatients,docemail, docAge;
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
        docemail = view.findViewById(R.id.docemail);
        docAge= view.findViewById(R.id.docage);
        phn=view.findViewById(R.id.phonetext);
        gender=view.findViewById(R.id.gendertext);
        noofpatients=view.findViewById(R.id.textView14);
        applyChangesBtn = (Button)view.findViewById(R.id.applyChange);
        DeleteAccBtn = (Button)view.findViewById(R.id.deleteAcc);
        ref= FirebaseDatabase.getInstance().getReference().child("Users").child(docuid);


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {

                    if (snap.getKey().equals("Gender")) {
                        gender.setText(snap.getValue().toString());
                    }


                    if (snap.getKey().equals("Username")) {
                        username.setText(snap.getValue().toString());
                        username.setClickable(false);
                        username.setEnabled(false);
                        username.setTextIsSelectable(false);
                    }
                    if (snap.getKey().equals("Phone Number")) {
                        phn.setText(snap.getValue().toString());
                    }

                    if (snap.getKey().equals("Name")) {
                        name.setText(snap.getValue().toString());
                    }
                    if (snap.getKey().equals("Email ID")) {
                        docemail.setText("Email Id : "+snap.getValue().toString());
                    }
                    if (snap.getKey().equals("Age")) {
                        docAge.setText("Age : "+snap.getValue().toString());
                    }

                    if (snap.getKey().equals("Patients")) {

                        Long count = snap.getChildrenCount();
                        Integer cnt = count.intValue();
                        noofpatients.setText("No of Patients : "+cnt.toString());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
                 String MobilePattern = "[0-9]{10}";

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "heyyyy", Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Alert");
                alertDialog.setCancelable(true);
                alertDialog.setMessage("Do You want to apply the changes?\n");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for(DataSnapshot snapshot1: snapshot.getChildren()) {

                                            if (snapshot1.getKey().equals("Gender")) {
                                                String Gender = gender.getText().toString();
                                                if (!Gender.isEmpty()) {
                                                    DatabaseReference ref = snapshot1.getRef();
                                                    ref.setValue(Gender);
                                                } else {
                                                    gender.setError("Field cannot be Empty");
                                                    gender.requestFocus();
                                                    return;
                                                }

                                            }
                                            if (snapshot1.getKey().equals("Username")) {
                                                String Username = username.getText().toString();
                                                if (!Username.isEmpty()) {
                                                    DatabaseReference ref = snapshot1.getRef();
                                                    ref.setValue(Username);
                                                } else {
                                                    username.setError("Field cannot be Empty");
                                                    username.requestFocus();
                                                    return;
                                                }

                                            }
                                            if (snapshot1.getKey().equals("Phone Number")) {
                                                String PhoneNo = phn.getText().toString();
                                                if(PhoneNo.matches(MobilePattern)){
                                                DatabaseReference ref = snapshot1.getRef();
                                                ref.setValue(PhoneNo);
                                                }
                                                else{
                                                    phn.setError("Enter valid Phone No. ");
                                                    phn.requestFocus();
                                                    return;
                                                }
                                            }
                                            if(snapshot1.getKey().equals("Name"))
                                            {
                                                String Name = name.getText().toString();
                                                if(!Name.isEmpty()) {
                                                    DatabaseReference ref = snapshot1.getRef();
                                                    ref.setValue(Name);
                                                }
                                                else{
                                                    name.setError("Field cannot be Empty");
                                                    name.requestFocus();
                                                    return;
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

        DeleteAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will result in completely removing your " +
                        "account from the app and you won't able to access the app!");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteUserData(docuid);
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

        return view;
    }

    private void deleteUserData(String docuid) {
        DatabaseReference deUsers = FirebaseDatabase.getInstance().getReference("Users").child(docuid);
        deUsers.removeValue();
    }

}
