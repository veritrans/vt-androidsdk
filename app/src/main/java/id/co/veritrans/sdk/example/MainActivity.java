package id.co.veritrans.sdk.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import id.co.veritrans.sdk.core.VeritransBuilder;
import id.co.veritrans.sdk.core.VeritransSDK;

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
                        VeritransBuilder(MainActivity.this, "dbdy", 100, true);
                veritransBuilder.enableLog(true);
                    veritransBuilder.buildSDK();



                VeritransBuilder veritransBuilder2 = new
                        VeritransBuilder(MainActivity.this, "dbdy", 100, true);

                VeritransSDK veritransSDK2 = veritransBuilder2.buildSDK();

                if(veritransSDK2 == null){
                    Log.d(TAG , "failed to create sdk instance.");
                }else{
                    Log.d(TAG , "successfully created sdk instance.");
                }

            }
        });

    }

}
