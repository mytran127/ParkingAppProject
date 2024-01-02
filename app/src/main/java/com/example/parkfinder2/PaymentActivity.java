package com.example.parkfinder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    //variables
    TextView total, location;
    TextView licensePlateEditText;

    Button submit;

    ImageButton back;
    private FirebaseAuth authProfile;
    String licensePlateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //linking variables to elements

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();


        licensePlateEditText = findViewById(R.id.licensePlate);

        submit = findViewById(R.id.submitPayment);
        back = (ImageButton) findViewById(R.id.backbutton);
        total = findViewById(R.id.total);
        location = findViewById(R.id.location);
        double number = 0;
        //format currency for price
        Locale locale = Locale.getDefault();

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        String formattedCurrency = currencyFormat.format(number);

        total.setText(formattedCurrency);

        //display the location and price from Firebase

        if(firebaseUser != null){
            String userId = firebaseUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String price = snapshot.child("Parking Reservation").child("Price").getValue(String.class);
                        String locationPull = snapshot.child("Parking Reservation").child("Parking Location").getValue(String.class);

                        total.setText(price);
                        location.setText(locationPull);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(PaymentActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //back button goes to previous page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PaymentActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        //license plat number input is saved

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                licensePlateString = licensePlateEditText.getText().toString();
               if(TextUtils.isEmpty(licensePlateString)){
                    licensePlateEditText.setError("This is required");
                    Toast.makeText(PaymentActivity.this, "Please enter License Plate", Toast.LENGTH_SHORT).show();

                }
                else{

                    if(firebaseUser!= null){
                        String userId = firebaseUser.getUid();

                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId);

                        userRef.child("Parking Reservation").child("License Plate").setValue(licensePlateString);

                    }

                    Toast.makeText(PaymentActivity.this, "Payment Successful!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(PaymentActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                }



            }
        });



    }


}