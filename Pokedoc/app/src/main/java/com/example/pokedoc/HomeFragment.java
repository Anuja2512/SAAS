package com.example.pokedoc;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference ref;
    Button addPatients;
    String docUid;
    LinearLayout mainlayout;
    TextView welcome;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home_doctor, container, false);
        int nightModeFlags = this.getActivity().getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                ConstraintLayout layout = (ConstraintLayout)view.findViewById(R.id.layout);
                // Resources layout =getResources();
                //  layout.(R.drawable.backgrounddark);
                layout.setBackgroundResource(R.drawable.backdark);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                ConstraintLayout layoutlight = (ConstraintLayout)view.findViewById(R.id.layout);
                layoutlight.setBackgroundResource(R.drawable.back);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                ConstraintLayout layoutd = (ConstraintLayout)view.findViewById(R.id.layout);
                // Resources layout =getResources();
                //  layout.(R.drawable.backgrounddark);
                layoutd.setBackgroundResource(R.drawable.backdark);
                break;
        }
        mainlayout=view.findViewById(R.id.mainlayout);
        addPatients=view.findViewById(R.id.button);
        addPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toaddpatient = new Intent(getActivity(), AddPatientActivity.class);
                startActivity(toaddpatient);
            }
        });
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        docUid=mUser.getUid();
        getPatient();

        return view;

    }

     void getPatient() {
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(docUid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        if (snapshot1.exists()) {
                            if (snapshot1.getKey().equals("Patients")) {
                                for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                    if (snapshot2.exists()) {
                                        for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                            if (snapshot3.exists()) {
                                                LinearLayout buttonlayout = new LinearLayout(getActivity());
                                                buttonlayout.setOrientation(LinearLayout.VERTICAL);
                                                Button userbutton = new Button(getActivity());
                                                String patusername=snapshot3.getValue(String.class);
                                                userbutton.setText(snapshot3.getValue(String.class));
                                                userbutton.setPadding(10, 20, 10, 20);
                                                userbutton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent=new Intent(getActivity(), PatientOverview.class);
                                                        intent.putExtra("uid", snapshot3.getKey());
                                                        startActivity(intent);
                                                    }
                                                });
                                                LinearLayout emptylayout = new LinearLayout(getActivity());
                                                emptylayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 20));
                                                emptylayout.setOrientation(LinearLayout.VERTICAL);
                                                mainlayout.addView(buttonlayout);
                                                buttonlayout.addView(userbutton);
                                                buttonlayout.addView(emptylayout);
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
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onRefresh() {
        
    }
}
