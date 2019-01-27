package ru.klepovnikita.billconvert;

import com.google.gson.JsonObject;

import dimitrovskif.smartcache.SmartCall;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyAPI {
    @GET("api/v6/convert")
    SmartCall<JsonObject> readJsonFromFileUri(@Query("q") String bills, @Query("compact") String compact);
}


