package com.example.pokedoc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ViewAllFiles extends AppCompatActivity {

    ListView myPDFListView;
    TextView mytext;
    SwipeRefreshLayout refreshing;
    DatabaseReference databaseReference;
    List<UploadFiless> uploadFile;
    ListView myList;

    HashMap<String, String > map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_files);
        int nightModeFlags = this.getApplicationContext().getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
                // Resources layout =getResources();
                //  layout.(R.drawable.backgrounddark);
                layout.setBackgroundResource(R.drawable.backdark);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                RelativeLayout layoutlight = (RelativeLayout) findViewById(R.id.layout);
                layoutlight.setBackgroundResource(R.drawable.back);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                RelativeLayout layoutd = (RelativeLayout) findViewById(R.id.layout);
                // Resources layout =getResources();
                //  layout.(R.drawable.backgrounddark);
                layoutd.setBackgroundResource(R.drawable.backdark);
                break;
        }

        myPDFListView=(ListView)findViewById(R.id.myListView);
        refreshing=findViewById(R.id.reportRefresh);
        myList = findViewById(R.id.myListView);

        uploadFile=new ArrayList<>();

        viewAllFiles();

        refreshing.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                refreshing.setRefreshing(false);
            }
        });

        myPDFListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UploadFiless uploadFiless= uploadFile.get(i);
                Toast.makeText(ViewAllFiles.this, Uri.parse(uploadFiless.getUrl()).toString(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Intent.ACTION_VIEW);
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
                        map = new HashMap<>();
                        for(DataSnapshot snapshot: postSnapshot.getChildren())
                        {

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


                       // uploadFile uploadFiles= new uploadFile(map.get("name"), map.get("url"));
                       UploadFiless uploadFiles=new UploadFiless(map.get("name"), map.get("url"));
                       uploadFile.add(uploadFiles);
                    }

                    final String[] uploads=new String[uploadFile.size()];

                    for(int i=0; i< uploads.length;i++){
                        uploads[i]=uploadFile.get(i).getName();
                    }

               ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), R.layout.reportitem,uploads);
                    myPDFListView.setAdapter(adapter);

                myPDFListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Toast.makeText(ViewAllFiles.this, uploads[position], Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder dialog = new AlertDialog.Builder(ViewAllFiles.this);
                        dialog.setTitle("Delete The Report");
                        dialog.setMessage("Deleting this file will result in completely removing this " +
                                "document from your reports."+
                                "Do you want to continue?");
                        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Reports");
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                        for(DataSnapshot snapshot1: snapshot.getChildren())
                                        {
                                            String hash = snapshot1.getKey();
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Reports").child(hash);
                                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot snapshot2: snapshot.getChildren())
                                                    {
                                                        if(snapshot2.getKey().equals("name")&&snapshot2.getValue().toString().equals(uploads[position]))
                                                        {
                                                            reference.removeValue();
                                                            finish();

                                                        }
                                                    }
                                                    
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
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
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}