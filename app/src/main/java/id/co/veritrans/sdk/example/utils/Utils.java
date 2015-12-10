package id.co.veritrans.sdk.example.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.StorageDataHandler;
import id.co.veritrans.sdk.core.TransactionRequest;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.example.apicallinterfaces.ApiInterface;
import id.co.veritrans.sdk.example.apicallinterfaces.RegisterTokenCallback;
import id.co.veritrans.sdk.example.model.RegisterDeviceRequest;
import id.co.veritrans.sdk.example.model.RegisterDeviceResponse;
import id.co.veritrans.sdk.example.model.TransactionUpdateMerchantResponse;
import id.co.veritrans.sdk.example.apicallinterfaces.UpdateTransactionCallBack;
import id.co.veritrans.sdk.example.model.TransactionMerchant;
import id.co.veritrans.sdk.models.BillInfoModel;
import id.co.veritrans.sdk.models.ItemDetails;
import id.co.veritrans.sdk.models.PaymentMethodsModel;
import id.co.veritrans.sdk.models.TransactionResponse;
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
    private static Subscription registerSubscription = null;
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

    public static void registerTokenMerchant(Context activity, RegisterDeviceRequest registerDeviceRequest,
                                                 final RegisterTokenCallback callBack) {

        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

        if (veritransSDK != null) {
            ApiInterface apiInterface =
                    RestAdapter.getMerchantApiClient(activity, false);
            Observable<RegisterDeviceResponse> observable = null;
            if (apiInterface != null) {
                observable = apiInterface.registerDevice(registerDeviceRequest);
                registerSubscription = observable.subscribeOn(Schedulers
                        .io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<RegisterDeviceResponse>() {

                            @Override
                            public void onCompleted() {

                                if (registerSubscription != null && !registerSubscription.isUnsubscribed()) {
                                    registerSubscription.unsubscribe();
                                }

                            }

                            @Override
                            public void onError(Throwable throwable) {

                                Logger.e("error while getting token : ", "" +
                                        throwable.getMessage());
                                callBack.onFailure(throwable.getMessage(), null);
                            }

                            @Override
                            public void onNext(RegisterDeviceResponse registerDeviceResponse) {

                                if (registerDeviceResponse != null) {

                                    /*if (veritransSDK != null && veritransSDK.isLogEnabled()) {

                                    }*/

                                    if (registerDeviceResponse.getMessage().trim()
                                            .equalsIgnoreCase(id.co.veritrans.sdk.core.Constants.SUCCESS)) {
                                        callBack.onSuccess(registerDeviceResponse);
                                    } else {
                                        callBack.onFailure(registerDeviceResponse.getError(),
                                                registerDeviceResponse);
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

    /**
     * initialize adapter data model by dummy values.
     */
    public static ArrayList<PaymentMethodsModel> initialiseAdapterData(Context context) {
         ArrayList<PaymentMethodsModel> selectedPaymentMethods = new ArrayList<>();
        String[] names = context.getResources().getStringArray(id.co.veritrans.sdk.R.array.payment_methods);
        Logger.d(TAG, "there are total " + names.length + " payment methods available.");

        int[] paymentImageList = getImageList();

        for (int i = 0; i < names.length; i++) {
            PaymentMethodsModel model = new PaymentMethodsModel(names[i], paymentImageList[i],
                    id.co.veritrans.sdk.core.Constants.PAYMENT_METHOD_NOT_SELECTED);
            model.setIsSelected(true);
            selectedPaymentMethods.add(model);
        }
        return selectedPaymentMethods;

    }

    public static int[] getImageList() {

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

    public static TransactionRequest addTransactionInfo(TransactionRequest transactionRequest, double amount,
                                                        String clickType, boolean isSecure) {


        //to  perform transaction using mandiri bill payment.
        // item details
        ItemDetails itemDetails = new ItemDetails("1", amount, 1, "shoes");
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        transactionRequest.setItemDetails(itemDetailsArrayList);

        // bill info
        BillInfoModel billInfoModel = new BillInfoModel("demo_lable", "demo_value");
        transactionRequest.setBillInfoModel(billInfoModel);

        //Logger.i("clickType"+clickType);
        transactionRequest.setCardPaymentInfo(clickType,isSecure);
        return transactionRequest;
    }
}