package com.example.pokedoc;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;


import java.util.ArrayList;
import java.util.List;

public class MyDoctorsFragment extends Fragment{

    private RecyclerView recyclerView;
    private MyDocAdapter myDocAdapter;
    private List<Doctor> mDoctors;
    private List<Doctor> mDoctorNames;
    private List<String> temp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_mydoctors, container, false);
        recyclerView = rootview.findViewById(R.id.DoctorRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDoctors=new ArrayList<>();
        mDoctorNames=new ArrayList<>();
        readDoctors();
        return rootview;
    }

    private void readDoctors() {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("Doctors");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDoctors.clear();
                mDoctorNames.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    for(DataSnapshot snapshot2: snapshot1.getChildren())
                    {
                        String doctoruid = snapshot2.getKey();
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(doctoruid);
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String docnamee="";
                                for(DataSnapshot snapshot3: snapshot.getChildren())
                                {
                                    temp = new ArrayList<>();
                                    if(snapshot3.getKey().equals("Name"))
                                    {

                                        Doctor doctor= new Doctor(snapshot2.getValue(String.class),snapshot3.getValue(String.class) );
                                        String docname = snapshot3.getValue().toString();

                                        temp.add( docname);
                                        mDoctorNames.add(doctor);
                                        docnamee = temp.toString();
                                    }


                                }
                              //  Toast.makeText(getActivity(), docnamee, Toast.LENGTH_SHORT).show();
                                Doctor doctor= new Doctor(snapshot2.getValue(String.class), docnamee);
                                mDoctors.add(doctor);

                                myDocAdapter = new MyDocAdapter(getContext(),mDoctors,mDoctorNames);
                              //  Toast.makeText(getActivity(), snapshot2.getValue(String.class), Toast.LENGTH_SHORT).show();
                                recyclerView.setAdapter(myDocAdapter);
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
