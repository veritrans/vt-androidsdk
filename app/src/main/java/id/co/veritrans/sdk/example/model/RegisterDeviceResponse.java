package id.co.veritrans.sdk.example.model;

import android.text.TextUtils;

/**
 * Created by chetan on 30/11/15.
 */
public class RegisterDeviceResponse {
    private String message;
    private String error;
    private String token;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getToken() {
        return TextUtils.isEmpty(token)?"":token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
