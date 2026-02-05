package com.example.safecampus;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // ⚠️ CHECK THIS IP: Make sure it is your Laptop A's IPv4 Address
    private static final String BASE_URL = "http://10.0.2.2/safecampus/";

    private static Retrofit retrofit = null;

    // FIX: The return type must be 'Retrofit', not 'ApiService'
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}