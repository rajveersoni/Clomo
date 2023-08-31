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

public class CreateAccountActivity extends AppCompatActivity {

     EditText etUsername, etEmail, etPass1, etPass2;
     Button submitButton;

     ProgressBar progressBar;
     TextView loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPass1 = findViewById(R.id.etPass1);
        etPass2 = findViewById(R.id.etPass2);
        submitButton = findViewById(R.id.submitButtom);
        progressBar = findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.loginButton);


        submitButton.setOnClickListener(v-> createAccount());
        loginButton.setOnClickListener(v-> startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class)));
    }

    public void createAccount() {

        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass1 = etPass1.getText().toString().trim();
        String pass2 = etPass2.getText().toString().trim();

        boolean isValid = validateData(username, email,pass1,pass2);

        if(!isValid){
            return;
        }
        createAccountFirebase(email,pass1);

    }
    void createAccountFirebase (String email, String password){

    changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()){
//                            creating account done
                            Toast.makeText(CreateAccountActivity.this, "Successfully created account, check email to verify", Toast.LENGTH_SHORT).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }else {
//                            failure
                            Toast.makeText(CreateAccountActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            
                        }
                    }
                });

    }

    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            submitButton.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String username, String email, String pass1, String pass2){
        if (username.isEmpty()){
            etUsername.setError("Username is Empty");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Email is invalid");
            return false;
        }
        if (pass1.length() < 6){
            etPass1.setError("Password is less than 6 letter");
            return false;
        }
        if (!pass1.equals(pass2)){
            etPass2.setError("Password not matched");
            return false;
        }

        return true;
    }
}

















