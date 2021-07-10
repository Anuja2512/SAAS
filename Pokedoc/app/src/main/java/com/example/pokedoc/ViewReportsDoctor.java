package com.example.pokedoc;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewReportsDoctor extends AppCompatActivity {

    ListView myPDFListView;
    TextView mytext;
    DatabaseReference databaseReference;
    List<UploadFiless> uploadFile;
    HashMap<String, String > map;
    String patuid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports_doctor);
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
        patuid=getIntent().getStringExtra("uid");
        myPDFListView=(ListView)findViewById(R.id.myListView);
        uploadFile=new ArrayList<>();

        viewAllFiles();
        myPDFListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UploadFiless uploadFiless= uploadFile.get(i);
              //  Toast.makeText(ViewReportsDoctor.this, Uri.parse(uploadFiless.getUrl()).toString(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uploadFiless.getUrl()));
                startActivity(intent);

            }
        });
    }

    private void viewAllFiles() {

        databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(patuid).child("Reports");
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
                    //   Toast.makeText(ViewAllFiles.this, map.get("name")+ map.get("url"), Toast.LENGTH_SHORT).show();

                    // uploadFile uploadFiles= new uploadFile(map.get("name"), map.get("url"));
                    UploadFiless uploadFiles=new UploadFiless(map.get("name"), map.get("url"));
                    uploadFile.add(uploadFiles);
                }

                String[] uploads=new String[uploadFile.size()];

                for(int i=0; i< uploads.length;i++){
                    uploads[i]=uploadFile.get(i).getName();
                }

                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), R.layout.reportitem,uploads);
                myPDFListView.setAdapter(adapter);



            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}