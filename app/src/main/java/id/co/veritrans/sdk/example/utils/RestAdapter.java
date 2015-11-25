package id.co.veritrans.sdk.example.utils;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.squareup.okhttp.OkHttpClient;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

import id.co.veritrans.sdk.BuildConfig;
import id.co.veritrans.sdk.example.model.ApiInterface;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by SHIVAM on 11/23/2015.
 */
public class RestAdapter {


    public static ApiInterface getMerchantApiClient(final Context activity,
                                                    boolean showNetworkNotAvailableDialog) {

        ApiInterface apiInterface = null;

        if (id.co.veritrans.sdk.utilities.Utils.isNetworkAvailable(activity)) {

            if (apiInterface == null) {
                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);

                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                        .registerTypeAdapter(Date.class, new DateTypeAdapter())
                        .create();

                retrofit.RestAdapter.Builder builder = new retrofit.RestAdapter.Builder()
                        .setConverter(new GsonConverter(gson))
                        .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                        .setClient(new OkClient(okHttpClient));

                retrofit.RestAdapter restAdapter;


                if (BuildConfig.DEBUG) {
                    builder.setEndpoint(Constants.BASE_URL_MERCHANT_FOR_DEBUG);
                    restAdapter = builder.build();
                    restAdapter.setLogLevel(retrofit.RestAdapter.LogLevel.FULL);

                } else {
                    builder.setEndpoint(Constants.BASE_URL_MERCHANT_FOR_RELEASE);
                    restAdapter = builder.build();
                }

                apiInterface = restAdapter.create(ApiInterface.class);
            }

            return apiInterface;

        }
        return null;

    }
}
