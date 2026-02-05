package com.example.safecampus;

public class LoginResponse {
    private String status;    // "success" or "error"
    private String real_name; // The name from the database (e.g., "Aisyah")
    private String message;   // Error message if any

    public String getStatus() {
        return status;
    }

    public String getRealName() {
        return real_name;
    }

    public String getMessage() {
        return message;
    }
}