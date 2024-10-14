package com.idragonpro.andmagnus.api;

import com.idragonpro.andmagnus.models.GetRazorOrderIDRequest;
import com.idragonpro.andmagnus.responseModels.RazorOrderIdResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitWebApi {

    @POST("v1/orders")
    Call<RazorOrderIdResponse> createRetrofitOrder(@Header("Authorization") String authHeader, @Body GetRazorOrderIDRequest request);

}
