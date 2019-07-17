package com.example.sebastian.formex;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText etEmail;
    EditText etPassword;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

        isLoggedin();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.al_et_email);
        etPassword = findViewById(R.id.al_et_password);
        TextView tvRegister = findViewById(R.id.al_et_register);
        TextView tvLogin = findViewById(R.id.al_et_login);



        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = validLogin();
                register(user);
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = validLogin();
                login(user);
            }
        });

    }

    private void login(User user){
        if(user != null){
            mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                isLoggedin();
                            } else {
                                Toast.makeText(LoginActivity.this, "Login failed, check your info.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }

    }

    private void isLoggedin(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private User validLogin(){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        User user = new User(email, password);
        if(user.validLogin()) return user;
        Toast.makeText(this, "Some information is missing", Toast.LENGTH_SHORT).show();
        return null;
    }

    private void register(User user){
        if(user != null){
            mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                isLoggedin();
                            } else {
                                Toast.makeText(LoginActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}
