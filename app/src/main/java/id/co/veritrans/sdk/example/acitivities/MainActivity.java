package id.co.veritrans.sdk.example.acitivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RadioGroup;

import java.io.IOException;
import java.util.ArrayList;

import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.StorageDataHandler;
import id.co.veritrans.sdk.core.VeritransBuilder;
import id.co.veritrans.sdk.example.R;
import id.co.veritrans.sdk.example.utils.Constants;
import id.co.veritrans.sdk.example.utils.Utils;
import id.co.veritrans.sdk.models.CardTokenRequest;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private RadioGroup clickradioGroup;
    private String clickType  = id.co.veritrans.sdk.core.Constants.CARD_CLICK_TYPE_NONE;
    private RadioGroup secureradioGroup;
    private boolean isSecure = false;
    private StorageDataHandler storageDataHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storageDataHandler = new StorageDataHandler();
        Button payment = (Button) findViewById(R.id.btn_payment);
        Button deleteBt = (Button) findViewById(R.id.btn_delete_cards);
        deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CardTokenRequest>cards = new ArrayList<CardTokenRequest>();
                try {
                    storageDataHandler.writeObject(MainActivity.this, id.co.veritrans.sdk.core.Constants.USERS_SAVED_CARD,cards);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VeritransBuilder veritransBuilder = new
                        VeritransBuilder(MainActivity.this, Utils.generateOrderId(),
                        Constants.VT_CLIENT_KEY, Constants.VT_SERVER_KEY, 100);
                veritransBuilder.enableLog(true);
                //Logger.i("clickType"+clickType);
                veritransBuilder.setCardPaymentInfo(clickType,isSecure);
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

        clickradioGroup = (RadioGroup) findViewById(R.id.click_rg);
        secureradioGroup = (RadioGroup) findViewById(R.id.secure_rg);
        clickradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.one_click_rd){
                    Logger.i("one click");
                    clickType = id.co.veritrans.sdk.core.Constants.CARD_CLICK_TYPE_ONE_CLICK;
                } else if(checkedId == R.id.two_click_rd){
                    Logger.i("two click");
                    clickType = id.co.veritrans.sdk.core.Constants.CARD_CLICK_TYPE_TWO_CLICK;
                } else {
                    Logger.i("normal");
                    clickType = id.co.veritrans.sdk.core.Constants.CARD_CLICK_TYPE_NONE;
                }
            }
        });
        secureradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.seure_rd){
                    isSecure = true;
                } else if(checkedId == R.id.unseure_rd){
                    isSecure = false;
                }
            }
        });



        //TextView textView = (TextView) findViewById(R.id.txt_payment);

        Animation animation = new TranslateAnimation(0,0, 100,100);
        animation.setStartOffset(200);
        animation.setFillAfter(true);
        animation.setDuration(2000);
        animation.setInterpolator(new LinearInterpolator());
        //textView.setAnimation(animation);
       // animation.set;



    }

}