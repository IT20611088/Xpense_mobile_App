package com.example.xpensemobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import android.widget.EditText;

import android.widget.Button;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUp_page extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        Objects.requireNonNull(getSupportActionBar()).hide();


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            finish();
            return;
        }

        Button btnRegister = findViewById(R.id.loginbutton);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });


        TextView textViewSwitchToLogin = findViewById(R.id.textView_signup_page3);
        textViewSwitchToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToLogin();
            }
        });



    }


    private void registerUser() {
        EditText textView_signup_pageTextName = findViewById(R.id.textView_signup_pageTextName);
        EditText textView_signup_pageTextEmail = findViewById(R.id.textView_signup_pageTextEmail);
        EditText editTextTextPassword = findViewById(R.id.loginPassword);

        String name = textView_signup_pageTextName.getText().toString();
        String email = textView_signup_pageTextEmail.getText().toString();
        String password = editTextTextPassword.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(name,email);
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            showMainActivity();
                                        }
                                    });
                        } else {
                            Toast.makeText(SignUp_page.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void showMainActivity() {
        Intent intent = new Intent(this, Navigation_drawer.class);
        startActivity(intent);
        finish();
    }

    private void switchToLogin() {
        Intent intent = new Intent(this, Login_page.class);
        startActivity(intent);
        finish();
    }
}