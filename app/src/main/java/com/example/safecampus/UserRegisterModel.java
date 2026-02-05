package com.example.safecampus;

public class UserRegisterModel {
    private String username;
    private String password;
    private String real_name; // Must match the JSON key in your PHP script

    public UserRegisterModel(String username, String password, String real_name) {
        this.username = username;
        this.password = password;
        this.real_name = real_name;
    }
}