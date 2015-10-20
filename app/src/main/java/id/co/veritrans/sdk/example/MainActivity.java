package id.co.veritrans.sdk.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import id.co.veritrans.sdk.core.VeritransBuilder;

public class MainActivity extends AppCompatActivity {

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
                    veritransBuilder.buildSDK();
            }
        });

    }

}
