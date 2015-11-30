package id.co.veritrans.sdk.example.apicallinterfaces;

import id.co.veritrans.sdk.example.model.TransactionUpdateMerchantResponse;

/**
 * Created by shivam on 10/29/15.
 */
public interface UpdateTransactionCallBack {
    public void onSuccess(TransactionUpdateMerchantResponse transactionUpdateMerchantResponse);

    public void onFailure(String errorMessage, TransactionUpdateMerchantResponse
            transactionUpdateMerchantResponse);
}