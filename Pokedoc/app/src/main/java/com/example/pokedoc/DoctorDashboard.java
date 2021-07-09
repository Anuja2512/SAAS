package com.example.pokedoc;

import android.content.res.Configuration;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class DoctorDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private static final int RESULT_CODE = 100;
    private DrawerLayout mNavDrawer;
    String uid, name;
    TextView nametxt;
    Button button;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference usersReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        NavigationView navigationView=findViewById(R.id.navigation_view);


        navigationView.setNavigationItemSelectedListener(this);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNavDrawer=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,mNavDrawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close
        );
        mNavDrawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.HomeTab);
        }

        uid = getIntent().getStringExtra("uid");

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
        usersReference= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        if (snapshot1.getKey().toString().equals("Name")) {
                            name = snapshot1.getValue().toString();
                         //  nametxt.setText("Welcome "+ name);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DoctorDashboard.this, "User not found, please try again or check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        if(mNavDrawer.isDrawerOpen(GravityCompat.START)){
            mNavDrawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.HomeTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                break;
            case R.id.ProfileTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DoctorSettingsFragment()).commit();

                break;
            case R.id.ContactUs:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ContactUsFragment()).commit();

                break;

            case R.id.SettingsTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DoctorSettingsFragment()).commit();

                break;
            case R.id.LogoutTab:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
        }
        mNavDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

