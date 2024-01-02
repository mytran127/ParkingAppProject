package com.example.parkfinder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    // variables
    private EditText editTextLoginEmail, editTextLoginPassword;
    private static final String TAG = "LoginActivity";

    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // associating variables with elements
        editTextLoginEmail = findViewById(R.id.loginEmail);
        editTextLoginPassword = findViewById(R.id.loginPassword);

        authProfile = FirebaseAuth.getInstance();

        //Login User
        Button buttonLogin = findViewById(R.id.loginbutton2);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editTextLoginEmail.getText().toString();
                String textPassword = editTextLoginPassword.getText().toString();

                //adding constraints to login and password

                if(TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Email is required");
                    editTextLoginEmail.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(LoginActivity.this, "Re-enter Email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Valid Email is Required");
                    editTextLoginEmail.requestFocus();
                }
                else if(TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    editTextLoginPassword.setError("Password is required");
                    editTextLoginPassword.requestFocus();
                }
                else{
                    loginUser(textEmail, textPassword);
                }

            }
        });
    }

    //method to login user via Firebase
    private void loginUser(String email, String password) {
        authProfile.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "You Are Logged In Now", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginActivity.this, MainActivity2.class));
                    finish();
                }
                else{
                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidUserException e){
                        editTextLoginEmail.setError("User does not exists or is no longer valid. Please register again");
                        editTextLoginEmail.requestFocus();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        editTextLoginEmail.setError("Invalid Credentials");
                        editTextLoginEmail.requestFocus();
                    }
                    catch(Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginActivity.this, "You Are Logged In Now", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });


    }

    //checking if the user is already logged in
    @Override
    protected void onStart(){
        super.onStart();
        //check if there is a current user, if so just go to the reservation activity
        if(authProfile.getCurrentUser() != null){
            Toast.makeText(LoginActivity.this, "Already Logged In", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(LoginActivity.this, MainActivity2.class));
            finish();
        }
        else{
            Toast.makeText(LoginActivity.this, "Login Now", Toast.LENGTH_SHORT).show();
        }
    }
}