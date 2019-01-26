package ru.klepovnikita.billconvert;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JSONPlaceHolderApi {
    @GET("api/v6/convert")
    public Call<JsonElement> getPostWithID(@Query("q") String bills);

    @GET("api/v6/convert")
    public Call<JsonObject> readJsonFromFileUri(@Query("q") String bills, @Query("compact") String compact);

}
