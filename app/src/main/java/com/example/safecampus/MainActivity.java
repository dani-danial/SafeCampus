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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

// Google Material Design
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// Google Location Services (For Fast Initial Zoom)
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

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
    private FusedLocationProviderClient fusedLocationClient; // Added for fast zooming

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. OSM Configuration (Prevents crashing on load)
        try {
            Context ctx = getApplicationContext();
            Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
            Configuration.getInstance().setUserAgentValue(getPackageName());
        } catch (Exception e) {
            Toast.makeText(this, "Error loading map config: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        setContentView(R.layout.activity_main);

        // 2. Initialize Views & Tools
        map = findViewById(R.id.map);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // 3. Setup Map Settings
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getController().setZoom(18.0); // Start zoomed in
        // Default start point (will be overwritten by GPS instantly)
        map.getController().setCenter(new GeoPoint(3.1412, 101.6865));

        // 4. User Greeting
        String realName = getIntent().getStringExtra("REAL_NAME");
        TextView tvWelcome = findViewById(R.id.tvWelcome);
        if (realName != null) {
            tvWelcome.setText("Hello, " + realName + "!");
        }

        // 5. Button Listeners
        // Report Button (Floating Action Button)
        findViewById(R.id.fabReport).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            startActivity(intent);
        });

        // About Button
        findViewById(R.id.btnAbout).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        // News Button
        findViewById(R.id.btnNews).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewsActivity.class);
            startActivity(intent);
        });

        // 6. Internet Check
        if (!isNetworkAvailable()) {
            Snackbar.make(findViewById(android.R.id.content), "No Internet Connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", v -> checkPermissionsAndShowLocation())
                    .show();
        }

        // 7. START LOCATION LOGIC
        checkPermissionsAndShowLocation();

        // 8. Add Example Marker (e.g., Campus Clinic)
        addMarker(2.2215, 102.4533, "UiTM Jasin", "Main Campus Area");
    }

    // Helper: Check Internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Permission Check
    private void checkPermissionsAndShowLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Ask for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // Permission already granted
            startLocationLogic();
        }
    }

    // Main Location Logic
    private void startLocationLogic() {
        // A. Setup the "Blue Dot" Overlay (Live Tracking)
        setupLocationOverlay();

        // B. Force immediate zoom to last known location (Fixes the "Google HQ" startup delay)
        getFastLocation();
    }

    private void setupLocationOverlay() {
        locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        locationOverlay.enableMyLocation();
        map.getOverlays().add(locationOverlay);

        // Fallback: If FastLocation fails, this will zoom when GPS finally warms up
        locationOverlay.runOnFirstFix(() -> {
            runOnUiThread(() -> {
                if (map != null) {
                    map.getController().animateTo(locationOverlay.getMyLocation());
                }
            });
        });
    }

    private void getFastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            // Immediately jump here without waiting for the Overlay to initialize
                            GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                            map.getController().setCenter(startPoint);
                            map.getController().setZoom(18.5); // Close up zoom
                        }
                    });
        }
    }

    // Handle Permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationLogic();
        } else {
            Toast.makeText(this, "Permission Denied. Map will not show your location.", Toast.LENGTH_SHORT).show();
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
        if (locationOverlay != null) locationOverlay.enableMyLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null) map.onPause();
        if (locationOverlay != null) locationOverlay.disableMyLocation();
    }
}