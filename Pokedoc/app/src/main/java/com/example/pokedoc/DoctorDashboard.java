package com.example.pokedoc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import android.widget.ImageView;
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
    String currentUserID;
    private DatabaseReference UsersRef;
    private ImageView NavProfileImg;
    private TextView NavUsername;
    private TextView NavEmail;
    DatabaseReference usersReference;
    Boolean isPressed=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        UsersRef=FirebaseDatabase.getInstance().getReference().child("Users");

        NavigationView navigationView=findViewById(R.id.navigation_view);
        View navView=navigationView.inflateHeaderView(R.layout.header);

        NavProfileImg=(ImageView)navView.findViewById(R.id.nav_profileimg);
        NavUsername=(TextView)navView.findViewById(R.id.nav_profileusnm);
        NavEmail=(TextView)navView.findViewById(R.id.nav_profilemail);

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("Username")) {
                        String username = dataSnapshot.child("Username").getValue().toString();
                        NavUsername.setText(username);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Your Username was not set up successfully",Toast.LENGTH_LONG).show();
                    }
                    if (dataSnapshot.hasChild("Email ID")) {
                        String mail = dataSnapshot.child("Email ID").getValue().toString();
                        NavEmail.setText(mail);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Your mail was not set up successfully",Toast.LENGTH_LONG).show();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
        usersReference= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
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

    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage("Do you wish to Logout or Exit?")
                .setCancelable(false)

                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // mAuth.signOut();
                      //  finish();
                      //  startActivity(new Intent(DoctorDashboard.this,RoleActivity.class));

                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                })
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        SharedPreferences sharedPreferences=getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putBoolean("hasLoggedIn", false);
                        editor.commit();
                        finish();
                        startActivity(new Intent(DoctorDashboard.this,RoleActivity.class));
                    }
                })
                .show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.HomeTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                break;
            case R.id.ContactUs:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ContactUsFragment()).commit();

                break;

            case R.id.SettingsTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DoctorSettingsFragment()).commit();

                break;
            case R.id.LogoutTab:
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences=getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("hasLoggedIn", false);
                editor.commit();
                finish();
                break;
        }
        mNavDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

