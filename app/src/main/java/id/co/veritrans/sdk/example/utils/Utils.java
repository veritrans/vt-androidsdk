package id.co.veritrans.sdk.example.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import id.co.veritrans.sdk.callbacks.UpdateTransactionCallBack;
import id.co.veritrans.sdk.core.*;
import id.co.veritrans.sdk.example.model.ApiInterface;
import id.co.veritrans.sdk.models.TransactionMerchant;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.models.TransactionUpdateMerchantResponse;
import id.co.veritrans.sdk.models.UserDetail;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by shivam on 10/29/15.
 */
public class Utils {

    private final static String TAG = Utils.class.getSimpleName();
    private static Subscription merchantSubscription = null;
    /**
     * It will return random 8 digit alpha numeric string.
     *
     * @return
     */
    public static String generateOrderId() {
        String uuid = UUID.randomUUID().toString();
        Logger.d("random order id is "+uuid);
        return uuid.replace("-", "").substring(0, 10);
    }




    public static void updateMerchantServer(Activity activity, TransactionResponse transactionResponse) {

        if(activity != null && transactionResponse != null) {
            VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
            StorageDataHandler storageDataHandler = new StorageDataHandler();
            UserDetail userDetail = null;

            Log.i("updating MerchantServer", "");

            try {
                userDetail = (UserDetail) storageDataHandler.readObject(activity,
                        id.co.veritrans.sdk.core.Constants.USER_DETAILS);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (transactionResponse != null) {
                String email = "";
                if (userDetail != null) {
                    email = userDetail.getEmail();
                }
                TransactionMerchant transactionMerchant = new TransactionMerchant(
                        transactionResponse.getOrderId(), transactionResponse.getTransactionId(),
                        transactionResponse.getGrossAmount(), transactionResponse.getPaymentType(), email,

                        transactionResponse.getTransactionTime(), transactionResponse.getTransactionStatus());
                UpdateTransactionCallBack updateTransactionCallBack = new UpdateTransactionCallBack() {
                    @Override
                    public void onSuccess(TransactionUpdateMerchantResponse transactionUpdateMerchantResponse) {
                        Logger.i("Success");
                    }

                    @Override
                    public void onFailure(String errorMessage, TransactionUpdateMerchantResponse transactionUpdateMerchantResponse) {
                        Logger.i("Failure");
                    }
                };


                transactionUpdateMerchant(activity.getApplication(),
                        transactionMerchant, updateTransactionCallBack);

            }

        }else {

        }
    }



    public static void transactionUpdateMerchant(Context activity, TransactionMerchant transactionMerchant,
                                                 final UpdateTransactionCallBack callBack) {

        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

        if (veritransSDK != null) {
            ApiInterface apiInterface =
                    RestAdapter.getMerchantApiClient(activity, false);
            Observable<TransactionUpdateMerchantResponse> observable = null;
            if (apiInterface != null) {
                observable = apiInterface.updateTransactionStatusMerchant(transactionMerchant);
                merchantSubscription = observable.subscribeOn(Schedulers
                        .io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TransactionUpdateMerchantResponse>() {

                            @Override
                            public void onCompleted() {

                                if (merchantSubscription != null && !merchantSubscription.isUnsubscribed()) {
                                    merchantSubscription.unsubscribe();
                                }

                            }

                            @Override
                            public void onError(Throwable throwable) {

                                Logger.e("error while getting token : ", "" +
                                        throwable.getMessage());
                                callBack.onFailure(throwable.getMessage(), null);
                            }

                            @Override
                            public void onNext(TransactionUpdateMerchantResponse transactionUpdateMerchantResponse) {

                                if (transactionUpdateMerchantResponse != null) {

                                    /*if (veritransSDK != null && veritransSDK.isLogEnabled()) {

                                    }*/

                                    if (transactionUpdateMerchantResponse.getMessage().trim()
                                            .equalsIgnoreCase(id.co.veritrans.sdk.core.Constants.SUCCESS)) {
                                        callBack.onSuccess(transactionUpdateMerchantResponse);
                                    } else {
                                        callBack.onFailure(transactionUpdateMerchantResponse.getError(),
                                                transactionUpdateMerchantResponse);
                                    }

                                } else {
                                    callBack.onFailure(id.co.veritrans.sdk.core.Constants.ERROR_EMPTY_RESPONSE, null);
                                    Logger.e(id.co.veritrans.sdk.core.Constants.ERROR_EMPTY_RESPONSE);
                                }

                            }
                        });

            } else {
                callBack.onFailure(id.co.veritrans.sdk.core.Constants.ERROR_UNABLE_TO_CONNECT, null);
                Log.e(TAG, id.co.veritrans.sdk.core.Constants.ERROR_UNABLE_TO_CONNECT);

            }

        } else {
            callBack.onFailure(id.co.veritrans.sdk.core.Constants.ERROR_SDK_IS_NOT_INITIALIZED, null);
            Log.e(TAG, id.co.veritrans.sdk.core.Constants.ERROR_SDK_IS_NOT_INITIALIZED);
        }

    }

}