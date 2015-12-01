package id.co.veritrans.sdk.example.model;

/**
 * Created by chetan on 30/11/15.
 */
public class RegisterDeviceResponse {
    private String message;
    private String statuscode;
    private String error;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
