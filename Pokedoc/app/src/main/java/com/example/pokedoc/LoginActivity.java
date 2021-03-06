package com.example.pokedoc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.*;

public class LoginActivity extends AppCompatActivity {
    Button newAcc;
    EditText email,password;
    TextView changerole;
    Button submit,forgotPass,googleSignInButton;
    String role;
    private ProgressDialog loadingBar;
    DatabaseReference usersReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleSignInClient;
    private static final String TAG = "LoginActivity";
    public static String PREFS_NAME="MyPresFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        role = getIntent().getStringExtra("role");
        changerole=findViewById(R.id.textView6);
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
        newAcc=findViewById(R.id.newAccount);
        newAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                intent.putExtra("role", role);
                startActivity(intent);
                finish();
            }
        });

        mAuth= FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        submit=findViewById(R.id.LoginBtn);
        email=findViewById(R.id.LemailTxt);
        password=findViewById(R.id.LPasswordTxt);
        forgotPass=findViewById(R.id.ForgotPassBtn);
        loadingBar=new ProgressDialog(this);
        googleSignInButton=findViewById(R.id.LGoogle);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
                    {
                        Toast.makeText(LoginActivity.this, "Connection to Google Sign is failed...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Fusnm;
                Fusnm=email.getText().toString();
                sendForgotPassLink(Fusnm);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Lusnm,Lpass;
                Lusnm=email.getText().toString();
                Lpass=password.getText().toString();
                Login(Lusnm,Lpass);
            }
        });
    }
    private void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN)
        {
            loadingBar.setMessage("Please wait, while we load your application.");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            }
            else
            {
                Toast.makeText(this, "Can't get Authentication result.", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Log.d(TAG, "signInWithCredential:success");
                            usersReference= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot)
                                {
                                    if(snapshot.exists()) {
                                        if(role.equals("patient")) {

                                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                if (snapshot1.getKey().toString().equals("Role") && snapshot1.getValue().equals("patient")) {
                                                    Intent intent = new Intent(LoginActivity.this, PatientDashboard.class);
                                                    intent.putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                    startActivity(intent);
                                                    SharedPreferences sharedPreferences=getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                                    editor.putBoolean("hasLoggedIn", true);
                                                    editor.commit();
                                                    finish();
                                                    loadingBar.dismiss();
                                                }
                                                else  if (snapshot1.getKey().toString().equals("Role") && snapshot1.getValue().equals("doctor"))
                                                {
                                                    Toast.makeText(LoginActivity.this, "You are not registered as a Patient, please change role or re-register", Toast.LENGTH_SHORT).show();
                                                    changerole.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }
                                        else
                                        {
                                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                if (snapshot1.getKey().toString().equals("Role") && snapshot1.getValue().equals("doctor"))
                                                {

                                                    Intent intent = new Intent(LoginActivity.this, DoctorDashboard.class);
                                                    intent.putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                    startActivity(intent);
                                                    SharedPreferences sharedPreferences=getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                                    editor.putBoolean("hasLoggedIn", true);
                                                    editor.commit();
                                                    finish();
                                                    loadingBar.dismiss();
                                                }
                                                else  if (snapshot1.getKey().toString().equals("Role") && snapshot1.getValue().equals("patient"))
                                                {
                                                    Toast.makeText(LoginActivity.this, "You are not registered as a Patient, please change role or re-register", Toast.LENGTH_SHORT).show();
                                                    changerole.setVisibility(View.VISIBLE);
                                                }

                                            }
                                        }


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(LoginActivity.this, "Login unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        else
                        {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            String message = task.getException().toString();
                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                            intent.putExtra("role", role);
                            startActivity(intent);
                            finish();
                            Toast.makeText(LoginActivity.this, "Not Authenticated : " + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
    }



    public void Login(String Lusnm, final String Lpass)
    {
        loadingBar.setMessage("Please wait, while we load your application.");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        if(TextUtils.isEmpty(Lusnm))
        {
            email.setError("Please enter Email.");
            email.requestFocus();
            loadingBar.dismiss();
            return;
        }
        else if(TextUtils.isEmpty(Lpass)){
            password.setError("Please enter Password.");
            password.requestFocus();
            loadingBar.dismiss();
            return;
        }
        mAuth.signInWithEmailAndPassword(Lusnm,Lpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    if (mAuth.getCurrentUser().isEmailVerified()) {
                        loadingBar.dismiss();
                        usersReference= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                if(snapshot.exists()) {
                                    if(role.equals("patient")) {

                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            if (snapshot1.getKey().toString().equals("Role") && snapshot1.getValue().equals("patient")) {
                                                Intent intent = new Intent(LoginActivity.this, PatientDashboard.class);
                                                intent.putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                startActivity(intent);
                                                SharedPreferences sharedPreferences=getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                                editor.putBoolean("hasLoggedIn", true);
                                                editor.commit();
                                                finish();
                                                loadingBar.dismiss();
                                            }
                                            else  if (snapshot1.getKey().toString().equals("Role") && snapshot1.getValue().equals("doctor"))
                                            {
                                                Toast.makeText(LoginActivity.this, "You are not registered as a Patient, please change role or re-register", Toast.LENGTH_SHORT).show();
                                                changerole.setVisibility(View.VISIBLE);
                                                changerole.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent torolepage = new Intent(LoginActivity.this, RoleActivity.class);
                                                        startActivity(torolepage);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                    else
                                    {
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            if (snapshot1.getKey().toString().equals("Role") && snapshot1.getValue().equals("doctor"))
                                            {

                                                Intent intent = new Intent(LoginActivity.this, DoctorDashboard.class);
                                                intent.putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                startActivity(intent);
                                                SharedPreferences sharedPreferences=getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                                editor.putBoolean("hasLoggedIn", true);
                                                editor.commit();
                                                finish();
                                                loadingBar.dismiss();
                                            }
                                            else  if (snapshot1.getKey().toString().equals("Role") && snapshot1.getValue().equals("patient"))
                                            {
                                                Toast.makeText(LoginActivity.this, "You are not registered as a Patient, please change role or re-register", Toast.LENGTH_SHORT).show();
                                                changerole.setVisibility(View.VISIBLE);
                                                changerole.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent torolepage = new Intent(LoginActivity.this, RoleActivity.class);
                                                        startActivity(torolepage);
                                                    }
                                                });
                                            }

                                        }
                                    }



                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(LoginActivity.this, "Login unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "Please Verify email....." + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    return;
                }
            }
        });
    }
    public void sendForgotPassLink(String emailId){
        if(!TextUtils.isEmpty(emailId)){
            mAuth.sendPasswordResetEmail(emailId).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Reset link sent to"+email.getText().toString(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            email.setError("Please enter Email.");
            email.requestFocus();
        }
    }

}



