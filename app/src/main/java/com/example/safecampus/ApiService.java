package com.example.safecampus;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    // 1. LOGIN (Sends username & password, expects a response with Real Name)
    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );

    // 2. REGISTER (Sends the UserRegisterModel we just made)
    @POST("register.php")
    Call<ServerResponse> registerUser(@Body UserRegisterModel user);

    // 3. GET MAP MARKERS (Downloads list of clinics/posts)
    @GET("get_locations.php")
    Call<List<LocationModel>> getLocations();

    // 4. SUBMIT REPORT (Sends the ReportModel)
    @POST("submit_report.php")
    Call<ServerResponse> submitReport(@Body ReportModel report);
}