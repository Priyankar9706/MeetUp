package com.example.project_1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import org.w3c.dom.Text;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
import static android.text.InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD;

public class Profile extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ImageView editImage,ename,epassword;
    EditText aname,apassword;
    String userid;
    Button  save,cancel;
    RadioGroup gender;
    RadioButton male,female,trans,radioButton;
    boolean flag1,flag2;


    FirebaseStorage firebaseStorage;
    Uri imageUri;
    String myUri="";
    StorageTask uploadTask;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        userid=firebaseUser.getUid();
        editImage=findViewById(R.id.editimage);
        ename=findViewById(R.id.ename);
        cancel=findViewById(R.id.cancel);
        epassword=findViewById(R.id.epassword);
        aname=findViewById(R.id.aname);
        gender=findViewById(R.id.gender);
        apassword=findViewById(R.id.apassword);
        save=findViewById(R.id.save);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        trans=findViewById(R.id.trans);
        flag1=true;
        flag2=true;
        storageReference=FirebaseStorage.getInstance().getReference("user").child("Profile Pic");
        FirebaseDatabase.getInstance().getReference("users").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                aname.setText(snapshot.child("FullName").getValue().toString());
                if(snapshot.child("Gender").getValue().toString().equals("Male")){
                    gender.check(R.id.male);
                }else if(snapshot.child("Gender").getValue().toString().equals("Female")){
                    gender.check(R.id.female);
                }else{
                    gender.check(R.id.trans);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        epassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag2){
                    apassword.setHint("Enter your new password");
                    apassword.setInputType(1);
                    apassword.setCursorVisible(true);
                    epassword.setImageResource(R.drawable.ok);
                    flag2=false;
                }else{
                    if(apassword.getText().toString().length()<6){
                        apassword.setError("Password must contain atleast 6 characters...");
                        return;
                    }else{
                        apassword.setHint("");
                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(apassword.getText().toString());
                        Toast.makeText(Profile.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                        apassword.setInputType(0);
                        apassword.getEditableText();
                        apassword.setCursorVisible(false);
                        closeKeyboard(v);
                        epassword.setImageResource(R.drawable.imageedit);
                        flag2=true;
                    }

                }

            }
        });

        ename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag1){
                    aname.setInputType(1);
                    aname.setCursorVisible(true);
                    ename.setImageResource(R.drawable.ok);
                    flag1=false;
                }else{

                    aname.setInputType(0);
                    aname.getEditableText();
                    aname.setCursorVisible(false);
                    closeKeyboard(v);
                    ename.setImageResource(R.drawable.imageedit);
                    flag1=true;
                }
            }
        });
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference("users").child(userid).child("FullName").setValue(aname.getText().toString());
                int id=gender.getCheckedRadioButtonId();
                radioButton=findViewById(id);
                if(radioButton.getText().toString().equals("Male")){

                    FirebaseDatabase.getInstance().getReference("users").child(userid).child("Gender").setValue("Male");
                }else{
                    FirebaseDatabase.getInstance().getReference("users").child(userid).child("Gender").setValue("Female");
                }
                Toast.makeText(Profile.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),TabInterface.class));
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Profile.this, "No Changes Made", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),TabInterface.class));
                finish();
            }
        });
    }
    public void closeKeyboard(View vi){
        View view=this.getCurrentFocus();
        if(view!=null){
            InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);

        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),TabInterface.class));

    }
}