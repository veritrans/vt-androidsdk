package id.co.veritrans.sdk.example.acitivities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.IOException;
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
import id.co.veritrans.sdk.models.BBMCallBackUrl;
import id.co.veritrans.sdk.models.BillInfoModel;
import id.co.veritrans.sdk.models.CardTokenRequest;
import id.co.veritrans.sdk.models.ItemDetails;
import id.co.veritrans.sdk.models.PaymentMethodsModel;
import id.co.veritrans.sdk.models.TransactionResponse;

/**
 * this is an example application,
 * created to show how developers can use veritrans sdk to perform transaction.
 */
public class UiFlowActivity extends AppCompatActivity {

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
    private EditText amountEt;

    private ArrayList<PaymentMethodsModel> selectedPaymentMethods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_ui_flow);
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

        amountEt = (EditText) findViewById(R.id.et_amount);

        if (BuildConfig.DEBUG) {
            amountEt.setText("100");
        }

        storageDataHandler = new StorageDataHandler();

        clickradioGroup = (RadioGroup) findViewById(R.id.click_rg);
        secureradioGroup = (RadioGroup) findViewById(R.id.secure_rg);
        Button payment = (Button) findViewById(R.id.btn_payment);
        Button deleteBt = (Button) findViewById(R.id.btn_delete_cards);
        unsecureRd = (RadioButton) findViewById(R.id.unseure_rd);
        secureRd = (RadioButton) findViewById(R.id.seure_rd);

        mVeritransSDK = (VeritransSDK) ((VeritransExampleApp) getApplication()).getVeritransSDK();

        initialiseAdapterData();
        deleteBt.setOnClickListener(new View.OnClickListener() {
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
        });
        //String[] paymentMethods = getResources().getStringArray(R.id.payment_methods);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                            paymentMethodsModel.setIsSelected(true);

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
                            paymentMethodsModel.setIsSelected(false);
                        }
                    }
                }
                if (mVeritransSDK != null) {
                    mVeritransSDK.setSelectedPaymentMethods(selectedPaymentMethods);
                }

                String amountData = amountEt.getText().toString();
                int amount = 100;

                if (amountData != null) {
                    try {
                        amount = Integer.parseInt(amountData);
                    } catch (NumberFormatException ex) {
                    }
                }

                transactionRequest =
                        new TransactionRequest(UiFlowActivity.this, Utils.generateOrderId(), amount);

                if (transactionRequest != null && mVeritransSDK != null) {

                    transactionRequest = addTransactionInfo(transactionRequest, amount);

                    BBMCallBackUrl bbmCallBackUrl = new BBMCallBackUrl(Constants.CHECK_STATUS,
                            Constants.BEFORE_PAYMENT_ERROR, Constants.USER_CANCEL);

                    //start transaction
                    mVeritransSDK.setTransactionRequest(transactionRequest);
                    mVeritransSDK.setBBMCallBackUrl(bbmCallBackUrl);
                    // for ui
                    mVeritransSDK.startPaymentUiFlow();

                }

                // "following code is added to test whether app allow to perform two"
                // "transaction simultaneously in that case sdk should give an error"

                //restart transaction
                //mVeritransSDK.setTransactionRequest(transactionRequest);

                //trying to create one more instance for debugging purpose. It should give u an
                // error message like 'transaction already in progress'.

                /*VeritransBuilder veritransBuilder2 = new
                        VeritransBuilder(MainActivity.this,
                        Constants.VT_CLIENT_KEY, Constants.VT_SERVER_KEY);

                VeritransSDK veritransSDK2 = veritransBuilder2.buildSDK();

                if(veritransSDK2 == null){
                    Log.d(TAG , "failed to create sdk instance.");
                }else{
                    Log.d(TAG, "successfully created sdk instance.");
                }*/

            }
        });

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

    private TransactionRequest addTransactionInfo(TransactionRequest transactionRequest, double amount) {

        transactionRequest.setCardPaymentInfo(clickType, isSecure);
        //to  perform transaction using mandiri bill payment.
        // item details
        ItemDetails itemDetails = new ItemDetails("1", amount, 1, "pen");
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        transactionRequest.setItemDetails(itemDetailsArrayList);

        // bill info
        BillInfoModel billInfoModel = new BillInfoModel("demo_lable", "demo_value");
        transactionRequest.setBillInfoModel(billInfoModel);

        //Logger.i("clickType"+clickType);
        transactionRequest.setCardPaymentInfo(clickType, secureRd.isChecked());
        return transactionRequest;
    }

    /**
     * initialize adapter data model by dummy values.
     */
    private void initialiseAdapterData() {

        String[] names = getResources().getStringArray(id.co.veritrans.sdk.R.array.payment_methods);
        Logger.d(TAG, "there are total " + names.length + " payment methods available.");

        int[] paymentImageList = getImageList();

        for (int i = 0; i < names.length; i++) {
            PaymentMethodsModel model = new PaymentMethodsModel(names[i], paymentImageList[i],
                    id.co.veritrans.sdk.core.Constants.PAYMENT_METHOD_NOT_SELECTED);
            selectedPaymentMethods.add(model);
        }

    }

    private int[] getImageList() {

        int[] paymentImageList = new int[11];

        paymentImageList[0] = id.co.veritrans.sdk.R.drawable.ic_offers;
        paymentImageList[1] = id.co.veritrans.sdk.R.drawable.ic_credit;
        paymentImageList[2] = id.co.veritrans.sdk.R.drawable.ic_mandiri2;
        paymentImageList[3] = id.co.veritrans.sdk.R.drawable.ic_cimb;
        paymentImageList[4] = id.co.veritrans.sdk.R.drawable.ic_epay;
        paymentImageList[5] = id.co.veritrans.sdk.R.drawable.ic_bbm;
        paymentImageList[6] = id.co.veritrans.sdk.R.drawable.ic_indosat;
        paymentImageList[7] = id.co.veritrans.sdk.R.drawable.ic_mandiri_e_cash; // mandiri e-Cash
        paymentImageList[8] = id.co.veritrans.sdk.R.drawable.ic_atm;
        paymentImageList[9] = id.co.veritrans.sdk.R.drawable.ic_mandiri_bill_payment2;
        paymentImageList[10] = id.co.veritrans.sdk.R.drawable.ic_indomaret;

        return paymentImageList;
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
                            (id.co.veritrans.sdk.core.Constants.PENDING)) {
                        SdkUtil.showSnackbar(UiFlowActivity.this, getString(R.string.transaction_pending));
                    } else if(transactionResponse.getTransactionStatus().equalsIgnoreCase
                            (id.co.veritrans.sdk.core.Constants.DENY)){
                        SdkUtil.showSnackbar(UiFlowActivity.this, getString(R.string.transaction_deny));
                    } else if (!TextUtils.isEmpty(transactionResponse.getStatusMessage())) {
                        SdkUtil.showSnackbar(UiFlowActivity.this, transactionResponse
                                .getStatusMessage());
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
}