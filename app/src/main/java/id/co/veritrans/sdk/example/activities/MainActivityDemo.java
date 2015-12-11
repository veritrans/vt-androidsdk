package id.co.veritrans.sdk.example.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.example.R;
import id.co.veritrans.sdk.example.gcmutils.RegistrationIntentService;
import id.co.veritrans.sdk.example.utils.Constants;

/**
 * this is an example application,
 * created to show how developers can use veritrans sdk to perform transaction.
 *
 */
public class MainActivityDemo extends AppCompatActivity implements View.OnClickListener {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String TAG = MainActivityDemo.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        startActivity(new Intent(MainActivityDemo.this, CartActivity.class));
        finish();
        setContentView(R.layout.activity_main);

        Button coreFlow = (Button) findViewById(R.id.btn_core_flow);
        coreFlow.setOnClickListener(this);

        Button uiFlow = (Button) findViewById(R.id.btn_ui_flow);
        uiFlow.setOnClickListener(this);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(Constants.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Logger.i("gcm token to server");
                } else {
                    Logger.i("gcm token not sent to server");
                }
            }
        };
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btn_core_flow){

            startActivity(new Intent(MainActivityDemo.this, CoreFlowActivity.class));

        }else if(view.getId() == R.id.btn_ui_flow){
            startActivity(new Intent(MainActivityDemo.this, UiFlowActivity.class));
        }

    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}