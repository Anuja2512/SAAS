package com.example.pokedoc;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.View;
import java.util.List;

public class MyDocAdapter extends RecyclerView.Adapter<MyDocAdapter.ViewHolder> {

    private Context context;
    private List<Doctor> doctors;
    private List<Doctor> usernames;

    public MyDocAdapter(Context context,List <Doctor> usernames,List<Doctor> doctors){
        this.context=context;
        this.doctors=doctors;
        this.usernames=usernames;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.my_doc_item,parent,false);
        return new MyDocAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doctor doctorsList=usernames.get(position);
        Doctor doctorNames=doctors.get(position);
        ViewHolder.name.setText(doctorNames.getNames());
        ViewHolder.username.setText(doctorsList.getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("receiverUser",doctorsList.getUsername());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        static public TextView name;
        static public TextView username;

        public ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.doc_name);
            username = itemView.findViewById(R.id.doc_username);
        }
    }

}
