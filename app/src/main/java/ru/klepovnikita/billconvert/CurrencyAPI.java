package ru.klepovnikita.billconvert;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyAPI {
    @GET("api/v6/convert")
    public Call<JsonObject> readJsonFromFileUri(@Query("q") String bills, @Query("compact") String compact);
}
