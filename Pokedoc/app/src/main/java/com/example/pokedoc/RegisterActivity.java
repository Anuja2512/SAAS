package com.example.pokedoc;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText name,email,password,username,phone,confirmPassword;
    Button submit,goLogin, datebtn;
    private DatePickerDialog DatePickerDialog;
    private ProgressDialog loadingBar;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference UsersReference, reference;
    String currentUserID, role;
    int Age;
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
        datebtn = findViewById(R.id.dataPickerbtn);
        radioGroup=findViewById(R.id.raiogroup);
        loadingBar=new ProgressDialog(this);
        goLogin=findViewById(R.id.RtoLSwitchBtn);
        reference= FirebaseDatabase.getInstance().getReference().child("Users");
        datebtn.setText(getTodaysDate());
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
        /////////////

        initDatePicker();
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
                int age;
                mail=email.getText().toString();
                pass=password.getText().toString();
                cpass=confirmPassword.getText().toString();
                Username=username.getText().toString();
                phn=phone.getText().toString();
                namee=name.getText().toString();
                age = Age;
                int i=radioGroup.getCheckedRadioButtonId();
                if(radiomale.getId()==i) {
                    gender=radiomale.getText().toString();
                }
                else gender=radiofemale.getText().toString();
                registerNewUser(mail,pass,cpass,Username,phn, namee, gender,age, role);

            }
        });

    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return (makeDateString(day,month,year));
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;
                String date = makeDateString(dayOfMonth,month,year);
                datebtn.setText(date);
                Age = calculateAge(year);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        DatePickerDialog = new DatePickerDialog(this, style, dateSetListener,year,month,day);

    }

    private int calculateAge(int y){
        Calendar cal = Calendar.getInstance();
        int cyear = cal.get(Calendar.YEAR);
        int age = (cyear-y);
        return age;
    }


    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month)+" " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        return "JAN";
    }

    public void opendatepicker(View view) {
        DatePickerDialog.show();
    }

    private void storeUserOnFireBase(String userName,String email,String phone,String passwd,String UID, String name, String gender,int age, String role){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        UsersReference=database.getReference().child("Users").child(UID);
        HashMap userMap=new HashMap();
        userMap.put("Username",userName);
        userMap.put("Email ID",email);
        userMap.put("Phone Number",phone);
        userMap.put("Password",passwd);
        userMap.put("Name", name);
        userMap.put("Gender", gender);
        userMap.put("Age", age);
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
    public void registerNewUser(String mail, String pswd, String cpass, String Username, String phn, String name, String gender, int age, String role) {
        String MobilePattern = "[0-9]{10}";
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
        } else if (!phn.matches(MobilePattern)){
            phone.setError("Please enter valid Phone No. ");
            phone.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(pswd)) {
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

                            storeUserOnFireBase(usnm, mail, phn, pass, currentUserID, name, gender, age, role);

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