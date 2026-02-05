package com.example.safecampus;

public class LocationModel {
    private String name;
    private String type; // "Security", "Clinic", etc.
    private double latitude;
    private double longitude;

    // Constructor
    public LocationModel(String name, String type, double latitude, double longitude) {
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public String getName() { return name; }
    public String getType() { return type; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}