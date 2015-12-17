package id.co.veritrans.sdk.example.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;

import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.TransactionRequest;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.example.R;
import id.co.veritrans.sdk.example.VeritransExampleApp;
import id.co.veritrans.sdk.example.gcmutils.RegistrationIntentService;
import id.co.veritrans.sdk.example.utils.Constants;
import id.co.veritrans.sdk.example.utils.Utils;
import id.co.veritrans.sdk.models.BBMCallBackUrl;
import id.co.veritrans.sdk.models.BillInfoModel;
import id.co.veritrans.sdk.models.ItemDetails;
import id.co.veritrans.sdk.models.PaymentMethodsModel;
import id.co.veritrans.sdk.models.TransactionResponse;

public class CartActivity extends AppCompatActivity {

    private static final int OPTION_CON = 500;
    private static final String TAG = "CartActivity";
    private VeritransSDK veritransSDK;
    private ArrayList<PaymentMethodsModel> selectedPaymentMethods;
    private Button payBtn;
    private int amount = Constants.AMOUNT;
    private String clickType = id.co.veritrans.sdk.core.Constants.CARD_CLICK_TYPE_NONE;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private TransactionRequest transactionRequest = null;
    private BBMCallBackUrl bbmCallBackUrl = null;
    private boolean isSecure = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cart");
        veritransSDK =  ((VeritransExampleApp) getApplication()).getVeritransSDK();
        selectedPaymentMethods = Utils.initialiseAdapterData(this);
        if (veritransSDK != null) {
            veritransSDK.setSelectedPaymentMethods(selectedPaymentMethods);
        }
        payBtn = (Button) findViewById(R.id.btn_payment);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(transactionRequest != null ) {

                    TransactionRequest transactionRequestNew = new
                            TransactionRequest(CartActivity.this, Utils.generateOrderId(), amount);


                    BBMCallBackUrl bbmCallBackUrl = new BBMCallBackUrl(Constants.CHECK_STATUS,
                            Constants.BEFORE_PAYMENT_ERROR, Constants.USER_CANCEL);

                    veritransSDK.setBBMCallBackUrl(bbmCallBackUrl);

                    Logger.i(" created new transaction object ");

                    ItemDetails itemDetails = new ItemDetails("1", amount, 1, "shoes");
                    ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
                    itemDetailsArrayList.add(itemDetails);
                    transactionRequestNew.setItemDetails(itemDetailsArrayList);

                    // bill info
                    BillInfoModel billInfoModel = new BillInfoModel("demo_lable", "demo_value");
                    transactionRequestNew.setBillInfoModel(billInfoModel);



                    //Logger.i("clickType"+clickType);
                    transactionRequestNew.setCardPaymentInfo(clickType,
                            isSecure);



                    veritransSDK.setTransactionRequest(transactionRequestNew);


                    veritransSDK.startPaymentUiFlow();
                }else {
                    Toast.makeText(CartActivity.this, "Please select payment information",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        setPaymentValues();
        payBtn.setText( getString(R.string.pay) +" "+getString(R.string.rp) +
                id.co.veritrans.sdk.utilities.Utils.getFormatedAmount(amount));
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
        if(veritransSDK!=null && veritransSDK.getTransactionRequest()!=null) {
            /*try {
                payBtn.setText(getString(R.string.pay) + " " +  (int)veritransSDK.getTransactionRequest().getAmount());
            }catch (NumberFormatException e){
                e.printStackTrace();
            }*/
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(id.co.veritrans.sdk.core.Constants.EVENT_TRANSACTION_COMPLETE);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void setPaymentValues() {

        transactionRequest = new TransactionRequest(this, Utils.generateOrderId(), amount);
        if (transactionRequest != null && veritransSDK != null) {
            transactionRequest = Utils.addTransactionInfo(transactionRequest, amount, clickType, isSecure);
            bbmCallBackUrl = new BBMCallBackUrl(Constants.CHECK_STATUS,
                    Constants.BEFORE_PAYMENT_ERROR, Constants.USER_CANCEL);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.option:
                startActivityForResult(new Intent(this, UiFlowActivity.class), 500);
                break;

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if ( resultCode == RESULT_OK ){

            Logger.i(" in on activity result");


            amount = data.getIntExtra("amount", Constants.AMOUNT);
            clickType = data.getStringExtra("clickType");
            isSecure = data.getBooleanExtra("isSecure",true);
            Logger.i("Amount:" + amount);
            //payBtn.setText(getString(R.string.pay)+" "+amount);
            payBtn.setText( getString(R.string.pay) +" "+getString(R.string.rp) +
                    id.co.veritrans.sdk.utilities.Utils.getFormatedAmount(amount));


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
                Log.i("CartActivity", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    /* onReceive will get called when transaction gets completed.
            */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Logger.d(TAG, "in onReceive  ");


            if (intent != null) {

                String errorMessage = intent.getStringExtra(id.co.veritrans.sdk.core.Constants
                        .TRANSACTION_ERROR_MESSAGE);
                TransactionResponse transactionResponse = (TransactionResponse) intent
                        .getSerializableExtra(id.co
                                .veritrans.sdk.core.Constants.TRANSACTION_RESPONSE);

                Log.d(TAG, "transaction error message " + errorMessage);

                if (transactionResponse != null) {
                    if(transactionResponse.getTransactionStatus().equalsIgnoreCase
                            (id.co.veritrans.sdk.core.Constants.DENY) ||  transactionResponse.getStatusCode()
                            .equalsIgnoreCase(id.co.veritrans.sdk.core.Constants.SUCCESS_CODE_202)){
                        SdkUtil.showSnackbar(CartActivity.this, transactionResponse.getStatusMessage());
                    } else if (transactionResponse.getTransactionStatus().equalsIgnoreCase
                            (id.co.veritrans.sdk.core.Constants.PENDING) || transactionResponse.getStatusCode()
                            .equalsIgnoreCase(id.co.veritrans.sdk.core.Constants.SUCCESS_CODE_201) ) {
                        if(transactionResponse.getFraudStatus().equalsIgnoreCase(id.co.veritrans.sdk.core.Constants.CHALLENGE)){
                            SdkUtil.showSnackbar(CartActivity.this, transactionResponse.getStatusMessage());
                        } else {
                            SdkUtil.showSnackbar(CartActivity.this, getString(R.string.transaction_pending));
                        }
                    } else if (!TextUtils.isEmpty(transactionResponse.getStatusMessage())) {
                        SdkUtil.showSnackbar(CartActivity.this, transactionResponse
                                .getStatusMessage());
                    } else if(transactionResponse.getStatusCode()
                            .equalsIgnoreCase(id.co.veritrans.sdk.core.Constants.SUCCESS_CODE_200)){
                        SdkUtil.showSnackbar(CartActivity.this, getString(R.string.payment_successful_msg));
                    }
                    Log.d(TAG, "transaction message " + transactionResponse.getStatusMessage());
                    // update Merchant Server;
                    Utils.updateMerchantServer(CartActivity.this, transactionResponse);
                } else {
                    Log.d(TAG, "Transaction failed.");
                }

            }

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}