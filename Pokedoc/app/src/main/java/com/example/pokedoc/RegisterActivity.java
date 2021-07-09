package com.example.pokedoc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText name,email,password,username,phone,confirmPassword;
    Button submit,goLogin;
    private ProgressDialog loadingBar;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference UsersReference, reference;
    String currentUserID, role;
    RadioGroup radioGroup;
    RadioButton radiomale, radiofemale;
    ArrayList<String> UserNames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

        mAuth= FirebaseAuth.getInstance();
        role=getIntent().getStringExtra("role");
        submit=(Button)findViewById(R.id.RSubmitBtn);
        user=mAuth.getCurrentUser();
        radiofemale=findViewById(R.id.Female);
        radiomale=findViewById(R.id.Male);
        email=findViewById(R.id.REmailTxt);
        username=findViewById(R.id.RUsernameTxt);
        phone=findViewById(R.id.RPhoneTxt);
        password=findViewById(R.id.RPasswordTxt);
        confirmPassword=findViewById(R.id.RConfirmPassTxt);
        name=findViewById(R.id.RNameTxt);
        radioGroup=findViewById(R.id.raiogroup);
        loadingBar=new ProgressDialog(this);
        goLogin=findViewById(R.id.RtoLSwitchBtn);
        reference= FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        CollectUserNames((Map<String, Object>) snapshot1.getValue());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, "Error Fetching UserNames..", Toast.LENGTH_SHORT).show();
            }
        });
        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                i.putExtra("role", role);
                startActivity(i);
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail,pass,cpass,Username,phn, namee, gender;
                mail=email.getText().toString();
                pass=password.getText().toString();
                cpass=confirmPassword.getText().toString();
                Username=username.getText().toString();
                phn=phone.getText().toString();
                namee=name.getText().toString();
                int i=radioGroup.getCheckedRadioButtonId();
                if(radiomale.getId()==i) {
                    gender=radiomale.getText().toString();
                }
                else gender=radiofemale.getText().toString();
                registerNewUser(mail,pass,cpass,Username,phn, namee, gender, role);

            }
        });

    }
    private void storeUserOnFireBase(String userName,String email,String phone,String passwd,String UID, String name, String gender, String role){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        UsersReference=database.getReference().child("Users").child(UID);
        HashMap userMap=new HashMap();
        userMap.put("Username",userName);
        userMap.put("Email ID",email);
        userMap.put("Phone Number",phone);
        userMap.put("Password",passwd);
        userMap.put("Name", name);
        userMap.put("Gender", gender);
        userMap.put("Role", role);
        if(role.equals("patient"))
        {
            userMap.put("Doctors", "0");
        }
        else{userMap.put("Patients", "0");}
        UsersReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful())
                {

                }
                else {
                    Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }
    public void registerNewUser(String mail,String pswd,String cpass,String Username,String phn, String name, String gender, String role) {

        if (TextUtils.isEmpty(Username)) {
            username.setError("Please enter Username.");
            username.requestFocus();
            return;
        } else if (TextUtils.isEmpty(mail)) {
            email.setError("Please enter Email.");
            email.requestFocus();
            return;
        } else if (TextUtils.isEmpty(phn)) {
            phone.setError("Please enter Phone No.");
            phone.requestFocus();
            return;
        } else if (TextUtils.isEmpty(pswd)) {
            password.setError("Please enter Password.");
            password.requestFocus();
            return;
        } else if (!pswd.equals(cpass)) {
            confirmPassword.setError("Please confirm Password.");
            confirmPassword.requestFocus();
            return;
        } else if (UserNames.contains(Username)) {
            username.setError("UserName Already Used, Try another one");
            username.requestFocus();
            return;
        }

        loadingBar.setTitle("Create New Account");
        loadingBar.setMessage("Please wait, while we create your new account.");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);

        mAuth.createUserWithEmailAndPassword(mail, pswd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Verification link has been sent to " + mAuth.getCurrentUser().getEmail() + ", Please verify it.", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                            currentUserID = mAuth.getCurrentUser().getUid();
                            String mail, pass, usnm, phn;
                            mail = email.getText().toString();

                            usnm = username.getText().toString();
                            phn = phone.getText().toString();
                            pass = password.getText().toString();

                            storeUserOnFireBase(usnm, mail, phn, pass, currentUserID, name, gender, role);

                            user = mAuth.getCurrentUser();
                            user.sendEmailVerification();

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("role", role);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                        }
                    }
                });
    }
    private void CollectUserNames(Map<String, Object> Surveys) {
        for (Map.Entry<String, Object> entry : Surveys.entrySet()) {
            if (entry.getKey().toString().equals("Username"))
            {
                UserNames.add(entry.getValue().toString());
            }

        }
    }
}