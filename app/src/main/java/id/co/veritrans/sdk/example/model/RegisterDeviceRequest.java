package id.co.veritrans.sdk.example.model;

/**
 * Created by chetan on 30/11/15.
 */
public class RegisterDeviceRequest {
    private String registrationId;
    private String oldRegistrationId;

    public RegisterDeviceRequest(String registrationId, String oldRegistrationId) {
        this.registrationId = registrationId;
        this.oldRegistrationId = oldRegistrationId;
    }

}
