package com.example.safecampus;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsList;

    public NewsAdapter(List<NewsItem> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem item = newsList.get(position);

        holder.titleView.setText(item.title);
        holder.messageView.setText(item.message);
        holder.typeView.setText(item.type != null ? item.type.toUpperCase() : "INFO");
        holder.authorView.setText("By " + item.createdBy);

        // Format Date
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        holder.dateView.setText(sdf.format(new Date(item.createdAt)));

        // Color Coding
        if ("emergency".equalsIgnoreCase(item.type)) {
            holder.typeView.setBackgroundColor(Color.parseColor("#D32F2F")); // Red
        } else if ("alert".equalsIgnoreCase(item.type)) {
            holder.typeView.setBackgroundColor(Color.parseColor("#F57C00")); // Orange
        } else {
            holder.typeView.setBackgroundColor(Color.parseColor("#388E3C")); // Green
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleView, messageView, typeView, authorView, dateView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.news_title);
            messageView = itemView.findViewById(R.id.news_message);
            typeView = itemView.findViewById(R.id.news_type);
            authorView = itemView.findViewById(R.id.news_author);
            dateView = itemView.findViewById(R.id.news_date);
        }
    }
}