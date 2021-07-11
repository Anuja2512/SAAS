package com.example.pokedoc;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PrescriptionDetails extends AppCompatActivity {
    String patuid, medicine, dose, description;
    Button submit, addmed;
    EditText  desc, dosage;
    DatabaseReference reference, reference1, reference2, reference3;
    TextView med;
    HashMap<String, String> map= new HashMap<>();;
    ArrayList<String> meallist;
    Dialog dialog, helpdialog;
    String res;
    ImageView lunch, breakfast, dinner, beverage, plusicon;
    TextView txtlunch, txtbreakfast, txtdinner, txtbeverage;
    String[] urls=new String[4];
    ImageView search;
    Button addmeal;
    TextView mealtext;
    EditText editText, quantitytext;
    TextView helptext;
    HashMap<String, String> foodmap;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_details);
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

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
        patuid=getIntent().getStringExtra("uid");
        med = findViewById(R.id.medtext);
        dosage=findViewById(R.id.dosetext);
        desc=findViewById(R.id.destext);
        addmed=findViewById(R.id.button10);
        meallist=new ArrayList<>();
        med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=new Dialog(PrescriptionDetails.this);
                dialog.setContentView(R.layout.dialouge_searchable_spinner1);
                dialog.getWindow().setLayout(1200, 1400);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                editText= dialog.findViewById(R.id.edit_text);
                ImageView search=dialog.findViewById(R.id.imageView6);
                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadingBar=new ProgressDialog(PrescriptionDetails.this);
                        loadingBar.setMessage("Please wait, while we load your application.");
                        loadingBar.setCanceledOnTouchOutside(false);

                        if(editText.getText().toString().equals(""))
                        {
                            editText.setError("Enter a Medicine to search");
                        }
                        else
                        {
                            loadingBar.show();
                            meallist.clear();
                            String[] mealname = editText.getText().toString().split("\\s");
                            String urlmealname="";
                            for(int i=0; i<mealname.length; i++)
                            {
                                urlmealname=urlmealname+mealname[i]+"+";
                            }
                            String mealinurl = urlmealname.substring(0,urlmealname.length()-1);
                            OkHttpClient client = new OkHttpClient();

                            Request request = new Request.Builder()
                                    .url("https://api.fda.gov/drug/drugsfda.json?search=%22"+mealinurl+"%22&limit=3")
                                    .get()
                                    .addHeader("x-rapidapi-key", "7dffa79fbcmshfc9dab8e10e6160p1d2ba5jsn3f841bb2e8bb")
                                    .addHeader("x-rapidapi-host", "edamam-food-and-grocery-database.p.rapidapi.com")
                                    .build();
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Response response = client.newCall(request).execute();
                                        res = response.body().string();
                                        PrescriptionDetails.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                try {
                                                    JSONObject fullbody = new JSONObject(res);
                                                   // Toast.makeText(PrescriptionDetails.this, res, Toast.LENGTH_SHORT).show();
                                                    String full = fullbody.getString("results");
                                                    Toast.makeText(PrescriptionDetails.this, full, Toast.LENGTH_SHORT).show();
                                                    JSONArray resultarray = new JSONArray(full);
                                                    JSONObject eachresult=null;
                                                    JSONArray eacnresularray=null;
                                                    JSONObject eachdrug=null;
                                                    for(int i=0; i<resultarray.length(); i++)
                                                    {
                                                        eachresult= resultarray.getJSONObject(i);
                                                        String prduct = eachresult.getString("products");
                                                        eacnresularray=new JSONArray(prduct);
                                                        for(int j=0; j<eacnresularray.length(); j++)
                                                        {
                                                            eachdrug=eacnresularray.getJSONObject(j);
                                                            String brandname = eachdrug.getString("brand_name");
                                                            meallist.add(brandname);


                                                        }

                                                        Toast.makeText(PrescriptionDetails.this, prduct, Toast.LENGTH_SHORT).show();


                                                    }

                                                    ListView listView=dialog.findViewById(R.id.listview);
                                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(PrescriptionDetails.this, android.R.layout.simple_list_item_1, meallist);
                                                    listView.setAdapter(adapter);
                                                    editText.addTextChangedListener(new TextWatcher() {
                                                        @Override
                                                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                        }

                                                        @Override
                                                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                            adapter.getFilter().filter(charSequence);
                                                        }

                                                        @Override
                                                        public void afterTextChanged(Editable editable) {

                                                        }
                                                    });
                                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                            med.setText(adapter.getItem(i));
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    loadingBar.dismiss();

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            });
                            thread.start();
                        }
                    }
                });

            }
        });
        addmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if((med.getText().toString().equals(""))||(med.getText().toString().equals("Select Medicine")))
                    {
                        med.requestFocus();
                        med.setError("Enter name of the Medicine");
                    }
                    else if(dosage.getText().toString().equals(""))
                    {
                        dosage.requestFocus();
                        dosage.setError("Enter dosage");
                    }
                    else
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(PrescriptionDetails.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setCancelable(true);
                        alertDialog.setMessage("Medicine Details: \nName: "+med.getText().toString()+"\nDosage: "+dosage.getText().toString()+"\nDescription: "+desc.getText().toString()+"\nDo you wish to submit?\n");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        reference=FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Prescriptions").child(patuid);
                                        reference1=FirebaseDatabase.getInstance().getReference().child("Users").child(patuid).child("Prescriptions").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                        medicine=med.getText().toString();
                                        dose=dosage.getText().toString();
                                        description=desc.getText().toString();
                                        reference2=FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot snapshot1: snapshot.getChildren())
                                                {
                                                    if(snapshot1.getKey().equals("Username"))
                                                    {
                                                        String docusername = snapshot1.getValue(String.class);
                                                        map.put("Doctor", docusername);
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        reference3=FirebaseDatabase.getInstance().getReference().child("Users").child(patuid);
                                        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                                for(DataSnapshot snapshot1: snapshot.getChildren())
                                                {
                                                    if(snapshot1.getKey().equals("Username"))
                                                    {
                                                        String patusername = snapshot1.getValue(String.class);
                                                        map.put("Patient", patusername);
                                                      //  Toast.makeText(PrescriptionDetails.this, snapshot1.getValue(String.class), Toast.LENGTH_SHORT).show();
                                                        map.put("Medicine", medicine);
                                                        map.put("Dosage", dose);
                                                        map.put("Description", description);
                                                        map.put("Date", date);
                                                        map.put("Reminder", "Not Set");
                                                    }

                                                }
                                                reference.push().setValue(map);
                                                reference1.push().setValue(map);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        med.setText("");
                                        dosage.setText("");
                                        desc.setText("");
                                        Toast.makeText(PrescriptionDetails.this, "Medicine added Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }

            }
        });

    }
}