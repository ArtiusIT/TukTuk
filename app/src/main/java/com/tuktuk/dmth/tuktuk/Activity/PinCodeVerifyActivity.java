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
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tuktuk.dmth.tuktuk.Database.DatabaseHandler;
import com.tuktuk.dmth.tuktuk.NetServiceinterface;
import com.tuktuk.dmth.tuktuk.R;
import com.tuktuk.dmth.tuktuk.Service.NetService;
import com.tuktuk.dmth.tuktuk.Utils.MetaData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PinCodeVerifyActivity extends AppCompatActivity {
    EditText verify_code_enter_msg_label;
    Button btn_done;
    Button btn_resend;
    Button btn_change_number;
    DatabaseHandler db;
    EditText pincodeinput;

    boolean isBound = false;
    DataReceiver Datarceiver;
    ProgressDialog dialog;
    NetServiceinterface mService;



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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code_verify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db=new DatabaseHandler(getApplicationContext());
        btn_done=(Button)findViewById(R.id.btn_done);
        btn_resend=(Button)findViewById(R.id.btn_resend);
        btn_change_number=(Button)findViewById(R.id.btn_chnage_number);
        verify_code_enter_msg_label=(EditText)findViewById(R.id.Verifycode_edit_text);
        String mobileno=getIntent().getStringExtra("phone1");
        verify_code_enter_msg_label.setText("Please enter your verification code that was sent to "+mobileno);
        pincodeinput=(EditText)findViewById(R.id.Verifycode_edit_text);
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
                dialog = ProgressDialog.show(PinCodeVerifyActivity.this, "Submitting", "Please wait...", true);
                //jsonObject.put("loguname", mobileNo_registration_input.getText());
                HashMap<String, String> inpmessage = new HashMap<String, String>();

                //inpmessage.put("id",mobileNo_registration_input.getText().toString());
                String enteredcode=verify_code_enter_msg_label.getText().toString();
                inpmessage.put("authCode",enteredcode);
                inpmessage.put("id",db.getMetadata("userid"));
                try {
                    if (mService == null) {
                        Log.e("mService", "mService register is null");
                    }
                    mService.sendNetworkData(inpmessage, MetaData.AuthFirstKey);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }


            }
        });


        bindServicetoActivity();
        Datarceiver=new DataReceiver();
        registerReceiver(Datarceiver, new IntentFilter("tukme.tcpdirectcall.result"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public void bindServicetoActivity(){
        Intent intent = new Intent(this, NetService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    class DataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            dialog.hide();
            int errorcode=intent.getIntExtra("status", -1);
            if(errorcode== MetaData.MSG_Done){

                Intent DetailsCapture = new Intent(PinCodeVerifyActivity.this, DetailsCapture.class);


                try {
                    JSONObject rep=new JSONObject(intent.getStringExtra("result"));

                    if(rep.getString("auth").equals("true")) {
                        startActivity(DetailsCapture);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Auth Fail",Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                startActivity(DetailsCapture);
            }




            if(errorcode== MetaData.MSG_Fail){//ToDo say what is the error
                Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (isBound) {
            // getApplicationContext().unbindService(serviceConnection);
        }
        unregisterReceiver(Datarceiver);
    }

}
