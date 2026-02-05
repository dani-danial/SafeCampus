package com.example.safecampus;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

// Firebase Imports
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private TextView tvLocation;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private double currentLat = 0.0;
    private double currentLng = 0.0;
    private boolean isLocationSet = false; // Check if we have a valid location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Spinner spinner = findViewById(R.id.spinnerType);
        EditText etDescription = findViewById(R.id.etDescription);
        tvLocation = findViewById(R.id.tvLocation);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnPickMap = findViewById(R.id.btnPickMap); // NEW BUTTON

        String[] types = {"Accident", "Crime", "Damaged Facilities", "Suspicious Activity", "Fire Hazard"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Auto-get current location on start
        getCurrentLocation();

        // ✅ HANDLE MAP PICKER RESULT
        ActivityResultLauncher<Intent> mapPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        currentLat = result.getData().getDoubleExtra("pickedLat", 0.0);
                        currentLng = result.getData().getDoubleExtra("pickedLng", 0.0);
                        isLocationSet = true;
                        updateLocationUI("Pinned on Map");
                    }
                }
        );

        // Open Map Picker when clicked
        btnPickMap.setOnClickListener(v -> {
            Intent intent = new Intent(ReportActivity.this, MapPickerActivity.class);
            mapPickerLauncher.launch(intent);
        });

        btnSubmit.setOnClickListener(v -> {
            String desc = etDescription.getText().toString().trim();
            String type = spinner.getSelectedItem().toString();

            if (TextUtils.isEmpty(desc)) {
                etDescription.setError("Required");
                return;
            }
            if (!isLocationSet) {
                Snackbar.make(v, "Please select a location first!", Snackbar.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> report = new HashMap<>();
            report.put("type", type);
            report.put("description", desc);
            report.put("latitude", currentLat);
            report.put("longitude", currentLng);
            report.put("timestamp", new Date());
            report.put("status", "Pending");

            FirebaseUser currentUser = mAuth.getCurrentUser();
            report.put("reporter_email", currentUser != null ? currentUser.getEmail() : "Anonymous");

            db.collection("reports").add(report)
                    .addOnSuccessListener(doc -> {
                        Snackbar.make(v, "Report Submitted!", Snackbar.LENGTH_LONG).show();
                        v.postDelayed(this::finish, 1500);
                    })
                    .addOnFailureListener(e -> Snackbar.make(v, "Error: " + e.getMessage(), Snackbar.LENGTH_SHORT).show());
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    currentLat = location.getLatitude();
                    currentLng = location.getLongitude();
                    isLocationSet = true;
                    updateLocationUI("Current GPS");
                }
            });
        }
    }

    private void updateLocationUI(String source) {
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        tvLocation.setText("Source: " + source + "\nLat: " + String.format("%.5f", currentLat) + "\nLon: " + String.format("%.5f", currentLng));
    }
}