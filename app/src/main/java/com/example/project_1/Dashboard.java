package com.example.project_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Dashboard extends AppCompatActivity {
    GridLayout gridLayout;
    private FirebaseAuth firebaseAuth;
    Button save;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    String userId;
    Map<String, String> interests;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        gridLayout = findViewById(R.id.gridlayout);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        save=findViewById(R.id.next);

        FirebaseDatabase.getInstance().getReference("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null && snapshot.exists()) {
                    interests = (Map) snapshot.child("Interests").getValue();
                    if(interests==null){
                            count=0;
                        }else {
                            count = interests.size();
                            for (String key : interests.keySet()) {
                                CardView cardView = (CardView) gridLayout.getChildAt(Integer.parseInt(key));
                                cardView.setAlpha((float) 0.5);
                            }
                        }
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            CardView cardView = (CardView) gridLayout.getChildAt(i);
            ViewGroup viewGroup = (ViewGroup) cardView.getChildAt(0);
            TextView textView = (TextView) viewGroup.getChildAt(1);
            int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (count < 3) {

                        if (interests!=null && interests.containsKey(String.valueOf(finalI))) {
                            Toast.makeText(Dashboard.this, "Alphaed1", Toast.LENGTH_SHORT).show();
                            cardView.setAlpha(1);
                            interests.remove(String.valueOf(finalI));
                            count--;
                            Log.i("Sheetal", String.valueOf(count));
                        } else {
                            cardView.setAlpha((float) 0.5);
                            count++;
                            if(interests==null){
                             interests=new HashMap<>();
                            }
                            interests.put(String.valueOf(finalI), textView.getText().toString());
                            Log.i("Sheetal", String.valueOf(count));
                            Toast.makeText(Dashboard.this, "Faded", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        if (interests.containsKey(String.valueOf(finalI))) {
                            Toast.makeText(Dashboard.this, "Alphaed2", Toast.LENGTH_SHORT).show();
                            cardView.setAlpha(1);
                            interests.remove(String.valueOf(finalI));
                            count--;
                        } else {
                            new AlertDialog.Builder(Dashboard.this)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Limit reached !!")
                                    .setMessage("You can add max 3 interests...")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        }

                    }
                }
            });

        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("users").child(userId).child("Interests").setValue(interests);
                startActivity(new Intent(getApplicationContext(),TabInterface.class));
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),TabInterface.class));
    }
}