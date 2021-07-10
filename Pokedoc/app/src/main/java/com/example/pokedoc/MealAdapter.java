package com.example.pokedoc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {

    private List<String> foodName;
    private List<String> foodQty;
    private List<String> hashList;
    private LayoutInflater layoutInflater;
    FirebaseAuth mAuth;

    public MealAdapter(Context context, List<String> foodName, List<String> foodQty, List<String> hashList){
        this.foodName=foodName;
        this.foodQty=foodQty;
        this.hashList = hashList;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.meal_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String foodList = foodName.get(position);
        String hash= hashList.get(position);
        ViewHolder.txtName.setText(foodList);
        String amtList = foodQty.get(position);
        ViewHolder.txtQty.setText(amtList);
        ViewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(view.getContext(), foodName.get(position), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle("Delete The Meal");
                dialog.setMessage("Deleting this meal will result in completely removing this " +
                        "meal from your food intake. Do you want to continue?");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        final String[] hash = {""};
//                        final DatabaseReference[] ref = {null};
                        DatabaseReference deUsers = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Nutrition").child(date).child(hash);
//                        deUsers.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                                for(DataSnapshot snapshot1: snapshot.getChildren())
//                                {
//                                    hash[0] =snapshot1.getKey();
//                                    for (DataSnapshot snapshot2: snapshot1.getChildren())
//                                    {
//                                        if(snapshot2.getKey().equals("Food Item"))
//                                        {
//                                            if(snapshot2.getValue(String.class).equals(foodList));
//
//                                            {
//                                                String reqhash = hash[0];
//                                                Toast.makeText(view.getContext(), reqhash, Toast.LENGTH_SHORT).show();
//                                                ref[0] =snapshot1.getRef();
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//                            }
//                        });


                        foodName.remove(position);
                        foodQty.remove(position);
                          deUsers.removeValue();
                          notifyItemRemoved(position);
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();;

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        static TextView txtName;
        static TextView txtQty;
        static View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName=itemView.findViewById(R.id.mealNameTxt);
            txtQty=itemView.findViewById(R.id.mealAmtTxt);
            mView=itemView;

        }
    }
}
