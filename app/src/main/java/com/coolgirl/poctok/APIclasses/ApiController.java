package com.coolgirl.poctok.APIclasses;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiController {
    @Headers({"Accept: application/json"})
    @GET("user/autorize/{login}&{password}")
    Call<UserLoginData> autorizeUser(@Path("login")String login, @Path("password") String password);

    @Headers("Accept: application/json")
    @POST("user/postuser")
    Call<UserLoginData> createUser(@Body UserLoginData user);

    @Headers("Accept: application/json")
    @GET("user/getprofiledata/{id}")
    Call<UserLoginData> getUserProfileData(@Path("id")int id);

    @Headers("Accept: application/json")
    @GET("user/getplantdata/{id}")
    Call<Plant> getPlantProfileData(@Path("id")int id);


    @Headers("Accept: application/json")
    @PUT("user/postnote")
    Call<Notes> postNote(@Body Notes note);

    @Headers("Accept: application/json")
    @PUT("user/postplant")
    Call<Plant> postPlant(@Body Plant plant);


}