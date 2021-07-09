package com.example.pokedoc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference ref;
    Button addPatients;
    SwipeRefreshLayout refreshLayout;
    String docUid;
    LinearLayout mainlayout;
    TextView welcome;

    boolean isActive;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home_doctor, container, false);
        welcome=view.findViewById(R.id.textView13);
        refreshLayout=view.findViewById(R.id.refresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainlayout.removeAllViews();
                getPatient();

                refreshLayout.setRefreshing(false);
            }
        });

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
                                                userbutton.setBackgroundColor(Color.parseColor("#AB2196F3"));
                                                userbutton.setTextColor(Color.parseColor("#FFFFFF"));
                                                String patusername = snapshot3.getValue(String.class);
                                                userbutton.setText(snapshot3.getValue(String.class));
                                                userbutton.setPadding(10, 20, 10, 20);
                                                userbutton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent(getActivity(), PatientOverview.class);
                                                        intent.putExtra("uid", snapshot3.getKey());
                                                        startActivity(intent);
                                                    }
                                                });
                                                LinearLayout emptylayout = new LinearLayout(getActivity());
                                                emptylayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 35));
                                                emptylayout.setOrientation(LinearLayout.VERTICAL);
                                                mainlayout.addView(buttonlayout);
                                                buttonlayout.addView(userbutton);
                                                buttonlayout.addView(emptylayout);
                                            }
                                        }

                                    }
                                }
                            }
                            if (snapshot1.getKey().equals("Username")) {
                                String username = snapshot1.getValue(String.class);
                                welcome.setText("Welcome " + username);
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
}
