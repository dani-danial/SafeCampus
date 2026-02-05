package com.example.safecampus;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MapPickerActivity extends AppCompatActivity {

    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load OSM Configuration
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_map_picker);

        map = findViewById(R.id.map);
        Button btnConfirm = findViewById(R.id.btnConfirmLocation);

        // Setup Map
        map.setMultiTouchControls(true);
        map.getController().setZoom(18.0);

        // Default Start Location (e.g., Campus Center)
        // You can change this to your specific campus coordinates
        GeoPoint startPoint = new GeoPoint(2.2215, 102.4533);
        map.getController().setCenter(startPoint);

        btnConfirm.setOnClickListener(v -> {
            // Get the coordinates of the CENTER of the map
            GeoPoint center = (GeoPoint) map.getMapCenter();

            // Send result back to ReportActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("pickedLat", center.getLatitude());
            resultIntent.putExtra("pickedLng", center.getLongitude());
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }
}