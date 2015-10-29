package id.co.veritrans.sdk.example.acitivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import id.co.veritrans.sdk.core.VeritransBuilder;
import id.co.veritrans.sdk.example.R;
import id.co.veritrans.sdk.example.utils.Constants;
import id.co.veritrans.sdk.example.utils.Utils;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button payment = (Button) findViewById(R.id.btn_payment);

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VeritransBuilder veritransBuilder = new
                        VeritransBuilder(MainActivity.this, Utils.generateOrderId(),
                        Constants.VT_CLIENT_KEY, Constants.VT_SERVER_KEY, 100);
                veritransBuilder.enableLog(true);
                veritransBuilder.buildSDK();




    /*
                //trying to create one more instance for debugging purpose. It should give u an
                // error message like 'transaction already in progress'.

                VeritransBuilder veritransBuilder = new
                        VeritransBuilder(MainActivity.this, "dbdy",
                        Constants.VT_CLIENT_KEY, Constants.VT_SERVER_KEY, 100);

                VeritransSDK veritransSDK2 = veritransBuilder2.buildSDK();

                if(veritransSDK2 == null){
                    Log.d(TAG , "failed to create sdk instance.");
                }else{
                    Log.d(TAG , "successfully created sdk instance.");
                }*/

            }
        });

    }

}