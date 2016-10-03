package com.tuktuk.dmth.tuktuk.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tuktuk.dmth.tuktuk.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {
    Button nextbtn_registration;
    EditText mobileNo_registration_input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ToDo set the +94 mark take automatically.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nextbtn_registration=(Button)findViewById(R.id.btn_next_userreg);
        mobileNo_registration_input=(EditText)findViewById(R.id.input_mobile_no);

        nextbtn_registration.setEnabled(false);
        mobileNo_registration_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern mPattern = Pattern.compile("^(\\d{10})$");  //ToDo change it to work with sri lankan mobile phone numbers

                Matcher matcher = mPattern.matcher(s.toString());
                if(matcher.find())
                {
                    nextbtn_registration.setEnabled(true);
                }
                else{
                    nextbtn_registration.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        nextbtn_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pincodeverifyactivityintent=new Intent(Registration.this,PinCodeVerifyActivity.class);
                pincodeverifyactivityintent.putExtra("MobileNo",mobileNo_registration_input.getText().toString());
                startActivity(pincodeverifyactivityintent);
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
