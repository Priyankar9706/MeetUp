package com.example.project_1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomListAdapter extends ArrayAdapter {
  private Activity context;
    private  List nameArray;
    private  List interestArray;
    private  List imgURL;
    private  List otherUserId;
    public CustomListAdapter(Activity context, ArrayList nameArray,ArrayList interestArray,ArrayList imgURL,ArrayList otherUserId) {
        super(context, R.layout.show_users,nameArray);
        this.context=context;
        this.nameArray=nameArray;
        this.interestArray=interestArray;
        this.imgURL=imgURL;
        this.otherUserId=otherUserId;
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.show_users, null, true);
        TextView lastmsg=rowView.findViewById(R.id.lastmsg);
        TextView username=rowView.findViewById(R.id.username);
        TextView commoninterestfield=rowView.findViewById(R.id.interestsfield);
        CircleImageView imageView=rowView.findViewById(R.id.profile_image);


        username.setText( nameArray.get(position).toString());
        commoninterestfield.setText(interestArray.get(position).toString());




        FirebaseDatabase.getInstance().getReference("chats").child(FirebaseAuth.getInstance().getUid()+otherUserId.get(position))
                .orderByChild("time")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                lastmsg.setText(dataSnapshot1.child("msg").getValue().toString());
                            }
                        }else {
                            lastmsg.setText("Start a chat by sending 'Hi'... ");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        if(imgURL.get(position).toString().equals("default")){
            imageView.setImageResource(R.drawable.defaultpp);
        }else{
            Picasso.get().load(imgURL.get(position).toString()).placeholder(R.drawable.defaultpp).into(imageView);
        }
        return rowView;
    }
}
