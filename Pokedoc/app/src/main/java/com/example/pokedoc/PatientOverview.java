package com.example.pokedoc;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian3d;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.database.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

public class PatientOverview extends AppCompatActivity {
    Button prescbtn, myreports;
    ImageView deletepat;
    ImageView chat;
    String patuid;
    TextView gendertext, nametext, emailtext, numbertext, agen;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference usersReference, foodref;
    Button addmeal;
    Calendar calendar;
    AnyChartView chart;
    HashMap<String, Integer> map;
    List<String> keylist = new ArrayList();
    List<Integer> prolist, carblist, fatlist, sodiumlist, callist, sugarlist;
    Integer a=0, proteins=0, carbo=0, fa=0, prosum, fsum, carsum, sodsum, calosum, sugarsum;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_overview);
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
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        List<DataEntry> data = new ArrayList<>();
        chart=findViewById(R.id.any_chart_view);
        Cartesian3d pie = AnyChart.column3d();
        patuid=getIntent().getStringExtra("uid");
        gendertext=findViewById(R.id.emailn);
        emailtext=findViewById(R.id.namen);
        nametext=findViewById(R.id.usern);
        agen = findViewById(R.id.agen);
        numbertext=findViewById(R.id.numbern);
        prescbtn=findViewById(R.id.button6);
        chat=findViewById(R.id.imageView4);
        deletepat=findViewById(R.id.imageView5);
        myreports=findViewById(R.id.button11);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child(patuid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    if(snapshot1.getKey().equals("Username"))
                    {
                        String username = snapshot1.getValue(String.class);
                       chat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(PatientOverview.this, ChatActivity.class);
                                intent.putExtra("receiverUser",username);
                                startActivity(intent);
                            }
                        });
                    }
                    if(snapshot1.getKey().equals("Name"))
                    {
                        String name = snapshot1.getValue(String.class);
                        nametext.setText("Name: "+name);
                    }
                    if(snapshot1.getKey().equals("Gender"))
                    {
                        String gender = snapshot1.getValue(String.class);
                        gendertext.setText("Gender: "+gender);
                    }
                    if(snapshot1.getKey().equals("Phone Number"))
                    {
                        String phn = snapshot1.getValue(String.class);
                        numbertext.setText("Phone: "+phn);
                    }
                    if(snapshot1.getKey().equals("Email ID"))
                    {
                        String email = snapshot1.getValue(String.class);
                        emailtext.setText(email);
                    }
                    if(snapshot1.getKey().equals("Age"))
                    {
                        Long agee = snapshot1.getValue(Long.class);
                        String Age = agee.toString();
                        agen.setText("Age: "+Age);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        prescbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PatientOverview.this, AddPrescription.class);
                intent.putExtra("uid", patuid);
                startActivity(intent);
            }
        });
        prolist= new ArrayList<>(30);
        sodiumlist= new ArrayList<>(30);
        sugarlist= new ArrayList<>(30);
        callist= new ArrayList<>(30);
        carblist= new ArrayList<>(30);
        fatlist= new ArrayList<>(30);
        foodref = FirebaseDatabase.getInstance().getReference().child("Users").child(patuid).child("Nutrition");
        foodref.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    if(snapshot1.getKey().equals(date))
                    {
                        for(DataSnapshot snapshot2: snapshot1.getChildren())
                        {
                            for(DataSnapshot snapshot3: snapshot2.getChildren())
                            {
                                // map = new HashMap<>();

                                if(snapshot3.getKey().equals("Food Item"))
                                {
                                    String foodname = snapshot3.getValue(String.class);
                                }
                                if(snapshot3.getKey().equals("Quantity"))
                                {
                                    String quant = snapshot3.getValue(String.class);
                                    Integer quantity = Integer.parseInt(quant);

                                }
                                if(snapshot3.getKey().equals("Protein"))
                                {
                                    String protein = snapshot3.getValue(String.class);
                                    Double pro = Double.parseDouble(protein);
                                    Integer prot = pro.intValue();
                                    //map.put("Protien", prot);
                                    prolist.add(prot);
                                    prosum = prolist.stream().mapToInt(Integer::intValue).sum();
                                    // Toast.makeText(getActivity(), sum.toString(), Toast.LENGTH_SHORT).show();
                                    // Toast.makeText(getActivity(), valuelist.toString(), Toast.LENGTH_SHORT).show();


                                }
                                if(snapshot3.getKey().equals("Carbohydrates"))
                                {
                                    String carb = snapshot3.getValue(String.class);
                                    Double carbs = Double.parseDouble(carb);
                                    carblist.add(carbs.intValue());
                                    carsum = carblist.stream().mapToInt(Integer::intValue).sum();
                                    // map.put("Carbohydrates", carbs);
                                    //  Toast.makeText(getActivity(), map.get("Carbohydrates"), Toast.LENGTH_SHORT).show();
                                    // data.add(new ValueDataEntry("Carbohydrates", map.get("Carbohydrates")));

                                }
                                if(snapshot3.getKey().equals("Fats"))
                                {
                                    String fats = snapshot3.getValue(String.class);
                                    Double fat = Double.parseDouble(fats);
                                    fatlist.add(fat.intValue());
                                    fsum = fatlist.stream().mapToInt(Integer::intValue).sum();
                                }
                                if(snapshot3.getKey().equals("Sodium"))
                                {
                                    String fats = snapshot3.getValue(String.class);
                                    Double fat = Double.parseDouble(fats);
                                    sodiumlist.add(fat.intValue());
                                    sodsum = sodiumlist.stream().mapToInt(Integer::intValue).sum();
                                }
                                if(snapshot3.getKey().equals("Sugar"))
                                {
                                    String fats = snapshot3.getValue(String.class);
                                    Double fat = Double.parseDouble(fats);
                                    sugarlist.add(fat.intValue());
                                    sugarsum = sugarlist.stream().mapToInt(Integer::intValue).sum();
                                }
                                if(snapshot3.getKey().equals("Calories"))
                                {
                                    String fats = snapshot3.getValue(String.class);
                                    Double fat = Double.parseDouble(fats);
                                    callist.add(fat.intValue());
                                    calosum = callist.stream().mapToInt(Integer::intValue).sum();
                                }

                            }
                        }
                    }
                }
                data.add(new ValueDataEntry("Protein(g)", prosum));
                data.add(new ValueDataEntry("Fat(g)" , fsum));
                data.add(new ValueDataEntry("Carb(g)", carsum));
                data.add(new ValueDataEntry("Na(mg)", sodsum));
                data.add(new ValueDataEntry("Kcal", calosum));
                data.add(new ValueDataEntry("Sugar(g)", sugarsum));
                pie.data(data);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



        pie.title("Nutrition Chart");
        pie.labels().position("outside");
        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Data")
                .padding(0d, 0d, 10d, 0d);
        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
        pie.labels().fontColor("#111");
        pie.palette().items();
        chart.setChart(pie);
        chart.setBackgroundColor(Color.BLACK);
        pie.background().corners(6);


        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                pie.animation();
            }
        });
        deletepat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(PatientOverview.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setCancelable(true);
                alertDialog.setMessage("Do you wish to Delete this Patient?\n");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference reference3= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Patients");
                                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot snapshot1: snapshot.getChildren())
                                        {
                                            for(DataSnapshot snapshot2: snapshot1.getChildren())
                                            {
                                                if(snapshot2.getKey().equals(patuid))
                                                {
                                                    DatabaseReference reference2=snapshot2.getRef();
                                                    Toast.makeText(PatientOverview.this, reference2.getKey(), Toast.LENGTH_SHORT).show();
                                                    reference2.removeValue();

                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                DatabaseReference reference5= FirebaseDatabase.getInstance().getReference().child("Users").child(patuid).child("Doctors");
                                reference5.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot snapshot1: snapshot.getChildren())
                                        {
                                            for(DataSnapshot snapshot2: snapshot1.getChildren())
                                            {
                                                if(snapshot2.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                                {
                                                    DatabaseReference reference2=snapshot2.getRef();
                                                    Toast.makeText(PatientOverview.this, reference2.getKey(), Toast.LENGTH_SHORT).show();
                                                    reference2.removeValue();
                                                }
                                            }
                                        }
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
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


        });
        myreports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PatientOverview.this, ViewReportsDoctor.class);
                intent.putExtra("uid", patuid);
                startActivity(intent);
            }
        });
    }
}