package com.example.pokedoc;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    FirebaseUser fUser;
    DatabaseReference reference;
    ImageButton send_btn;
    EditText msg_send;
    String receiver;
    HashMap<String, String> map;
    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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
                                    Toast.makeText(ChatActivity.this, receiver, Toast.LENGTH_SHORT).show();

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
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    map = new HashMap<>();
                    for(DataSnapshot snapshot1: snapshot.getChildren())
                    {

                        if(snapshot1.getKey().equals("sender"))
                        {
                            String sender = snapshot1.getValue(String.class);
                            map.put("sender", sender);
                        }
                        if(snapshot1.getKey().equals("receiver"))
                        {
                            String receiver = snapshot1.getValue(String.class);
                            map.put("receiver", receiver);
                        }
                        if(snapshot1.getKey().equals("message"))
                        {
                            String message = snapshot1.getValue(String.class);
                            map.put("message", message);

                        }

                    }
                    Chat chat= new Chat(map.get("sender"),map.get("receiver"),map.get("message"));
                    mChat.add(chat);
                    messageAdapter=new MessageAdapter(ChatActivity.this,mChat);
                    recyclerView.setAdapter(messageAdapter);
                }
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