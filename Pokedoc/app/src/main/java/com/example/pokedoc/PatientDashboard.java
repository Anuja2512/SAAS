package com.example.pokedoc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class PatientDashboard extends AppCompatActivity {

    boolean isPressed=false;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1, new HomePatientFragment()).commit();
        }
        int nightModeFlags = this.getApplicationContext().getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
                // Resources layout =getResources();
                //  layout.(R.drawable.backgrounddark);
                layout.setBackgroundResource(R.drawable.backdark);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                RelativeLayout layoutlight = (RelativeLayout) findViewById(R.id.layout);
                layoutlight.setBackgroundResource(R.drawable.back);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                RelativeLayout layoutd = (RelativeLayout) findViewById(R.id.layout);
                // Resources layout =getResources();
                //  layout.(R.drawable.backgrounddark);
                layoutd.setBackgroundResource(R.drawable.backdark);
                break;
        }

    }
     BottomNavigationView.OnNavigationItemSelectedListener navListner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.menuHome:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,new HomePatientFragment()).commit();
                    break;
                case R.id.menuAccount:

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,new MyPrescriptionsPatientFragment()).commit();

                    break;
                case R.id.menuFavourite:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,new MyDoctorsFragment()).commit();

                    break;
                case R.id.menuSettings:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,new SettingsPatientFragment()).commit();

                    break;
                case R.id.myreports:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,new MyReportsPatientFragment()).commit();
                    break;

            }
            return true;
        }
    };

    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage("Do you wish to Logout or Exit?")
                .setCancelable(false)

                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(PatientDashboard.this,RoleActivity.class));
                    }
                })
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(PatientDashboard.this,RoleActivity.class));
                    }
                })
                .show();
    }

}
