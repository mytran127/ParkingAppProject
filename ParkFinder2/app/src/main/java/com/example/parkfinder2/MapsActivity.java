package com.example.parkfinder2;

import androidx.fragment.app.FragmentActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.Button;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // variables
    private GoogleMap mMap;
    ImageButton profile;

    private FirebaseAuth authProfile;

    TextView address1Text , address2Text, address3Text, address4Text, price1Text, price2Text, price3Text, price4Text;

    String address1String , address2String, address3String, address4String, price1String, price2String, price3String, price4String;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //associating variables with elements

        profile = findViewById(R.id.profileButton);
        address1Text = findViewById(R.id.address1);
        address2Text = findViewById(R.id.address2);
        address3Text = findViewById(R.id.address3);
        address4Text = findViewById(R.id.address4);
        price1Text = findViewById(R.id.price1);
        price2Text = findViewById(R.id.price2);
        price3Text = findViewById(R.id.price3);
        price4Text = findViewById(R.id.price4);

        //converting variable values to a String
        address1String = address1Text.getText().toString();
        address2String = address2Text.getText().toString();
        address3String = address3Text.getText().toString();
        address4String = address4Text.getText().toString();
        price1String = price1Text.getText().toString();
        price2String = price2Text.getText().toString();
        price3String = price3Text.getText().toString();
        price4String = price4Text.getText().toString();

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        //link to user profile page

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Selecting parking spots


        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        final ImageButton button1 = (ImageButton) findViewById(R.id.parking1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                LatLng parking1 = new LatLng(38.897080, -77.051370);
                mMap.addMarker(new MarkerOptions().position(parking1));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(parking1.latitude, parking1.longitude), 18.0f));

            }

        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        final ImageButton button2 = (ImageButton) findViewById(R.id.parking2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                LatLng parking2 = new LatLng(38.902130, -77.048800);
                mMap.addMarker(new MarkerOptions().position(parking2));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(parking2.latitude, parking2.longitude), 18.0f));

            }

        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        final ImageButton button3 = (ImageButton) findViewById(R.id.parking3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                LatLng parking3 = new LatLng(38.902290, -77.051980);
                mMap.addMarker(new MarkerOptions().position(parking3));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(parking3.latitude, parking3.longitude), 18.0f));

            }

        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        final ImageButton button4 = (ImageButton) findViewById(R.id.parking4);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                LatLng parking4 = new LatLng(38.904525, -77.048887);
                mMap.addMarker(new MarkerOptions().position(parking4));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(parking4.latitude, parking4.longitude), 18.0f));

            }

        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        final ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        //saves parking spot info into Firebase


        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        final Button bookButton = (Button) findViewById(R.id.bookButton);
        bookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(firebaseUser!= null){
                    String userId = firebaseUser.getUid();

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId);

                    userRef.child("Parking Reservation").child("Parking Location").setValue(address1String);
                    userRef.child("Parking Reservation").child("Price").setValue(price1String);

                }

                Intent intent = new Intent(MapsActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        final Button bookButton2 = (Button) findViewById(R.id.bookButton2);
        bookButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(firebaseUser!= null){
                    String userId = firebaseUser.getUid();

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId);

                    userRef.child("Parking Reservation").child("Parking Location").setValue(address2String);
                    userRef.child("Parking Reservation").child("Price").setValue(price2String);

                }

                Intent intent = new Intent(MapsActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        final Button bookButton3 = (Button) findViewById(R.id.bookButton3);
        bookButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(firebaseUser!= null){
                    String userId = firebaseUser.getUid();

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId);

                    userRef.child("Parking Reservation").child("Parking Location").setValue(address3String);
                    userRef.child("Parking Reservation").child("Price").setValue(price3String);

                }

                Intent intent = new Intent(MapsActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        final Button bookButton4 = (Button) findViewById(R.id.bookButton4);
        bookButton4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(firebaseUser!= null){
                    String userId = firebaseUser.getUid();

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId);

                    userRef.child("Parking Reservation").child("Parking Location").setValue(address4String);
                    userRef.child("Parking Reservation").child("Price").setValue(price4String);

                }

                Intent intent = new Intent(MapsActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near George Washington University.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in GWU and move the camera
        LatLng gwu = new LatLng(38.89736557006836, -77.0500717163086);
        mMap.addMarker(new MarkerOptions().position(gwu));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gwu));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gwu.latitude, gwu.longitude), 13.0f));
    }
}