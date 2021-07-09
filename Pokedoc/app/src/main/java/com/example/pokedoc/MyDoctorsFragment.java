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
import org.jetbrains.annotations.NotNull;

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
        temp = new ArrayList<>();
        readDoctors();

        int nightModeFlags = this.getActivity().getResources().getConfiguration().uiMode &
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
        }
        return rootview;
    }

    private void readDoctors() {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).child("Doctors");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mDoctors.clear();
                mDoctorNames.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    for(DataSnapshot snapshot2: snapshot1.getChildren())
                    {
                        String doctoruid = snapshot2.getKey();
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(doctoruid);
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for(DataSnapshot snapshot3: snapshot.getChildren())
                                {
                                    if(snapshot3.getKey().equals("Name"))
                                    {
                                       String docname = snapshot3.getValue().toString();
                                        temp.add(docname);
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });

                        Doctor doctor= new Doctor(snapshot2.getValue(String.class), temp.toString());
                      mDoctors.add(doctor);


                       mDoctorNames.add(doctor);
                    }
                }

                myDocAdapter = new MyDocAdapter(getContext(),mDoctors,mDoctorNames);
                recyclerView.setAdapter(myDocAdapter);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
