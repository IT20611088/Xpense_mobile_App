package com.example.xpensemobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.xpensemobileapp.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class login_signup extends AppCompatActivity {

    Button button;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {

                Intent intent = new Intent(this, Navigation_drawer.class);
                startActivity(intent);
                finish();
            return;
        }

        Objects.requireNonNull(getSupportActionBar()).hide();

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });

        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivitysignup();
            }
        });
    }

    public void openNewActivity(){
        Intent intent = new Intent(this, Login_page.class);
        startActivity(intent);
    }

    public void openNewActivitysignup(){
        Intent intent = new Intent(this, SignUp_page.class);
        startActivity(intent);
    }
}