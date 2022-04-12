  package com.example.project_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project_1.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    String myid;
    ImageView back,clear;
    String otherid, othername, imageURL;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        final String myid = firebaseUser.getUid();
        intent = getIntent();
        firebaseFirestore = FirebaseFirestore.getInstance();
        back = findViewById(R.id.back);

        otherid = intent.getStringExtra("otherUser");
        othername = intent.getStringExtra("otherName");
        imageURL = intent.getStringExtra("imageURL");
        binding.fdname.setText(othername);

        if (imageURL.equals("default")) {
                binding.fdimg.setImageResource(R.drawable.defaultpp);
        } else {
                Picasso.get().load(imageURL).placeholder(R.drawable.defaultpp).into(binding.fdimg);
        }

        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageModels, this, otherid);

        binding.rcv.setAdapter(chatAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rcv.setLayoutManager(linearLayoutManager);
        final String receiverRoom = otherid + myid;
        final String senderRoom = myid + otherid;

        firebaseDatabase.getReference()
                .child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                            messageModel.setMessageId(dataSnapshot.getKey());
                            messageModels.add(messageModel);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//************************************************ Clear All Chat *******************************************************************//
//
//        clear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseDatabase.getInstance().getReference("chats").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                         String sr=FirebaseAuth.getInstance().getUid()+otherid;
//                         for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
//                             if (sr.equals(dataSnapshot.getKey())) {
//                                 Log.i("00000","00000");
//                                 new AlertDialog.Builder(ChatDetailActivity.this)
//                                         .setTitle("Clear Chat")
//                                         .setMessage("Are you sure you want to clear the chat?")
//                                         .setIcon(R.drawable.attention)
//                                         .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                             @Override
//                                             public void onClick(DialogInterface dialog, int which) {
//                                                 dialog.dismiss();
//                                                 dataSnapshot.getRef().setValue(null);
//                                             }
//                                         })
//                                         .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                             @Override
//                                             public void onClick(DialogInterface dialog, int which) {
//                                                 dialog.dismiss();
//                                             }
//                                         })
//                                         .create()
//                                         .show();
//                                 break;
//                             }
////                                                    Toast.makeText(ChatDetailActivity.this, "No Chat to delete", Toast.LENGTH_SHORT).show();
//                         }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });


        //************************************************ Clear All Chat *******************************************************************//


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TabInterface.class));
            }
        });

        binding.sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.editTextTextPersonName.getText().toString();
                final MessageModel messageModel = new MessageModel(myid, message);

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM");
                String currentDateandTime = sdf.format(new Date());
                messageModel.setTime(currentDateandTime);
                binding.editTextTextPersonName.setText("");
                FirebaseDatabase.getInstance().getReference()
                        .child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(messageModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                firebaseDatabase.getReference()
                                        .child("chats")
                                        .child(receiverRoom)
                                        .push()
                                        .setValue(messageModel)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                            }
                        });
            }
        });
        chatAdapter.notifyDataSetChanged();
    }

    public void closeKeyboard(View w){
        View vi=this.getCurrentFocus();
        if(vi!=null){
            InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(vi.getWindowToken(),0);

        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),TabInterface.class));
    }
}