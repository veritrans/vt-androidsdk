package id.co.veritrans.sdk.example.model;

import id.co.veritrans.sdk.models.TransactionMerchant;
import id.co.veritrans.sdk.models.TransactionUpdateMerchantResponse;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;
import rx.Observable;

public interface ApiInterface {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/purchase/")
    Observable<TransactionUpdateMerchantResponse> updateTransactionStatusMerchant(
            @Body TransactionMerchant transactionMerchant);
}
