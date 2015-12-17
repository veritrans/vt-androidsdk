package id.co.veritrans.sdk.example.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.StorageDataHandler;
import id.co.veritrans.sdk.core.TransactionRequest;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.example.BuildConfig;
import id.co.veritrans.sdk.example.R;
import id.co.veritrans.sdk.example.VeritransExampleApp;
import id.co.veritrans.sdk.example.utils.Constants;
import id.co.veritrans.sdk.example.utils.Utils;
import id.co.veritrans.sdk.models.PaymentMethodsModel;
import id.co.veritrans.sdk.models.TransactionResponse;

/**
 * this is an example application,
 * created to show how developers can use veritrans sdk to perform transaction.
 */
public class UiFlowActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    public static final String TAG = UiFlowActivity.class.getSimpleName();
    private RadioGroup clickradioGroup;
    private String clickType = id.co.veritrans.sdk.core.Constants.CARD_CLICK_TYPE_NONE;
    private RadioGroup secureradioGroup;
    private boolean isSecure = false;
    private StorageDataHandler storageDataHandler;

    private VeritransSDK mVeritransSDK = null;
    private TransactionRequest transactionRequest = null;
    private RadioButton unsecureRd;
    private RadioButton secureRd;

    private CheckBox creditCardCheckBox;
    private CheckBox mandiriClickpayCheckBox;
    private CheckBox cimbClickpayCheckBox;
    private CheckBox epayBriCheckBox;
    private CheckBox bbmMoneyCheckBox;
    private CheckBox indosatCheckBox;
    private CheckBox manidriEcashCheckBox;
    private CheckBox banktransferCheckBox;
    private CheckBox mandiriBillCheckBox;
    private CheckBox indomaretCheckBox;
    private CheckBox selectAllCheckBox;
    private EditText amountEt;

    private ArrayList<PaymentMethodsModel> selectedPaymentMethods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_ui_flow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Options");
        creditCardCheckBox = (CheckBox) findViewById(R.id.cb_credit_card);
        mandiriClickpayCheckBox = (CheckBox) findViewById(R.id.cb_mandiri_clickpay);
        cimbClickpayCheckBox = (CheckBox) findViewById(R.id.cb_cimb_clickpay);
        epayBriCheckBox = (CheckBox) findViewById(R.id.cb_epay_bri);
        bbmMoneyCheckBox = (CheckBox) findViewById(R.id.cb_bbm_money);
        indosatCheckBox = (CheckBox) findViewById(R.id.cb_indosat);
        manidriEcashCheckBox = (CheckBox) findViewById(R.id.cb_manidri_ecash);
        banktransferCheckBox = (CheckBox) findViewById(R.id.cb_banktransfer);
        mandiriBillCheckBox = (CheckBox) findViewById(R.id.cb_mandiri_bill);
        indomaretCheckBox = (CheckBox) findViewById(R.id.cb_indomaret);
        selectAllCheckBox = (CheckBox) findViewById(R.id.cb_select_all);
        creditCardCheckBox.setOnCheckedChangeListener(this);
        mandiriClickpayCheckBox.setOnCheckedChangeListener(this);
        cimbClickpayCheckBox.setOnCheckedChangeListener(this);
        epayBriCheckBox.setOnCheckedChangeListener(this);
        bbmMoneyCheckBox.setOnCheckedChangeListener(this);
        indosatCheckBox.setOnCheckedChangeListener(this);
        manidriEcashCheckBox.setOnCheckedChangeListener(this);
        banktransferCheckBox.setOnCheckedChangeListener(this);
        mandiriBillCheckBox.setOnCheckedChangeListener(this);
        indomaretCheckBox.setOnCheckedChangeListener(this);


        selectAllCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                creditCardCheckBox.setChecked(isChecked);
                mandiriClickpayCheckBox.setChecked(isChecked);
                cimbClickpayCheckBox.setChecked(isChecked);
                epayBriCheckBox.setChecked(isChecked);
                bbmMoneyCheckBox.setChecked(isChecked);
                indosatCheckBox.setChecked(isChecked);
                manidriEcashCheckBox.setChecked(isChecked);
                banktransferCheckBox.setChecked(isChecked);
                mandiriBillCheckBox.setChecked(isChecked);
                indomaretCheckBox.setChecked(isChecked);
            }
        });

        amountEt = (EditText) findViewById(R.id.et_amount);

        if (BuildConfig.DEBUG) {
            amountEt.setText("" + Constants.AMOUNT);
        }

        storageDataHandler = new StorageDataHandler();

        clickradioGroup = (RadioGroup) findViewById(R.id.click_rg);
        secureradioGroup = (RadioGroup) findViewById(R.id.secure_rg);
        //Button payment = (Button) findViewById(R.id.btn_payment);
        //Button deleteBt = (Button) findViewById(R.id.btn_delete_cards);
        unsecureRd = (RadioButton) findViewById(R.id.unseure_rd);
        secureRd = (RadioButton) findViewById(R.id.seure_rd);

        mVeritransSDK = ((VeritransExampleApp) getApplication()).getVeritransSDK();

        selectedPaymentMethods = Utils.initialiseAdapterData(this);
        /*deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CardTokenRequest> cards = new ArrayList<CardTokenRequest>();
                try {
                    storageDataHandler.writeObject(UiFlowActivity.this, id.co.veritrans.sdk.core
                            .Constants.USERS_SAVED_CARD, cards);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/
        //String[] paymentMethods = getResources().getStringArray(R.id.payment_methods);
        /*payment.setOnClickListener(new View.OnClickListener() {
            @Override

        });*/

        clickradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.one_click_rd) {
                    Logger.i("one click");
                    clickType = id.co.veritrans.sdk.core.Constants.CARD_CLICK_TYPE_ONE_CLICK;
                    secureRd.setChecked(true);
                    unsecureRd.setEnabled(false);

                } else if (checkedId == R.id.two_click_rd) {
                    Logger.i("two click");
                    clickType = id.co.veritrans.sdk.core.Constants.CARD_CLICK_TYPE_TWO_CLICK;
                    secureRd.setChecked(true);
                    unsecureRd.setEnabled(false);
                } else {
                    Logger.i("normal");
                    clickType = id.co.veritrans.sdk.core.Constants.CARD_CLICK_TYPE_NONE;
                    unsecureRd.setEnabled(true);
                }
            }
        });

        secureradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.seure_rd) {
                    isSecure = true;
                } else if (checkedId == R.id.unseure_rd) {
                    isSecure = false;
                }
            }
        });

    }

    /**
     * onReceive will get called when transaction gets completed.
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
                    if (transactionResponse.getTransactionStatus().equalsIgnoreCase
                            (id.co.veritrans.sdk.core.Constants.DENY) || transactionResponse.getStatusCode()
                            .equalsIgnoreCase(id.co.veritrans.sdk.core.Constants.SUCCESS_CODE_202)) {
                        SdkUtil.showSnackbar(UiFlowActivity.this, transactionResponse.getStatusMessage());
                    } else if (transactionResponse.getTransactionStatus().equalsIgnoreCase
                            (id.co.veritrans.sdk.core.Constants.PENDING) || transactionResponse.getStatusCode()
                            .equalsIgnoreCase(id.co.veritrans.sdk.core.Constants.SUCCESS_CODE_201)) {
                        if (transactionResponse.getFraudStatus().equalsIgnoreCase(id.co.veritrans.sdk.core.Constants.CHALLENGE)) {
                            SdkUtil.showSnackbar(UiFlowActivity.this, transactionResponse.getStatusMessage());
                        } else {
                            SdkUtil.showSnackbar(UiFlowActivity.this, getString(R.string.transaction_pending));
                        }
                    } else if (!TextUtils.isEmpty(transactionResponse.getStatusMessage())) {
                        SdkUtil.showSnackbar(UiFlowActivity.this, transactionResponse
                                .getStatusMessage());
                    } else if (transactionResponse.getStatusCode()
                            .equalsIgnoreCase(id.co.veritrans.sdk.core.Constants.SUCCESS_CODE_200)) {
                        SdkUtil.showSnackbar(UiFlowActivity.this, getString(R.string.payment_successful_msg));
                    }
                    Log.d(TAG, "transaction message " + transactionResponse.getStatusMessage());
                    // update Merchant Server;
                    Utils.updateMerchantServer(UiFlowActivity.this, transactionResponse);
                } else {
                    Log.d(TAG, "Transaction failed.");
                }

            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(id.co.veritrans.sdk.core.Constants.EVENT_TRANSACTION_COMPLETE);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (!isChecked) {
            selectAllCheckBox.setChecked(false);

        } else if (creditCardCheckBox.isChecked() &&
                mandiriClickpayCheckBox.isChecked() &&
                cimbClickpayCheckBox.isChecked() &&
                epayBriCheckBox.isChecked() &&
                bbmMoneyCheckBox.isChecked() &&
                indosatCheckBox.isChecked() &&
                manidriEcashCheckBox.isChecked() &&
                banktransferCheckBox.isChecked() &&
                mandiriBillCheckBox.isChecked() &&
                indomaretCheckBox.isChecked()) {
                selectAllCheckBox.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.save:
                onClickSave();
                break;

        }
        return true;
    }

    private void onClickSave() {

        // transaction request initialization process.
        if (mVeritransSDK != null) {
            for (PaymentMethodsModel paymentMethodsModel : selectedPaymentMethods) {
                Logger.i("" + paymentMethodsModel.getName());
                if (paymentMethodsModel.getName().equalsIgnoreCase(getString(R.string
                        .credit_card))) {
                    paymentMethodsModel.setIsSelected(creditCardCheckBox.isChecked());
                }
                if (paymentMethodsModel.getName().equalsIgnoreCase(getString(R.string
                        .mandiri_clickpay))) {
                    paymentMethodsModel.setIsSelected(mandiriClickpayCheckBox.isChecked());
                }
                if (paymentMethodsModel.getName().equalsIgnoreCase(getString(R.string
                        .mandiri_bill_payment))) {
                    paymentMethodsModel.setIsSelected(mandiriBillCheckBox.isChecked());
                }
                if (paymentMethodsModel.getName().equalsIgnoreCase(getString(R.string
                        .cimb_clicks))) {
                    paymentMethodsModel.setIsSelected(cimbClickpayCheckBox.isChecked());
                }
                if (paymentMethodsModel.getName().equalsIgnoreCase(getString(R.string
                        .epay_bri))) {
                    paymentMethodsModel.setIsSelected(epayBriCheckBox.isChecked());
                }
                if (paymentMethodsModel.getName().equalsIgnoreCase(getString(R.string
                        .bbm_money))) {
                    paymentMethodsModel.setIsSelected(bbmMoneyCheckBox.isChecked());
                }
                if (paymentMethodsModel.getName().equalsIgnoreCase(getString(R.string
                        .indomaret))) {
                    paymentMethodsModel.setIsSelected(indomaretCheckBox.isChecked());
                }
                if (paymentMethodsModel.getName().equalsIgnoreCase(getString(R.string
                        .indosat_dompetku))) {
                    paymentMethodsModel.setIsSelected(indosatCheckBox.isChecked());

                }
                if (paymentMethodsModel.getName().equalsIgnoreCase(getString(R.string
                        .mandiri_e_cash))) {
                    paymentMethodsModel.setIsSelected(manidriEcashCheckBox.isChecked());
                }
                if (paymentMethodsModel.getName().equalsIgnoreCase(getString(R.string
                        .bank_transfer))) {
                    paymentMethodsModel.setIsSelected(banktransferCheckBox.isChecked());
                }
                if (paymentMethodsModel.getName().equalsIgnoreCase(getString(R.string
                        .offers))) {
                    paymentMethodsModel.setIsSelected(true);
                }
            }
        }
        if (mVeritransSDK != null) {
            mVeritransSDK.setSelectedPaymentMethods(selectedPaymentMethods);
        }

        String amountData = amountEt.getText().toString();
        int amount = Constants.AMOUNT;

        if (amountData != null) {
            try {
                amount = Integer.parseInt(amountData);
            } catch (NumberFormatException ex) {
            }
        }

        transactionRequest =
                new TransactionRequest(UiFlowActivity.this, Utils.generateOrderId(), amount);

        if (transactionRequest != null && mVeritransSDK != null) {

            transactionRequest = Utils.addTransactionInfo(transactionRequest, amount, clickType,
                    secureRd.isChecked());

            //start transaction
                    /*mVeritransSDK.setTransactionRequest(transactionRequest);
                    mVeritransSDK.setBBMCallBackUrl(bbmCallBackUrl);
*/

            Intent data = new Intent();
            data.putExtra("amount", amount);
            data.putExtra("clickType", clickType);
            data.putExtra("isSecure", secureRd.isChecked());
            setResult(RESULT_OK, data);

            // for ui
            //mVeritransSDK.startPaymentUiFlow();
            finish();

        }
    }
}