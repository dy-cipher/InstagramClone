package com.example.instagramclone;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.instagramclone.fragment.HomeFragment;
import com.example.instagramclone.fragment.ProfileFragment;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    public static final  String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

         EditText etUserLog;
         EditText etPassword;
         Button btnLog;
         Button btnSignUp;


        etUserLog = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        btnLog = findViewById(R.id.btnLog);
        btnSignUp = findViewById(R.id.btnSignUp);



        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick button");
                String username = etUserLog.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });

        // signup button click
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signUp button");
                String username = etUserLog.getText().toString();
                String password = etPassword.getText().toString();
                signUpUser(username, password);
            }
        });
    }



    private void loginUser(String username, String password){
        Log.i(TAG, "Attempting to login user " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Go to the main activity
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void signUpUser(String username, String password) {
        Log.i(TAG, "Attempting to user signIn " + username);
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with signUp", e);
                    Toast.makeText(LoginActivity.this, "Issue with signUp", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Go to the main activity
                goMainActivity();
            }
        });
    }

    private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showEditDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = HomeFragment.newInstance("Home");
        homeFragment.show(fragmentManager, "fragment_home");
    }
}