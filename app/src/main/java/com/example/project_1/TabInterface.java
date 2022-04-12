package com.example.project_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class TabInterface extends AppCompatActivity {
        TabLayout tabLayout;
        ViewPager2 viewPager2;
        TabItem chats,friends;
        ImageView editChoice,logout;
        FirebaseAuth firebaseAuth;
        CircleImageView profile;
        FragAdapter fragAdapter;
        AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_tab_interface);
        tabLayout=findViewById(R.id.tabLayout);
        viewPager2=findViewById(R.id.viewPager2);
        chats=findViewById(R.id.chats);
        friends=findViewById(R.id.friends);
        editChoice=findViewById(R.id.choice);
        logout=findViewById(R.id.logout);
        profile=findViewById(R.id.profile);
       builder=new AlertDialog.Builder(this);

        editChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Dashboard.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder
                        .setTitle("LogOut")
                        .setIcon(R.drawable.attention)
                        .setCancelable(false)
                        .setMessage("Are you sure to logout?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            dialog.dismiss();
                            firebaseAuth.signOut();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        })

                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(),Profile.class));

            }
        });

        FragmentManager fm=getSupportFragmentManager();
        fragAdapter=new FragAdapter(fm,getLifecycle());
        viewPager2.setAdapter(fragAdapter);
        tabLayout.getTabAt(0).setIcon(R.drawable.chat);
        tabLayout.getTabAt(1).setIcon(R.drawable.friend);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
               tabLayout.selectTab(tabLayout.getTabAt(position));

            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
}