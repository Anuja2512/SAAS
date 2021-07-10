package com.example.pokedoc;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddMeal extends AppCompatActivity {
    TextView  textView;
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
        setContentView(R.layout.activity_add_meal);
        quantitytext=findViewById(R.id.editTextNumber);
        helptext=findViewById(R.id.textView16);
        addmeal=findViewById(R.id.button12);
        lunch=findViewById(R.id.lunch);
        breakfast=findViewById(R.id.breakfast);
        dinner=findViewById(R.id.dinner);
        beverage=findViewById(R.id.beverage);
        txtlunch=findViewById(R.id.txtlunch);
        txtbeverage=findViewById(R.id.txtbreakfast);
        txtdinner=findViewById(R.id.txtdinner);
        txtbreakfast=findViewById(R.id.txtbreakfast);

        urls[0]="https://en.wikipedia.org/wiki/Lunch";
        urls[1]="https://en.wikipedia.org/wiki/Breakfast";
        urls[2]="https://en.wikipedia.org/wiki/Dinner";
        urls[3]="https://en.wikipedia.org/wiki/Drink";

        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddMeal.this, FoodWiki.class);
                intent.putExtra("links",urls[0]);
                startActivity(intent);
            }
        });

        txtlunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddMeal.this, FoodWiki.class);
                intent.putExtra("links",urls[0]);
                startActivity(intent);
            }
        });

        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddMeal.this, FoodWiki.class);
                intent.putExtra("links",urls[1]);
                startActivity(intent);
            }
        });

        txtbreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddMeal.this, FoodWiki.class);
                intent.putExtra("links",urls[1]);
                startActivity(intent);
            }
        });

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddMeal.this, FoodWiki.class);
                intent.putExtra("links",urls[2]);
                startActivity(intent);
            }
        });

        txtdinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddMeal.this, FoodWiki.class);
                intent.putExtra("links",urls[2]);
                startActivity(intent);
            }
        });

        beverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddMeal.this, FoodWiki.class);
                intent.putExtra("links",urls[3]);
                startActivity(intent);
            }
        });

        txtbeverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddMeal.this, FoodWiki.class);
                intent.putExtra("links",urls[3]);
                startActivity(intent);
            }
        });

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
        helptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=new Dialog(AddMeal.this);
                dialog.setContentView(R.layout.help_dialogue);
                dialog.getWindow().setLayout(1000, 1200);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        Long today = MaterialDatePicker.todayInUtcMilliseconds();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        mealtext=findViewById(R.id.textView30);
        foodmap=new HashMap<>();
        meallist=new ArrayList<>();
        mealtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=new Dialog(AddMeal.this);
                dialog.setContentView(R.layout.dialouge_searchable_spinner1);
                dialog.getWindow().setLayout(1200, 1400);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                 editText= dialog.findViewById(R.id.edit_text);
                ImageView search=dialog.findViewById(R.id.imageView6);
                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadingBar=new ProgressDialog(AddMeal.this);
                        loadingBar.setMessage("Please wait, while we load your application.");
                        loadingBar.setCanceledOnTouchOutside(false);

                        if(editText.getText().toString().equals(""))
                        {
                            editText.setError("Enter a Meal to search");
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
                                    .url("https://api.nal.usda.gov/fdc/v1/foods/search?query="+mealinurl+"&api_key=fuvMsy9eT2THamJRumfXOTR9f6m2oBoTKU7YDj65&pageSize=20")
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
                                        AddMeal.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                try {
                                                    JSONObject fullbody = new JSONObject(res);
                                                    String full = fullbody.getString("foods");
                                                    JSONObject eachfood=null;
                                                    String eachfoodstring="";
                                                    JSONArray foodsarray = new JSONArray(full);
                                                    for(int i=0; i<foodsarray.length(); i++)
                                                    {
                                                        eachfood= foodsarray.getJSONObject(i);
                                                        eachfoodstring=eachfoodstring+eachfood.toString();
                                                        String description = eachfood.getString("description");
                                                        String foodids = eachfood.getString("fdcId");
                                                        if(!meallist.contains(description))
                                                        {
                                                            meallist.add(description);
                                                            foodmap.put(description, foodids);
                                                        }

                                                    }

                                                    ListView listView=dialog.findViewById(R.id.listview);
                                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddMeal.this, android.R.layout.simple_list_item_1, meallist);
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
                                                            mealtext.setText(adapter.getItem(i));
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    loadingBar.dismiss();

                                                } catch (JSONException e) {
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
        addmeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantitytext.getText().toString().equals(""))
                {
                    quantitytext.setError("Enter an amount!");
                }
                else if(quantitytext.getText().toString().equals("0"))
                {
                    quantitytext.setError("Enter an amount!");
                }
                else if(mealtext.getText().equals("")||mealtext.getText().equals("Select Meal Item"))
                {
                    mealtext.setError("Enter a meal item!");
                }
                else
                {
                    loadingBar.show();
                    Integer amount=Integer.parseInt(quantitytext.getText().toString());
                    String desc = mealtext.getText().toString();
                    String id = foodmap.get(desc);
                    DatabaseReference foodref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Nutrition").child(date);
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url("https://api.nal.usda.gov/fdc/v1/food/"+id+"?&api_key=fuvMsy9eT2THamJRumfXOTR9f6m2oBoTKU7YDj65")
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
                                AddMeal.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {
                                            JSONObject eachfood= new JSONObject(res);
                                            String description = eachfood.getString("description");
                                            String foodids = eachfood.getString("fdcId");
                                            String foodclass = eachfood.getString("foodClass");
                                          //  Toast.makeText(AddMeal.this, foodclass, Toast.LENGTH_SHORT).show();
                                            if(foodclass.equals("Branded"))
                                            {
                                                JSONObject allnutrients = new JSONObject(eachfood.getString("labelNutrients"));
                                                String fats = allnutrients.getString("fat");
                                                JSONObject fatsobj = new JSONObject(fats);
                                                Double fat = fatsobj.getDouble("value")*amount;
                                                String sodium = allnutrients.getString("sodium");
                                                JSONObject sodiumobj = new JSONObject(sodium);
                                                Double sodiumvalue = sodiumobj.getDouble("value")*amount;
                                                String carbohydrates = allnutrients.getString("carbohydrates");
                                                JSONObject carbohydratesobj = new JSONObject(carbohydrates);
                                                Double carbohydratesvalue = carbohydratesobj.getDouble("value")*amount;
                                                String sugars = allnutrients.getString("sugars");
                                                JSONObject sugarssobj = new JSONObject(sugars);
                                                Double sugarsvalue = sugarssobj.getDouble("value")*amount;
                                                String protein = allnutrients.getString("protein");
                                                JSONObject proteinsobj = new JSONObject(protein);
                                                Double proteinvalue = proteinsobj.getDouble("value")*amount;
                                                String calories = allnutrients.getString("calories");
                                                JSONObject caloriesobj = new JSONObject(calories);
                                                Double caloriesvalue = caloriesobj.getDouble("value")*amount;
                                                HashMap<String , String> fooddbmap = new HashMap<>();
                                                fooddbmap.put("Fats", fat.toString());
                                                fooddbmap.put("Sodium", (sodiumvalue).toString());
                                                fooddbmap.put("Carbohydrates", carbohydratesvalue.toString());
                                                fooddbmap.put("Sugar", sugarsvalue.toString());
                                                fooddbmap.put("Protein", proteinvalue.toString());
                                                fooddbmap.put("Calories", caloriesvalue.toString());
                                                fooddbmap.put("Food Item", description);
                                                fooddbmap.put("Quantity", amount.toString());
                                                fooddbmap.put("FCDID", foodids);
                                                foodref.push().setValue(fooddbmap);
                                                loadingBar.dismiss();
                                                Toast.makeText(AddMeal.this, "Meal added successfully", Toast.LENGTH_SHORT).show();
                                                Intent tohome=new Intent(AddMeal.this, PatientDashboard.class);
                                                startActivity(tohome);
                                                finish();
                                            }
                                            if(foodclass.equals("Survey"))
                                            {
                                                JSONArray nutrientarray = new JSONArray(eachfood.getString("foodNutrients"));
                                                HashMap<String , String> fooddbmap = new HashMap<>();
                                                for(int i=0; i<nutrientarray.length(); i++)
                                                {
                                                    JSONObject allnutrient = nutrientarray.getJSONObject(i);
                                                    JSONObject eachnutrient = new JSONObject(allnutrient.get("nutrient").toString());
                                                    String nutrient = eachnutrient.getString("name");


                                                    if(nutrient.equals("Sodium, Na"))
                                                    {
                                                        Double sodvalue = allnutrient.getDouble("amount")*amount;
                                                        fooddbmap.put("Sodium", sodvalue.toString());
                                                    }
                                                    if(nutrient.equals("Sugars, total including NLEA"))
                                                    {
                                                        Double sodvalue = allnutrient.getDouble("amount")*amount;
                                                        fooddbmap.put("Sugar", sodvalue.toString());
                                                    }
                                                    if(nutrient.equals("Total lipid (fat)"))
                                                    {
                                                        Double sodvalue = allnutrient.getDouble("amount")*amount;
                                                        fooddbmap.put("Fats", sodvalue.toString());
                                                    }
                                                    if(nutrient.equals("Carbohydrate, by difference"))
                                                    {
                                                        Double sodvalue = allnutrient.getDouble("amount")*amount;
                                                        fooddbmap.put("Carbohydrates", sodvalue.toString());
                                                    }
                                                    if(nutrient.equals("Energy"))
                                                    {
                                                        Double sodvalue = allnutrient.getDouble("amount")*amount;
                                                        fooddbmap.put("Calories", sodvalue.toString());
                                                    }
                                                    if(nutrient.equals("Protein"))
                                                    {
                                                        Double sodvalue = allnutrient.getDouble("amount")*amount;
                                                        fooddbmap.put("Protein", sodvalue.toString());
                                                    }
                                                    fooddbmap.put("Food Item", description);
                                                    fooddbmap.put("Quantity", amount.toString());
                                                    fooddbmap.put("FCDID", foodids);

                                                    loadingBar.dismiss();
                                                }
                                                foodref.push().setValue(fooddbmap);
                                                Toast.makeText(AddMeal.this, "Meal added successfully", Toast.LENGTH_SHORT).show();
                                                Intent tohome=new Intent(AddMeal.this, PatientDashboard.class);
                                                startActivity(tohome);
                                                finish();


                                            }
                                            else
                                            {
                                                Toast.makeText(AddMeal.this, "An error occurred! Please try to be specific about the food item!", Toast.LENGTH_SHORT).show();
                                            }



                                        } catch (JSONException e) {
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

    public void onBackPressed(){
        super.onBackPressed();
    }
}