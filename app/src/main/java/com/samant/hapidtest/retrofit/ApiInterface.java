package com.samant.hapidtest.retrofit;


import com.google.gson.JsonObject;
import com.samant.hapidtest.model.createProfileModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {



    @POST("/api/login")
    Call<createProfileModel> getCreateprofile(@Body JsonObject postJson);



}
