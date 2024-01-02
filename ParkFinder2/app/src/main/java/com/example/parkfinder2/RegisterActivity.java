package com.example.parkfinder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    //variables
    private EditText editTextName, editTextEmail, editTextGWID, editTextPassword, editTextConfirm;
    private static final String TAG = "RegisterActivity";

    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //link variables to elements
        editTextName = findViewById(R.id.registerName);
        editTextEmail = findViewById(R.id.registerEmail);
        editTextGWID = findViewById(R.id.registerGWID);
        editTextPassword = findViewById(R.id.registerPassword);
        editTextConfirm = findViewById(R.id.registerConfirm);
        backButton = findViewById(R.id.backButton2);

        //back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });



        //register button

        Button buttonRegister = findViewById(R.id.registerbutton2);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //convert values to Strings from editText
                String textName = editTextName.getText().toString();
                String textEmail = editTextEmail.getText().toString();
                String textGWID = editTextGWID.getText().toString();
                String textPassword = editTextPassword.getText().toString();
                String textConfirm = editTextConfirm.getText().toString();

                boolean emailEnding = textEmail.endsWith("@gwu.edu");

                //adding constraints to inputs for registration
                if(TextUtils.isEmpty(textName)){
                    Toast.makeText(RegisterActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    editTextName.setError("Full Name is required");
                    editTextName.requestFocus();
                }
                else if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(RegisterActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(RegisterActivity.this, "Re-enter Email", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Valid Email is Required");
                    editTextEmail.requestFocus();
                }
                else if(!emailEnding){
                    Toast.makeText(RegisterActivity.this, "Re-enter Email", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Must be a '@gwu.edu' email");
                    editTextEmail.requestFocus();
                }
                else if(TextUtils.isEmpty(textGWID)){
                    Toast.makeText(RegisterActivity.this, "Enter GWID", Toast.LENGTH_SHORT).show();
                    editTextGWID.setError("GWID is required");
                    editTextGWID.requestFocus();
                }
                else if(textGWID.length() != 9){
                    Toast.makeText(RegisterActivity.this, "Re-enter GWID", Toast.LENGTH_SHORT).show();
                    editTextGWID.setError("GWID must be 9 characters");
                    editTextGWID.requestFocus();
                }
                else if(TextUtils.isEmpty(textPassword)){
                    Toast.makeText(RegisterActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    editTextPassword.setError("Password is required");
                    editTextPassword.requestFocus();
                }
                else if (!containsSpecialCharacter(textPassword)) {
                    Toast.makeText(RegisterActivity.this, "Password must contain special characters", Toast.LENGTH_SHORT).show();
                    editTextPassword.setError("Password must contain special characters");
                    editTextPassword.requestFocus();
                }
                else if(TextUtils.isEmpty(textConfirm)){
                    Toast.makeText(RegisterActivity.this, "Enter Confirm", Toast.LENGTH_SHORT).show();
                    editTextConfirm.setError("Confirm is required");
                    editTextConfirm.requestFocus();
                }
                else if(!textPassword.equals(textConfirm)){
                    Toast.makeText(RegisterActivity.this, "Password Does Not Match", Toast.LENGTH_SHORT).show();
                    editTextConfirm.setError("Password Does Not Match");
                    editTextConfirm.requestFocus();

                    editTextPassword.clearComposingText();
                    editTextConfirm.clearComposingText();
                }
                else{
                    registerUser(textName, textEmail, textGWID, textPassword, textConfirm);
                }


            }
        });
    }

    //method used for requiring special character in password
    private boolean containsSpecialCharacter(String password) {
        // Define your set of special characters
        String specialCharacters = "!@#$%^&*()_+-=[]{}|;:'\",.<>?";

        // Check if the password contains at least one special character
        for (char specialChar : specialCharacters.toCharArray()) {
            if (password.contains(String.valueOf(specialChar))) {
                return true;
            }
        }

        return false;
    }

    //Register User Method
    private void registerUser(String textName, String textEmail, String textGWID, String textPassword, String textConfirm) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){


                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    //UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textName).build();
                    //firebaseUser.updateProfile(profileChangeRequest);

                    //Enter User Data into the DB
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails( textName,textEmail, textGWID);

                    //Extracting User reference from DB for "Registered Users"
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(RegisterActivity.this, UserProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);
                                finish();

                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Registration Failed. Try again", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
                else{
                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException e){
                        editTextPassword.setError("Your password is too weak. Use a mix of alphabets, numbers, and characters");
                        editTextPassword.requestFocus();
                    }
                    catch(FirebaseAuthInvalidCredentialsException e){
                        editTextPassword.setError("Your email is invalid or already in use. ");
                        editTextPassword.requestFocus();
                    }
                    catch(FirebaseAuthUserCollisionException e){
                        editTextPassword.setError("User is already registered with this email. Use another email");
                        editTextPassword.requestFocus();
                    }
                    catch(Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}