package com.example.project_1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class fragment_1 extends Fragment {
    TextView t;
    View v;
    LinearLayout ll;
    TextView na;

    FirebaseUser fuser;
    String userId;
    String otheruserimgURL;
    String otherName,imageURLS;
    Map<String, String> interests=new HashMap<>();
    List<DocumentSnapshot> name;
    String otherid;
    FirebaseAuth firebaseAuth;
    String sr,rs;
    ArrayList otherUser=new ArrayList<String>();
    ArrayList nameList=new ArrayList<String>();
    ArrayList imageURL=new ArrayList<String>();
    ArrayList user=new ArrayList<String>();
    Map<String,ArrayList<String>> cinterests=new HashMap<>();
    Intent intent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_1, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        fuser = firebaseAuth.getCurrentUser();
        userId = firebaseAuth.getCurrentUser().getUid();

        ll=v.findViewById(R.id.ll_f1);
        na=v.findViewById(R.id.na_f1);
        na.setVisibility(View.INVISIBLE);
        ll.setVisibility(View.INVISIBLE);



        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null && snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.child("users").getChildren()) {
                        if (!(userId.equals(dataSnapshot.getKey()))) {
                            otherid = dataSnapshot.getKey();
                            sr = userId + otherid;
                            rs = otherid + userId;
                            for (DataSnapshot dataSnapshot1 : snapshot.child("chats").getChildren()) {
                                if (dataSnapshot1.getKey().equals(rs) || dataSnapshot1.getKey().equals(sr)) {
                                    Map<String, String> otherinterests = (Map<String, String>) dataSnapshot.child("Interests").getValue();
                                    ArrayList<String> other_interests = new ArrayList<>();
                                    if (otherinterests != null) {
                                        for (String key : otherinterests.keySet()) {
                                            other_interests.add(otherinterests.get(key));
                                        }
                                        otheruserimgURL = (String) dataSnapshot.child("ImageURL").getValue();
                                        cinterests.put(otherid, other_interests);
                                        nameList.add(dataSnapshot.child("FullName").getValue());
                                        otherUser.add(otherid);
                                        imageURL.add(dataSnapshot.child("ImageURL").getValue());
                                    }
                                    break;
                                }

                            }
                        }
                    }

                    if (!nameList.isEmpty()) {
                        for (Object friend : nameList) {
                            user.add(friend.toString() + "");
                            ListView listView = v.findViewById(R.id.list_f1);
                            ArrayList<ArrayList<String>> userList = new ArrayList<>();
                            userList.addAll(cinterests.values());
                            CustomListAdapter adapter = new CustomListAdapter(getActivity(), nameList, userList, imageURL,otherUser);
                            listView.setAdapter(adapter);
                        }

                    } else {
                        na.setVisibility(View.VISIBLE);
                        ll.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ListView listView=v.findViewById(R.id.list_f1);
        ArrayAdapter adapter=new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,user);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent=new Intent(getContext(),ChatDetailActivity.class);
                String ouserId= (String) otherUser.get(position);
                otherName= (String) nameList.get(position);
                imageURLS= (String) imageURL.get(position);
                intent.putExtra("otherUser",ouserId);
                intent.putExtra("otherName",otherName);
                intent.putExtra("imageURL",imageURLS);
                startActivity(intent);
            }
        });

        return v;
    }
}