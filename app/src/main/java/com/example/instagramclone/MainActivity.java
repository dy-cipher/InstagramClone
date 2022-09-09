package com.example.instagramclone;




import android.os.Bundle;

import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.example.instagramclone.fragment.HomeFragment;
import com.example.instagramclone.fragment.PostFragment;
import com.example.instagramclone.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;



public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    final Fragment PostFragment = new PostFragment();
    final Fragment HomeFragment = new HomeFragment();
    final Fragment ProfileFragment = new ProfileFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, HomeFragment).commit();
                        break;
                    case R.id.plus:
                        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, PostFragment).commit();
                        break;
                    case R.id.profil:
                        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, ProfileFragment).commit();
                        break;
                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.home);

//        ParseObject firstObject = new  ParseObject("FirstClass");
//        firstObject.put("message","Hey ! First message from android. Parse is now connected");
//        firstObject.saveInBackground(e -> {
//            if (e != null){
//                Log.e(TAG, e.getLocalizedMessage());
//            }else{
//                Log.d(TAG,"Object saved.");
//            }
//        });
//
//        queryPost();
//
//
//
//


    }}