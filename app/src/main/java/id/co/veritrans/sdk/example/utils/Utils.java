package id.co.veritrans.sdk.example.utils;

import java.util.UUID;

import id.co.veritrans.sdk.core.Logger;

/**
 * Created by shivam on 10/29/15.
 */
public class Utils {

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
}