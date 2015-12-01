package id.co.veritrans.sdk.example.apicallinterfaces;

import id.co.veritrans.sdk.example.model.RegisterDeviceRequest;
import id.co.veritrans.sdk.example.model.RegisterDeviceResponse;
import id.co.veritrans.sdk.example.model.TransactionMerchant;
import id.co.veritrans.sdk.example.model.TransactionUpdateMerchantResponse;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;
import rx.Observable;

public interface ApiInterface {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/purchase/")
    Observable<TransactionUpdateMerchantResponse> updateTransactionStatusMerchant(
            @Body TransactionMerchant transactionMerchant);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/registerdevice/")
    Observable<RegisterDeviceResponse> registerDevice(
            @Body RegisterDeviceRequest registerDeviceRequest);
}
