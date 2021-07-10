package com.example.pokedoc;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian3d;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column3d;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.database.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

public class HomePatientFragment extends Fragment{
    String uid, name;
    TextView nametxt;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference usersReference, foodref;
    Button addmeal;
    Calendar calendar;
    SwipeRefreshLayout refreshLayout;
    AnyChartView chart;
    RecyclerView meals;
    List<String> mealName;
    List<String> mealAmt;
    List<String> hashlist;
    HashMap<String, Integer> map;
    List<String> keylist = new ArrayList();
    List<Integer> prolist, carblist, fatlist, sodiumlist, callist, sugarlist;
    Integer a=0, proteins=0, carbo=0, fa=0, prosum, fsum, carsum, sodsum, calosum, sugarsum;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_pat_home, container, false);
        chart=rootview.findViewById(R.id.any_chart_view);
        Long today = MaterialDatePicker.todayInUtcMilliseconds();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        List<DataEntry> data = new ArrayList<>();

         Cartesian3d pie = AnyChart.column3d();
        calendar=Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(today);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        uid=user.getUid();
        addmeal=(Button)rootview.findViewById(R.id.button8);
        meals=rootview.findViewById(R.id.MealRecycler);
        nametxt=rootview.findViewById(R.id.textView5);
        usersReference= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        if (snapshot1.getKey().toString().equals("Name")) {
                            name = snapshot1.getValue().toString();
                            nametxt.setText(name);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        addmeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1=new Intent(getActivity(), AddMeal.class);
                startActivity(intent1);
            }
        });

        prolist= new ArrayList<>(30);
        mealName = new ArrayList<>();
        mealAmt = new ArrayList<>();
        hashlist = new ArrayList<>();
        sodiumlist= new ArrayList<>(30);
        sugarlist= new ArrayList<>(30);
        callist= new ArrayList<>(30);
        carblist= new ArrayList<>(30);
        fatlist= new ArrayList<>(30);
        foodref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Nutrition");
        foodref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    if(snapshot1.getKey().equals(date))
                    {
                        for(DataSnapshot snapshot2: snapshot1.getChildren())
                        {
                            String hash = snapshot2.getKey();
                          //  Toast.makeText(getActivity(), hash, Toast.LENGTH_SHORT).show();
                            hashlist.add(hash);
                            for(DataSnapshot snapshot3: snapshot2.getChildren())
                            {
                               // map = new HashMap<>();

                                if(snapshot3.getKey().equals("Food Item"))
                                {
                                    String foodname = snapshot3.getValue(String.class);
                                    mealName.add(foodname);


                                }
                                if(snapshot3.getKey().equals("Quantity"))
                                {
                                    String quant = snapshot3.getValue(String.class);
                                    Integer quantity = Integer.parseInt(quant);
                                    mealAmt.add(quant);


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

                meals.setLayoutManager(new LinearLayoutManager(getActivity()));
                MealAdapter mealAdapter=new MealAdapter(getActivity(),mealName,mealAmt,hashlist);
                meals.setAdapter(mealAdapter);
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
        pie.labels().adjustFontSize();
        pie.isFullScreenAvailable();
        pie.fullScreen();
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


        return rootview;
    }

}
