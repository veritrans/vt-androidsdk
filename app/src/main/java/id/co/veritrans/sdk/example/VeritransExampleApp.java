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
        /*if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }*/
        initializeSdk();
       // broadcastPushTest();
    }

  /*  public void broadcastPushTest() {
        Intent intent = new Intent("com.google.android.gms.gcm.GcmReceiver");
        String jsonString = "{\n" +
                " \"status_code\" : \"201\",\n" +
                " \"status_message\" : \"Success, CIMB Clicks transaction is successful\",\n" +
                " \"transaction_id\" : \"d5f2d082-ce28-4fd1-a999-9a85bc566a9e\",\n" +
                " \"order_id\" : \"a8572607d8\",\n" +
                " \"gross_amount\" : \"100.00\",\n" +
                " \"payment_type\" : \"cimb_clicks\",\n" +
                " \"transaction_time\" : \"2015-12-01 14:40:01\",\n" +
                " \"transaction_status\" : \"pending\"\n" +
                "}";
        intent.putExtra("message",jsonString);
        Logger.i("broadcast:"+jsonString);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long now = System.currentTimeMillis();
        long interval = 10 * 1000;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, now + interval, interval,
                pendingIntent);
    }*/

    /**
     * initialize veritrans sdk at the beginning of the application.
     */
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