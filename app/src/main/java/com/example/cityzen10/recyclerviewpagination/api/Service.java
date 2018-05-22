package com.example.cityzen10.recyclerviewpagination.api;

import com.example.cityzen10.recyclerviewpagination.model.PersonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {


    @GET("api/users")
    Call<PersonResponse> getUsers(@Query("page") int pageIndex);

}
