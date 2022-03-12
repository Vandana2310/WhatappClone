package com.example.whatappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatappclone.Adapter.ChatAdapter;
import com.example.whatappclone.Model.MessagesModel;
import com.example.whatappclone.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupChatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

       final FirebaseDatabase database=FirebaseDatabase.getInstance();
       final ArrayList<MessagesModel> messagesModels=new ArrayList<>();

       final String senderInd= FirebaseAuth.getInstance().getUid();
       binding.Username.setText("Group chat");
       final ChatAdapter adapter= new ChatAdapter(messagesModels,this);
       binding.chatRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        database.getReference().child("Group Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesModels.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MessagesModel model=dataSnapshot.getValue(MessagesModel.class);
                    messagesModels.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String messages=binding.edtMessage.getText().toString();
                final MessagesModel model=new MessagesModel(senderInd,messages);
                model.setTimeStamp(new Date().getTime());
                binding.edtMessage.setText("");

                database.getReference().child("Group Chat").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });
    }
}