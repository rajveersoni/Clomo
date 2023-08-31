package com.example.memomind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPass;
    Button loginButton;

    ProgressBar progressBar;
    TextView createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        createAccount = findViewById(R.id.createAccount);



        loginButton.setOnClickListener(v-> loginUser());
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)); // can also use lamda like v->
            }
        });
    }

     void loginUser() {

         String email = etEmail.getText().toString().trim();
         String pass = etPass.getText().toString().trim();

         boolean isValid = validateData(email,pass);

         if(!isValid){
             return;
         }
         loginAccountFirebase(email,pass);
    }

     void loginAccountFirebase(String email, String pass) {
        changeInProgress(true);

         FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
         firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()){
                    //login successful
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to main activity
                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                        

                    }else {
                        //login failed
                        Toast.makeText(LoginActivity.this, "Email is not verified, please verify your email!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    //login failed
                    Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }



             }
         });

    }

    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String pass){

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Email is invalid");
            return false;
        }
        if (pass.length() < 6){
            etPass.setError("Password is Invalid");
            return false;
        }
        return true;
    }
}