package id.co.veritrans.sdk.example.acitivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import id.co.veritrans.sdk.example.R;

/**
 * this is an example application,
 * created to show how developers can use veritrans sdk to perform transaction.
 *
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_main);

        Button coreFlow = (Button) findViewById(R.id.btn_core_flow);
        coreFlow.setOnClickListener(this);

        Button uiFlow = (Button) findViewById(R.id.btn_ui_flow);
        uiFlow.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btn_core_flow){

            startActivity(new Intent(MainActivity.this, CoreFlowActivity.class));

        }else if(view.getId() == R.id.btn_ui_flow){
            startActivity(new Intent(MainActivity.this, UiFlowActivity.class));
        }

    }
}