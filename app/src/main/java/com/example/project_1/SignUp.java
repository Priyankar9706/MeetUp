package com.example.project_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    EditText fname,email,pass,cpass;
    TextView verem;
    TextView login;
    RadioGroup radioGroup;
    RadioButton r;
    Button register;
    FirebaseDatabase firebaseDatabase;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String userId;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        fname = findViewById(R.id.fnameEdit);
        email = findViewById(R.id.emailEdit);
        pass = findViewById(R.id.passwordEdit);
        cpass = findViewById(R.id.cpasswordEdit);
        register = findViewById(R.id.submit);
        progressBar = findViewById(R.id.progressBar);
        radioGroup = findViewById(R.id.radioGroup);
        login = findViewById(R.id.logIns);
        verem = findViewById(R.id.verem);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(true){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String semail = email.getText().toString();
                String sfname = fname.getText().toString();
                String spass = pass.getText().toString();
                String scpass = cpass.getText().toString();
                String imageURL = "default";
                int id = radioGroup.getCheckedRadioButtonId();
                r = findViewById(id);
                String sgender = r.getText().toString();
                if (TextUtils.isEmpty(semail)) {
                    email.setError("Please enter email");
                    email.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(sfname)) {
                    fname.setError("Please enter name");
                    fname.requestFocus();
                    return;
                }
                if (spass.length() < 6) {
                    pass.setError("Password must contain atleast 6 characters");
                    pass.requestFocus();
                    return;
                }
                if (!scpass.matches(spass)) {
                    cpass.setError("Password does not matches");
                    cpass.requestFocus();
                    return;
                }
                if (id == -1) {
                    r.setError("Please check one of it!!");
                }
                if (TextUtils.isEmpty(spass)) {
                    pass.setError("Please enter email");
                    pass.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                firebaseAuth.createUserWithEmailAndPassword(semail, spass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //sendVerification
                            FirebaseUser fuser = firebaseAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SignUp.this, "Verification email sent", Toast.LENGTH_SHORT).show();
                                    register.setText("Proceed");
                                    verem.setVisibility(View.VISIBLE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUp.this, "Failed to send Email " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                            Map<String, String> interests = new HashMap<>();
                            Map<String, Object> user = new HashMap<>();
                            user.put("FullName", sfname);
                            user.put("Email", semail);
                            user.put("Gender", sgender);
                            user.put("Interests", interests);
                            user.put("ImageURL", imageURL);
                            userId = firebaseAuth.getCurrentUser().getUid();

                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(userId)
                                    .setValue(user)
                                    .addOnSuccessListener(aVoid -> {

                                    });

                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            register.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    register.setText("Register");
                                    verem.setVisibility(View.INVISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    Toast.makeText(SignUp.this, "User Created", Toast.LENGTH_SHORT).show();
                                    firebaseAuth.signOut();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    }
                            });
                        } else {

                            Toast.makeText(SignUp.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }

    public void closeKeyboard(View v){
         View view=this.getCurrentFocus();
        if(view!=null){
            InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);

        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

}