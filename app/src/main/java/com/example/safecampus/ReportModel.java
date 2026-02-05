package com.example.safecampus;

public class ReportModel {
    private String username;
    private String type;
    private String desc;
    private double lat;
    private double lng;
    private String time;

    public ReportModel(String username, String type, String desc, double lat, double lng, String time) {
        this.username = username;
        this.type = type;
        this.desc = desc;
        this.lat = lat;
        this.lng = lng;
        this.time = time;
    }
}