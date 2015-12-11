package id.co.veritrans.sdk.example;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import id.co.veritrans.sdk.core.VeritransBuilder;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.example.utils.Constants;
import io.fabric.sdk.android.Fabric;

/**
 * Created by chetan on 17/11/15.
 */
public class VeritransExampleApp extends Application {

    private VeritransSDK mVeritransSDK = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
        initializeSdk();
       // broadcastPushTest();
    }


    private void initializeSdk() {
        // sdk initialization process
        VeritransBuilder veritransBuilder = new
                VeritransBuilder(getApplicationContext(), Constants.VT_CLIENT_KEY, Constants.BASE_URL_MERCHANT_FOR_DEBUG);
        veritransBuilder.enableLog(true);
        mVeritransSDK = veritransBuilder.buildSDK();
    }


    public VeritransSDK getVeritransSDK() {
        return mVeritransSDK;
    }


}