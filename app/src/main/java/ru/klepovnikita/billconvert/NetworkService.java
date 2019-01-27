package ru.klepovnikita.billconvert;

import android.content.Context;

import dimitrovskif.smartcache.BasicCaching;
import dimitrovskif.smartcache.SmartCallFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


class NetworkService {
    private static NetworkService mInstance;
    private static final String BASE_URL = "http://free.currencyconverterapi.com";
    private Retrofit mRetrofit;


    private NetworkService(Context mContext){

        SmartCallFactory smartFactory = new SmartCallFactory(BasicCaching.fromCtx(mContext));

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(smartFactory)
                .build();
        }

    static NetworkService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkService(context);
        }
        return mInstance;
    }

    CurrencyAPI getJSONApi() {
        return mRetrofit.create(CurrencyAPI.class);
    }

}
