package com.example.safecampus;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Firebase Imports
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Sign Up");
        }

        EditText etName = findViewById(R.id.etRegRealName);
        EditText etEmail = findViewById(R.id.etRegUsername); // We use Username input as Email
        EditText etPass = findViewById(R.id.etRegPassword);
        Button btnSubmit = findViewById(R.id.btnRegisterSubmit);

        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPass.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. Create User in Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // 2. Save User Details to Firestore Database
                            String userId = mAuth.getCurrentUser().getUid();
                            Map<String, Object> user = new HashMap<>();
                            user.put("fullName", name);
                            user.put("email", email);
                            user.put("role", "user"); // Default role

                            db.collection("users").document(userId).set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_LONG).show();
                                        finish(); // Go back to login
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(RegisterActivity.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}