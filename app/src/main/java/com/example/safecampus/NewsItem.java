package com.example.safecampus;

public class NewsItem {
    public String title;
    public String message;
    public String type; // "alert", "announcement", "emergency"
    public long createdAt;
    public String createdBy;

    // Empty constructor needed for Firebase
    public NewsItem() {}

    public NewsItem(String title, String message, String type, long createdAt, String createdBy) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }
}