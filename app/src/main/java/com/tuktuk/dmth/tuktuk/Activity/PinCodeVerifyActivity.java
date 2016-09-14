package com.tuktuk.dmth.tuktuk.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tuktuk.dmth.tuktuk.R;

public class PinCodeVerifyActivity extends AppCompatActivity {
    TextView verify_code_enter_msg_label;
    Button btn_done;
    Button btn_resend;
    Button btn_change_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code_verify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_done=(Button)findViewById(R.id.btn_done);
        btn_resend=(Button)findViewById(R.id.btn_resend);
        btn_change_number=(Button)findViewById(R.id.btn_chnage_number);
        verify_code_enter_msg_label=(TextView)findViewById(R.id.text_verfy_enter_message_lbl);
        String mobileno=getIntent().getStringExtra("MobileNo");
        verify_code_enter_msg_label.setText("Please enter your verification code that was sent to "+mobileno);

        btn_change_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changenoIntent = new Intent(PinCodeVerifyActivity.this, Registration.class);
                startActivity(changenoIntent);
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(true){//check wheather the pincode is right
                        Intent DetailsCapture = new Intent(PinCodeVerifyActivity.this, DetailsCapture.class);
                        startActivity(DetailsCapture);
                    }
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
