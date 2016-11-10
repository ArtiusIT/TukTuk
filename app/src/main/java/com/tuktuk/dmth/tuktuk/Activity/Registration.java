package com.tuktuk.dmth.tuktuk.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tuktuk.dmth.tuktuk.Database.DatabaseHandler;
import com.tuktuk.dmth.tuktuk.NetServiceinterface;
import com.tuktuk.dmth.tuktuk.R;
import com.tuktuk.dmth.tuktuk.Service.NetService;
import com.tuktuk.dmth.tuktuk.Utils.MetaData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {
    Button nextbtn_registration;
    EditText mobileNo_registration_input;
    boolean isBound = false;
    DataReceiver Datarceiver;

    ProgressDialog dialog;

    NetServiceinterface mService;
    DatabaseHandler db;
    private ServiceConnection serviceConnection=new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService= NetServiceinterface.Stub.asInterface(service);
            isBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService=null;
            isBound=false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ToDo set the +94 mark take automatically.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nextbtn_registration = (Button) findViewById(R.id.next_button_reg);
        mobileNo_registration_input = (EditText) findViewById(R.id.mobileno_register_edittext);

        Datarceiver=new DataReceiver();
        db=new DatabaseHandler(getApplicationContext());

        nextbtn_registration.setEnabled(false);
        mobileNo_registration_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern mPattern = Pattern.compile("^(\\d{10})$");  //ToDo change it to work with sri lankan mobile phone numbers

                Matcher matcher = mPattern.matcher(s.toString());
                if (matcher.find()) {
                    nextbtn_registration.setEnabled(true);
                } else {
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
                dialog = ProgressDialog.show(Registration.this, "Submitting", "Please wait...", true);
                //jsonObject.put("loguname", mobileNo_registration_input.getText());
                HashMap<String, String> inpmessage = new HashMap<String, String>();

                inpmessage.put("phone1",mobileNo_registration_input.getText().toString());
                try {
                    if (mService == null) {
                        Log.e("mService","mService register is null");
                    }
                    mService.sendNetworkData(inpmessage, MetaData.registerKey);
                } catch (RemoteException e) {
                    e.printStackTrace();
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

        bindServicetoActivity();
        registerReceiver(Datarceiver, new IntentFilter("tukme.tcpdirectcall.result"));
    }

    public void bindServicetoActivity(){
        Intent intent = new Intent(this, NetService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (isBound) {
           // getApplicationContext().unbindService(serviceConnection);
        }
        unregisterReceiver(Datarceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isBound) {
            bindServicetoActivity();
        }
        registerReceiver(Datarceiver, new IntentFilter("tukme.tcpdirectcall.result"));


    }

    class DataReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            dialog.hide();
            int errorcode=intent.getIntExtra("status", -1);
            if(errorcode== MetaData.MSG_Done){

                Intent pincodeverifyactivityintent = new Intent(Registration.this, PinCodeVerifyActivity.class);
                pincodeverifyactivityintent.putExtra("phone1", mobileNo_registration_input.getText().toString());

                try {
                    JSONObject rep=new JSONObject(intent.getStringExtra("result"));
                    db.updateMetadata("lastauthcode", rep.getString("authenticateCode"));
                    db.updateMetadata("userid",rep.getString("userID"));
                    startActivity(pincodeverifyactivityintent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            if(errorcode== MetaData.MSG_Fail){//ToDo say what is the error
                Toast.makeText(getApplicationContext(),"Error occured",Toast.LENGTH_LONG).show();
            }
        }
    }



}
