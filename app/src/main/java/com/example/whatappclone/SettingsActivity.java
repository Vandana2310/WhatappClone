package com.example.whatappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.whatappclone.Model.Users;
import com.example.whatappclone.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        storage=FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        //for saving and updating username and about in database
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String about=binding.edtStatus.getText().toString();
                String username=binding.edtUsernameSettings.getText().toString();

                HashMap<String,Object> obj=new HashMap<>();
                obj.put("username",username);
                obj.put("about",about);

               database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(obj);

                Toast.makeText(SettingsActivity.this, "Profile updated ", Toast.LENGTH_SHORT).show();
            }
        });

        //bringing image back from firebase to app
        //for showing image into settings activity
        database.getReference().child("Users").child(FirebaseAuth.getInstance()
                .getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users users=snapshot.getValue(Users.class);
                Picasso.get()
                        .load(users.getProfilePic())
                        .placeholder(R.drawable.user)
                        .into(binding.profileImage);

                binding.edtUsernameSettings.setText(users.getUsername());
                binding.edtStatus.setText(users.getAbout());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,33);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData()!=null){
            Uri sFile= data.getData();
            binding.profileImage.setImageURI(sFile);

            final StorageReference storageReference =storage.getReference().child("profilePic").child(FirebaseAuth.getInstance().getUid());

            storageReference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("profilepic")
                                    .setValue(uri.toString());
                            Toast.makeText(SettingsActivity.this, "Profile picture updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}