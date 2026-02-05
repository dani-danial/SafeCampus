package com.example.safecampus;

import android.os.Bundle;
import android.widget.Button; // Import added
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // 1. Enable the "Back" arrow in the top toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("About Us");
        }

        // 2. Make the "Back to Dashboard" button work
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish(); // Closes About page and returns to Main Map
        });
    }

    // Handle the Top Toolbar "Back" arrow click
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}