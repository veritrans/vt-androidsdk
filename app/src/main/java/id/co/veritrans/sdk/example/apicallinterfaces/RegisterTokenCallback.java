package id.co.veritrans.sdk.example.apicallinterfaces;

import id.co.veritrans.sdk.example.model.RegisterDeviceResponse;

/**
 * Created by chetan on 30/11/15.
 */
public interface RegisterTokenCallback {
    public void onSuccess(RegisterDeviceResponse registerDeviceResponse);

    public void onFailure(String errorMessage, RegisterDeviceResponse
            registerDeviceResponse);

}
