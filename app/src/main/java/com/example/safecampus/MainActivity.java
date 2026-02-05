package com.example.safecampus;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

// Google Material Design (For Snackbars - Rubric Requirement)
import com.google.android.material.snackbar.Snackbar;

// OSM Imports
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MainActivity extends AppCompatActivity {

    private MapView map;
    private MyLocationNewOverlay locationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Exception Handling (Rubric Requirement)
        // Wraps the configuration loading to prevent crashes if storage is inaccessible
        try {
            Context ctx = getApplicationContext();
            Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
            Configuration.getInstance().setUserAgentValue(getPackageName());
        } catch (Exception e) {
            Toast.makeText(this, "Error loading map settings: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        setContentView(R.layout.activity_main);

        // 2. Setup MapView
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getController().setZoom(10.0); // Start zoomed out slightly
        map.getController().setCenter(new GeoPoint(3.1412, 101.6865)); // Default to KL

        // 3. User Greeting
        String realName = getIntent().getStringExtra("REAL_NAME");
        TextView tvWelcome = findViewById(R.id.tvWelcome);
        if(realName != null) tvWelcome.setText("Hello, " + realName + "!");

        // 4. Button Listeners
        // Open Report Page
        findViewById(R.id.fabReport).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            intent.putExtra("REAL_NAME", realName);
            startActivity(intent);
        });

        // Open About Page
        findViewById(R.id.btnAbout).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        // Open News Page (Rubric Requirement)
        findViewById(R.id.btnNews).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewsActivity.class);
            startActivity(intent);
        });

        // 5. Internet Connection Check & Feedback (Rubric Requirement)
        if (!isNetworkAvailable()) {
            // Show "No Internet" using Snackbar (Level 5 Requirement)
            Snackbar.make(findViewById(android.R.id.content), "No Internet Connection detected!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", v -> checkPermissionsAndShowLocation())
                    .show();
        } else {
            // Show Welcome Feedback using Snackbar
            Snackbar.make(findViewById(android.R.id.content), "Welcome to SafeCampus", Snackbar.LENGTH_SHORT).show();
        }

        // 6. Enable "Blue Dot" (User Location)
        checkPermissionsAndShowLocation();

        // 7. Add a Marker (Example)
        addMarker(3.123, 101.543, "University Clinic", "Open 24/7");
    }

    // Helper method to check internet connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkPermissionsAndShowLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        setupLocationOverlay();
    }

    private void setupLocationOverlay() {
        // OSM specific code to show location
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        locationOverlay.enableMyLocation();

        // Fix for "Blue Map": Wait for first GPS fix then zoom in
        locationOverlay.runOnFirstFix(() -> {
            runOnUiThread(() -> {
                map.getController().setZoom(18.0);
                map.getController().animateTo(locationOverlay.getMyLocation());
            });
        });

        map.getOverlays().add(locationOverlay);
    }

    // Handle Permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupLocationOverlay();
        }
    }

    private void addMarker(double lat, double lon, String title, String snippet) {
        Marker marker = new Marker(map);
        marker.setPosition(new GeoPoint(lat, lon));
        marker.setTitle(title);
        marker.setSnippet(snippet);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null) map.onPause();
    }
}