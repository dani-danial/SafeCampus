package com.example.safecampus;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<NewsItem> newsList;
    private FirebaseFirestore db;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        recyclerView = findViewById(R.id.recyclerNews);
        tvEmpty = findViewById(R.id.tvEmpty);
        Button btnBack = findViewById(R.id.btnBack); // Link Button

        // ✅ Back Button Logic
        btnBack.setOnClickListener(v -> finish());

        // Setup Recycler View
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsList = new ArrayList<>();
        adapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(adapter);

        // Setup Firebase
        db = FirebaseFirestore.getInstance();

        // Start Listening
        listenToNews();
    }

    private void listenToNews() {
        db.collection("notifications")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(50)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("NewsActivity", "Listen failed.", error);
                        return;
                    }

                    if (value != null) {
                        newsList.clear();
                        for (com.google.firebase.firestore.DocumentSnapshot doc : value.getDocuments()) {
                            String title = doc.getString("title");
                            String message = doc.getString("message");
                            String type = doc.getString("type");
                            String createdBy = doc.getString("createdBy");

                            long createdAt = 0;
                            Timestamp ts = doc.getTimestamp("createdAt");
                            if (ts != null) createdAt = ts.toDate().getTime();

                            newsList.add(new NewsItem(title, message, type, createdAt, createdBy));
                        }

                        adapter.notifyDataSetChanged();

                        if (newsList.isEmpty()) {
                            tvEmpty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            tvEmpty.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}