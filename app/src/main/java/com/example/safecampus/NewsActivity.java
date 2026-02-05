package com.example.safecampus;

import android.os.Bundle;
import android.widget.Button; // Import added
import androidx.appcompat.app.AppCompatActivity;

public class NewsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // 1. Enable the small "Back Arrow" in the top toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Campus News");
        }

        // 2. Make the big "Back to Dashboard" button work
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // Closing this activity returns the user to the previous screen (Main Dashboard)
            finish();
        });
    }

    // This handles the Top Toolbar Back Arrow click
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}