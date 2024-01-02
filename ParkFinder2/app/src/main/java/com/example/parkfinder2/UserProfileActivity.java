package com.example.parkfinder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    //variable
    private TextView textViewWelcome, textViewName, textViewEmail, textViewGWID, textViewPrice, textViewLocation, textViewLicense, textViewSD, textViewST, textViewED, textViewET;
    private String name, email, gwid;

    private Button buttonLogout;
    private FirebaseAuth authProfile;

    private ImageButton backButton;

    CardView recentTransactionCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //variable linked to element

        textViewWelcome = findViewById(R.id.welcomeMessage);
        textViewName = findViewById(R.id.profileUserName);
        textViewEmail = findViewById(R.id.profileUserEmail);
        textViewGWID = findViewById(R.id.profileUserGWID);
        buttonLogout = findViewById(R.id.logoutbutton);
        backButton = findViewById(R.id.backButton);

        recentTransactionCard = findViewById(R.id.recentTransaction);
        textViewPrice = findViewById(R.id.priceProfileUser);
        textViewLocation = findViewById(R.id.locationProfileUser);
        textViewLicense = findViewById(R.id.licenseProfileUser);
        textViewSD = findViewById(R.id.startDateProfileUser);
        textViewST = findViewById(R.id.startTimeProfileUser);
        textViewED = findViewById(R.id.endDateProfileUser);
        textViewET = findViewById(R.id.endTimeProfileUser);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser!= null){
            String userId = firebaseUser.getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId).child("Parking Reservation");

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                //pulling information from Firebase to display on page for confirmation
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        recentTransactionCard.setVisibility(View.VISIBLE);
                        String price = snapshot.child("Price").getValue(String.class);
                        String location = snapshot.child("Parking Location").getValue(String.class);
                        String license = snapshot.child("License Plate").getValue(String.class);
                        String startDate = snapshot.child("Start Date").getValue(String.class);
                        String startTime = snapshot.child("Start Time").getValue(String.class);
                        String endDate = snapshot.child("End Date").getValue(String.class);
                        String endTime = snapshot.child("End Time").getValue(String.class);

                        textViewPrice.setText(price);
                        textViewLocation.setText(location);
                        textViewLicense.setText(license);
                        textViewSD.setText(startDate);
                        textViewST.setText(startTime);
                        textViewED.setText(endDate);
                        textViewET.setText(endTime);

                    }
                    else{
                        recentTransactionCard.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //log out button
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authProfile.signOut();
                Toast.makeText(UserProfileActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });



        if(firebaseUser == null){
            Toast.makeText(UserProfileActivity.this, "Something went wrong, details not available at the moment", Toast.LENGTH_SHORT).show();
        }
        else{
            showUserProfile(firebaseUser);
        }
    }

    //displays user's basic information
    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //Extracting User Reference from Database
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails != null){
                    name = readUserDetails.name;
                    email = firebaseUser.getEmail();
                    gwid = readUserDetails.gwid;

                    textViewWelcome.setText("Welcome, " + name + "!");
                    textViewName.setText(name);
                    textViewEmail.setText(email);
                    textViewGWID.setText(gwid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

            }
        });


    }
}