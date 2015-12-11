package id.co.veritrans.sdk.example.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import id.co.veritrans.sdk.callbacks.TransactionCallback;
import id.co.veritrans.sdk.core.TransactionRequest;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.example.BuildConfig;
import id.co.veritrans.sdk.example.R;
import id.co.veritrans.sdk.example.VeritransExampleApp;
import id.co.veritrans.sdk.example.utils.Utils;
import id.co.veritrans.sdk.models.BillInfoModel;
import id.co.veritrans.sdk.models.IndomaretRequestModel;
import id.co.veritrans.sdk.models.ItemDetails;
import id.co.veritrans.sdk.models.TransactionResponse;

/**
 * this is an example application,
 * created to show how developers can use veritrans sdk to perform transaction using core flow.
 * in core payment flow we are assuming that you have already implemented an ui to get required
 * information from user.
 * <p/>
 * In case of core payment flow you don't need to register broadcast receiver to get transaction
 * response.
 */
public class CoreFlowActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = CoreFlowActivity.class.getSimpleName();

    private VeritransSDK mVeritransSDK = null;
    private TransactionRequest transactionRequest = null;

    private EditText amountEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_core_flow);

        amountEt = (EditText) findViewById(R.id.et_amount);
        Button mandiri = (Button) findViewById(R.id.btn_using_mandiri);
        Button indosat = (Button) findViewById(R.id.btn_using_indosat);
        Button indomaret = (Button) findViewById(R.id.btn_using_indomaret);

        mandiri.setOnClickListener(this);
        indosat.setOnClickListener(this);
        indomaret.setOnClickListener(this);

        if (BuildConfig.DEBUG) {
            amountEt.setText("100");
        }

        mVeritransSDK = ((VeritransExampleApp) getApplication()).getVeritransSDK();


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


    /**
     * it adds all required information related to mandiri bill payment.
     */
    private void addTransactionInfoForMandiri() {

        //to  perform transaction using mandiri bill payment.
        transactionRequest =
                new TransactionRequest( CoreFlowActivity.this, Utils.generateOrderId(),  getAmount());


        // item details
        ItemDetails itemDetails = new ItemDetails("1", getAmount(), 1, "pen");
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        transactionRequest.setItemDetails(itemDetailsArrayList);

        // bill info
        BillInfoModel billInfoModel = new BillInfoModel("demo_lable", "demo_value");
        transactionRequest.setBillInfoModel(billInfoModel);


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_using_mandiri) {

            if (transactionRequest == null && mVeritransSDK != null) {

                addTransactionInfoForMandiri();
                mVeritransSDK.setTransactionRequest(transactionRequest);

                //start transaction
                performTransactionUsingMandiri();
            }else{
                Toast.makeText(CoreFlowActivity.this, "in else part of mandiri bill pay request", Toast
                        .LENGTH_SHORT).show();
            }


        } else if(R.id.btn_using_indosat ==view.getId()){

            if (transactionRequest == null && mVeritransSDK != null) {

                addTransactionInfoForMandiri();
                mVeritransSDK.setTransactionRequest(transactionRequest);

                //start transaction
                performTransactionUsingIndosat();
            }else{
                Toast.makeText(CoreFlowActivity.this, "in else part of indosat request", Toast
                        .LENGTH_SHORT).show();
            }
        } else {

            if (transactionRequest == null && mVeritransSDK != null) {

                addTransactionInfoForMandiri();
                mVeritransSDK.setTransactionRequest(transactionRequest);

                //start transaction
                performTransactionUsingIndomaret();
            }else{
                Toast.makeText(CoreFlowActivity.this, "in else part of indomaret request", Toast
                        .LENGTH_SHORT).show();
            }
        }

    }


    private int getAmount() {

        String amountData = amountEt.getText().toString();
        int amount = 10000;

        if (amountData != null) {
            try {
                amount = Integer.parseInt(amountData);
            } catch (NumberFormatException ex) {
            }
        }

        return amount;
    }


    /**
     * Execute mandiri bill payment transaction.
     */
    private void performTransactionUsingMandiri() {

        //execute transaction
        mVeritransSDK.paymentUsingMandiriBillPay(CoreFlowActivity.this, new
                TransactionCallback() {

                    @Override
                    public void onSuccess(TransactionResponse transactionResponse) {
                        Toast.makeText(CoreFlowActivity.this, "Transaction success:  " +
                                transactionResponse.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String errorMessage, TransactionResponse
                            transactionResponse) {

                        Toast.makeText(CoreFlowActivity.this, "Transaction failed: " + errorMessage,
                                Toast.LENGTH_SHORT).show();

                    }
                });
    }


    /**
     * Execute payment transaction using credit card method.
     * for debug it is 08123456789
     * msisdn must must less than 12
     */
    private void performTransactionUsingIndosat() {

        //Execute transaction
        mVeritransSDK.paymentUsingIndosatDompetku(CoreFlowActivity.this , "08123456789", new
                TransactionCallback() {

                    @Override
                    public void onSuccess(TransactionResponse transactionResponse) {
                        Toast.makeText(CoreFlowActivity.this, "Transaction success:  " +
                                transactionResponse.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String errorMessage, TransactionResponse
                            transactionResponse) {

                        Toast.makeText(CoreFlowActivity.this, "Transaction failed: " + errorMessage,
                                Toast.LENGTH_SHORT).show();

                    }
                });
    }



    /**
     * Execute payment transaction using  indomaret method.
     *
     */
    private void performTransactionUsingIndomaret() {


        IndomaretRequestModel.CstoreEntity cstoreEntity = new IndomaretRequestModel.CstoreEntity();
        cstoreEntity.setMessage("demo_message");
        cstoreEntity.setStore("indomaret");

        //Execute transaction
        mVeritransSDK.paymentUsingIndomaret(CoreFlowActivity.this, cstoreEntity, new
                TransactionCallback() {

                    @Override
                    public void onSuccess(TransactionResponse transactionResponse) {
                        Toast.makeText(CoreFlowActivity.this, "Transaction success:  " +
                                transactionResponse.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String errorMessage, TransactionResponse
                            transactionResponse) {

                        Toast.makeText(CoreFlowActivity.this, "Transaction failed: " + errorMessage,
                                Toast.LENGTH_SHORT).show();

                    }
                });
    }

}