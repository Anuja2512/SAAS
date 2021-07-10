package com.example.pokedoc;
import android.content.res.Configuration;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.database.annotations.NotNull;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    FirebaseUser fUser;
    DatabaseReference reference, reff;
    ImageButton send_btn;
    EditText msg_send;
    String receiver;
    HashMap<String, String> map;
    MessageAdapter messageAdapter;
    List<Chat> mChat;
    Boolean b;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
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
        b=true;
        send_btn=findViewById(R.id.btn_send);
        msg_send=findViewById(R.id.text_send);
        receiver=getIntent().getStringExtra("receiverUser");

        recyclerView=findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg= msg_send.getText().toString();
                if(!msg.equals("")){
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot1: snapshot.getChildren())
                            {
                                if(snapshot1.getKey().toString().equals("Username"))
                                {
                                    String myusername = snapshot1.getValue(String.class);
                                    sendMessage(myusername,receiver,msg);


                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

                }
                else {
                    Toast.makeText(ChatActivity.this,"You cannot send empty message",Toast.LENGTH_LONG).show();
                }
                msg_send.setText("");
            }
        });

        mChat=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                mChat.clear();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1: snapshot.getChildren())
                        {
                            if(snapshot1.getKey().toString().equals("Username"))
                            {
                                String myusername = snapshot1.getValue(String.class);

                                for(DataSnapshot snapshott: dataSnapshot.getChildren()){
                                    map = new HashMap<>();
                                    String sender="";
                                    String receiver1="";
                                    for(DataSnapshot snapshott1: snapshott.getChildren())
                                    {

                                        if(snapshott1.getKey().equals("sender"))
                                        {
                                            sender = snapshott1.getValue(String.class);
                                            if(sender.equals(receiver))
                                            {
                                                map.put("sender", sender);
                                            }
                                            if(sender.equals(myusername))
                                            {
                                                map.put("sender", sender);

                                            }

                                        }
                                        if((snapshott1.getKey().equals("receiver")))
                                        {
                                            receiver1 = snapshott1.getValue(String.class);
                                            if(receiver1.equals(receiver)){
                                                map.put("receiver", receiver1);
                                             //   Toast.makeText(ChatActivity.this, receiver1, Toast.LENGTH_SHORT).show();
                                            }

                                            if(receiver1.equals(myusername))
                                            {
                                                map.put("receiver", receiver1);
                                               // Toast.makeText(ChatActivity.this, receiver1, Toast.LENGTH_SHORT).show();
                                                reff= snapshott1.getRef();
                                            }

                                        }
                                       if((sender.equals(receiver)&&receiver1.equals(myusername))||(sender.equals(myusername)&&receiver1.equals(receiver)))
                                       {
                                          DatabaseReference reference= snapshott.getRef();
                                          String hash = reference.getKey();
                                           final String[] senderr = {""};
                                          DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats").child(hash);
                                           databaseReference.addValueEventListener(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                   for(DataSnapshot dataSnapshot1: snapshot.getChildren())
                                                   {

                                                       if(dataSnapshot1.getKey().equals("sender"))
                                                       {
                                                           senderr[0] =dataSnapshot1.getValue(String.class);
                                                           map.put("sender", senderr[0]);
                                                       }
                                                       if(dataSnapshot1.getKey().equals("message")) {
                                                           String message = dataSnapshot1.getValue(String.class);
                                                           map.put("message", message);

                                                       }

                                                   }
                                                   Chat chat= new Chat(senderr[0],map.get("receiver"),map.get("message"));
                                                   mChat.add(chat);
                                                   messageAdapter= new MessageAdapter(ChatActivity.this,mChat);
                                                   recyclerView.setAdapter(messageAdapter);

                                               }

                                               @Override
                                               public void onCancelled(@NonNull DatabaseError error) {

                                               }


                                           });

                                       }




                                    }

                                }

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void sendMessage(String sender,String receiver,String message){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats");
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        databaseReference.push().setValue(hashMap);
    }

}


