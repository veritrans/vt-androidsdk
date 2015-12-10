package id.co.veritrans.sdk.example.acitivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import id.co.veritrans.sdk.core.TransactionRequest;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.example.R;
import id.co.veritrans.sdk.example.VeritransExampleApp;
import id.co.veritrans.sdk.example.utils.Constants;
import id.co.veritrans.sdk.example.utils.Utils;
import id.co.veritrans.sdk.models.BBMCallBackUrl;
import id.co.veritrans.sdk.models.PaymentMethodsModel;

public class CartActivity extends AppCompatActivity {

    private static final int OPTION_CON = 500;
    private VeritransSDK mVeritransSDK;
    private ArrayList<PaymentMethodsModel> selectedPaymentMethods;
    private Button payBtn;
    private int amount = Constants.AMOUNT;
    private String clickType = id.co.veritrans.sdk.core.Constants.CARD_CLICK_TYPE_NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cart");
        mVeritransSDK = (VeritransSDK) ((VeritransExampleApp) getApplication()).getVeritransSDK();
        selectedPaymentMethods = Utils.initialiseAdapterData(this);
        if (mVeritransSDK != null) {
            mVeritransSDK.setSelectedPaymentMethods(selectedPaymentMethods);
        }
        payBtn = (Button) findViewById(R.id.btn_payment);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVeritransSDK.startPaymentUiFlow();
            }
        });
        setPaymentValues();

    }

    private void setPaymentValues() {
        TransactionRequest transactionRequest = new TransactionRequest(this, Utils.generateOrderId(), amount);
        if (transactionRequest != null && mVeritransSDK != null) {
            transactionRequest = Utils.addTransactionInfo(transactionRequest, amount, clickType, true);
            BBMCallBackUrl bbmCallBackUrl = new BBMCallBackUrl(Constants.CHECK_STATUS,
                    Constants.BEFORE_PAYMENT_ERROR, Constants.USER_CANCEL);
            //start transaction
            mVeritransSDK.setTransactionRequest(transactionRequest);
            mVeritransSDK.setBBMCallBackUrl(bbmCallBackUrl);
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
                startActivity(new Intent(this, UiFlowActivity.class));
                break;

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}


