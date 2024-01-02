package com.example.parkfinder2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MainActivity2 extends AppCompatActivity {

// variables
    Button findParkingButton;


    TextView tvStartDate, tvStartTime, tvEndTime, tvEndDate;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    ImageView topLogo;
    private FirebaseAuth authProfile;

    String startTimeString2, endTimeString2, startDateString2, endDateString2;




    private boolean isValidTimeRange() {
        try {
            String startTimeString = tvStartTime.getText().toString();
            String endTimeString = tvEndTime.getText().toString();
            String startDateString = tvStartDate.getText().toString();
            String endDateString = tvEndDate.getText().toString();
            // Parse Start Time
            String[] startParts = startTimeString.split(":");
            int startHour = Integer.parseInt(startParts[0]);
            int startMinute = Integer.parseInt(startParts[1]);

            // Parse End Time
            String[] endParts = endTimeString.split(":");
            int endHour = Integer.parseInt(endParts[0]);
            int endMinute = Integer.parseInt(endParts[1]);

            // Parse Start Date
            String[] startDateParts = startDateString.split("/");
            int startDay = Integer.parseInt(startDateParts[0]);
            int startMonth = Integer.parseInt(startDateParts[1]) - 1; // Month is 0-based
            int startYear = Integer.parseInt(startDateParts[2]);

            // Parse End Date
            String[] endDateParts = endDateString.split("/");
            int endDay = Integer.parseInt(endDateParts[0]);
            int endMonth = Integer.parseInt(endDateParts[1]) - 1; // Month is 0-based
            int endYear = Integer.parseInt(endDateParts[2]);

            // Create Calendar instances
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.set(startYear, startMonth, startDay, startHour, startMinute);

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.set(endYear, endMonth, endDay, endHour, endMinute);

            // Check if end is after start
            return endCalendar.after(startCalendar);
        } catch (Exception e) {
            // In case of any parsing error
            return false;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //initialize and associating variables with elements

        findParkingButton = findViewById(R.id.parking);

        tvStartDate = findViewById(R.id.text_start_date);
        tvEndDate = findViewById(R.id.text_end_date);
        tvStartTime = findViewById(R.id.text_start_time);
        tvEndTime = findViewById(R.id.text_end_time);
        topLogo = findViewById(R.id.toplogo);


        //link to user profile activity
        topLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        //selecting start time

        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                /*
                timePickerDialog = new TimePickerDialog(MainActivity2.this,
                        (view, hourOfDay, minutes) ->
                                tvStartTime.setText(String.format("%02d:%02d", hourOfDay, minutes)),
                        hour, minute, false);
                timePickerDialog.show();

                 */

                timePickerDialog = new TimePickerDialog(MainActivity2.this,
                        (view, hourOfDay, minutes) -> {
                            startTimeString2 = String.format("%02d:%02d", hourOfDay, minutes);
                            tvStartTime.setText(startTimeString2);
                        },
                        hour, minute, false);
                timePickerDialog.show();
            }
        });
        //selecting start date
        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                /*
                datePickerDialog = new DatePickerDialog(MainActivity2.this,
                        (view, year, monthOfYear, dayOfMonth) -> {
                                tvStartDate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year),
                        mYear, mMonth, mDay);
                datePickerDialog.show();

                 */
                datePickerDialog = new DatePickerDialog(MainActivity2.this,
                        (view, year, monthOfYear, dayOfMonth) -> {
                            startDateString2 = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            tvStartDate.setText(startDateString2);
                        },
                        mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        //selecting end date

        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                /*
                datePickerDialog = new DatePickerDialog(MainActivity2.this,
                        (view, year, monthOfYear, dayOfMonth) ->
                                tvEndDate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year),
                        mYear, mMonth, mDay);
                datePickerDialog.show();

                 */

                datePickerDialog = new DatePickerDialog(MainActivity2.this,
                        (view, year, monthOfYear, dayOfMonth) -> {
                            endDateString2 = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            tvEndDate.setText(endDateString2);
                        },
                        mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        //selecting end time

        tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                /*
                timePickerDialog = new TimePickerDialog(MainActivity2.this,
                        (view, hourOfDay, minutes) ->
                                tvEndTime.setText(String.format("%02d:%02d", hourOfDay, minutes)),
                        hour, minute, false);
                timePickerDialog.show();

                 */
                timePickerDialog = new TimePickerDialog(MainActivity2.this,
                        (view, hourOfDay, minutes) -> {
                            endTimeString2 = String.format("%02d:%02d", hourOfDay, minutes);
                            tvEndTime.setText(endTimeString2);
                        },
                        hour, minute, false);
                timePickerDialog.show();
            }
        });

        //submiting times and dates to choose parking spot

        findParkingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //check invalid time/date range
                if (!isValidTimeRange()) {
                    Toast.makeText(MainActivity2.this, "Invalid date and time range. Please check your start and end times.", Toast.LENGTH_SHORT).show();
                    return;
                }

                authProfile = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = authProfile.getCurrentUser();

                //adding times and dates to Firebase
                if(firebaseUser!= null){
                    String userId = firebaseUser.getUid();

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Registered Users").child(userId);

                    userRef.child("Parking Reservation").child("Start Time").setValue(startTimeString2);
                    userRef.child("Parking Reservation").child("Start Date").setValue(startDateString2);
                    userRef.child("Parking Reservation").child("End Time").setValue(endTimeString2);
                    userRef.child("Parking Reservation").child("End Date").setValue(endDateString2);
                }

                //moving on to Maps Activity
                Intent intent = new Intent(MainActivity2.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }


}