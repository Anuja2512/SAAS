package com.example.pokedoc;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewAllFiles extends AppCompatActivity {

    ListView myPDFListView;
    DatabaseReference databaseReference;
    List<uploadFile> uploadFile;
    HashMap<String, String > map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_files);

        myPDFListView=(ListView)findViewById(R.id.myListView);
        uploadFile=new ArrayList<>();

        viewAllFiles();

        myPDFListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                uploadFile uploadFiless= uploadFile.get(i);
                Intent intent=new Intent();
                //intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uploadFiless.getUrl()));
                startActivity(intent);

            }
        });
    }

    private void viewAllFiles() {

        databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Reports");
        databaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                        for(DataSnapshot snapshot: postSnapshot.getChildren())
                        {
                            map = new HashMap<>();
                            if(snapshot.getKey().equals("name"))
                            {
                                String name = snapshot.getValue(String.class);
                                map.put("name", name);
                            }
                            if(snapshot.getKey().equals("url"))
                            {
                                String url = snapshot.getValue(String.class);
                                map.put("url", url);
                            }
                        }
                        //uploadFile uploadFiles= new uploadFile(map.get("name"), map.get("url"));
                        uploadFile uploadFiles=postSnapshot.getValue(uploadFile.class);
                        uploadFile.add(uploadFiles);
                    }

                    String[] uploads=new String[uploadFile.size()];

                    for(int i=0; i< uploads.length;i++){
                        uploads[i]=uploadFile.get(i).getName();
                    }

                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,uploads){


                        @Override
                    public View getView(int position, View convertView, ViewGroup parent){

                          View view=super.getView(position,convertView,parent);
                          TextView mytext=(TextView)view.findViewById(android.R.id.text1);
                          mytext.setTextColor(Color.BLACK);


                           return view;
                        }
                    };
                    myPDFListView.setAdapter(adapter);

                }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}