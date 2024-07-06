package com.example.datatransferprojectedited.retrofit;

import com.example.datatransferprojectedited.model.Root;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {
    @GET("employees")
    Call<Root> getAllUsers();
}
