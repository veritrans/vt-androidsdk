package id.co.veritrans.sdk.example.acitivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import java.io.IOException;
import java.util.ArrayList;

import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.StorageDataHandler;
import id.co.veritrans.sdk.core.TransactionRequest;
import id.co.veritrans.sdk.core.VeritransBuilder;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.example.R;
import id.co.veritrans.sdk.example.utils.Constants;
import id.co.veritrans.sdk.example.utils.Utils;
import id.co.veritrans.sdk.models.BillInfoModel;
import id.co.veritrans.sdk.models.CardTokenRequest;
import id.co.veritrans.sdk.models.ItemDetails;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private RadioGroup clickradioGroup;
    private String clickType = id.co.veritrans.sdk.core.Constants.CARD_CLICK_TYPE_NONE;
    private RadioGroup secureradioGroup;
    private boolean isSecure = false;
    private StorageDataHandler storageDataHandler;

    private VeritransSDK mVeritransSDK = null;
    private TransactionRequest transactionRequest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storageDataHandler = new StorageDataHandler();

        clickradioGroup = (RadioGroup) findViewById(R.id.click_rg);
        secureradioGroup = (RadioGroup) findViewById(R.id.secure_rg);
        Button payment = (Button) findViewById(R.id.btn_payment);
        Button deleteBt = (Button) findViewById(R.id.btn_delete_cards);

        initializeSdk();

        deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CardTokenRequest> cards = new ArrayList<CardTokenRequest>();
                try {
                    storageDataHandler.writeObject(MainActivity.this, id.co.veritrans.sdk.core
                            .Constants.USERS_SAVED_CARD, cards);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // transaction request initialization process.

                transactionRequest =
                        new TransactionRequest(Utils.generateOrderId(), MainActivity.this, 100,
                                id.co.veritrans.sdk.core.Constants.PAYMENT_METHOD_NOT_SELECTED);

                if (transactionRequest != null && mVeritransSDK != null) {

                    transactionRequest = addTransactionInfo(transactionRequest);
                    //start transaction
                    mVeritransSDK.setTransactionRequest(transactionRequest);

                    // for ui
                    mVeritransSDK.startPaymentUiFlow();


                   /* mVeritransSDK.paymentUsingMandiriBillPay(MainActivity.this, new
                            TransactionCallback() {

                                @Override
                                public void onFailure(String errorMessage, TransactionResponse
                                        transactionResponse) {

                                    Toast.makeText(getApplicationContext(),
                                            "failed : " + errorMessage, Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onSuccess(TransactionResponse transactionResponse) {
                                    Toast.makeText(getApplicationContext(),
                                            "Success: ", Toast.LENGTH_SHORT).show();


                                    mVeritransSDK.setTransactionRequest(transactionRequest);

                                }


                            });*/
                }


                //todo "following code is added to test whether app allow to perform two"
                //todo "transaction simultaneously in that case sdk should give an error"


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
                } else if (checkedId == R.id.two_click_rd) {
                    Logger.i("two click");
                    clickType = id.co.veritrans.sdk.core.Constants.CARD_CLICK_TYPE_TWO_CLICK;
                } else {
                    Logger.i("normal");
                    clickType = id.co.veritrans.sdk.core.Constants.CARD_CLICK_TYPE_NONE;
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

    private TransactionRequest addTransactionInfo(TransactionRequest transactionRequest) {

        transactionRequest.setCardPaymentInfo(clickType, isSecure);
        //to  perform transaction using mandiri bill payment.
        // item details
        ItemDetails itemDetails = new ItemDetails("1", 25, 4, "pen");
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        transactionRequest.setItemDetails(itemDetailsArrayList);

        // bill info
        BillInfoModel billInfoModel = new BillInfoModel("demo_lable", "demo_value");
        transactionRequest.setBillInfoModel(billInfoModel);

        //Logger.i("clickType"+clickType);
        transactionRequest.setCardPaymentInfo(clickType, isSecure);
        return transactionRequest;
    }

    private void initializeSdk() {
        // sdk initialization process
        VeritransBuilder veritransBuilder = new
                VeritransBuilder(getApplicationContext(),
                Constants.VT_CLIENT_KEY, Constants.VT_SERVER_KEY);
        veritransBuilder.enableLog(true);

        mVeritransSDK = veritransBuilder.buildSDK();
    }

}