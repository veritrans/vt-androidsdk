package id.co.veritrans.sdk.example.utils;

import java.util.UUID;

/**
 * Created by shivam on 10/29/15.
 */
public class Utils {

    /**
     * It will return random 16 digit alpha numeric string.
     * @return
     */
    public static String generateOrderId(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-","");
    }
}