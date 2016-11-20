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
import android.widget.ImageView;
import android.widget.Toast;

import com.tuktuk.dmth.tuktuk.Database.DatabaseHandler;
import com.tuktuk.dmth.tuktuk.NetServiceinterface;
import com.tuktuk.dmth.tuktuk.R;
import com.tuktuk.dmth.tuktuk.Service.NetService;
import com.tuktuk.dmth.tuktuk.Utils.MetaData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DetailsCapture extends AppCompatActivity {

    DatabaseHandler db;

    boolean isBound = false;
    DataReceiver Datarceiver;
    ProgressDialog dialog;
    NetServiceinterface mService;

    ImageView profpic;
    EditText editetxt_name;
    EditText editetxt_email;
    EditText editetxt_pwd;
    EditText editetxt_pwd_retype;
    Button btn_next_userdetails;

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
        setContentView(R.layout.activity_details_capture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profpic=(ImageView)findViewById(R.id.logoview);

        editetxt_name=(EditText)findViewById(R.id.editetxt_name);
        editetxt_email=(EditText)findViewById(R.id.editetxt_email);
        editetxt_pwd=(EditText)findViewById(R.id.editetxt_pwd);
        editetxt_pwd_retype=(EditText)findViewById(R.id.editetxt_pwd_retype);
        btn_next_userdetails=(Button)findViewById(R.id.btn_next_userdetails);

        btn_next_userdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editetxt_pwd.getText().toString().equals(editetxt_pwd_retype.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Password recheck fail",Toast.LENGTH_LONG);
                }
                else {
                    dialog = ProgressDialog.show(DetailsCapture.this, "Submitting", "Please wait...", true);
                    HashMap<String, String> inpmessage = new HashMap<String, String>();

                    inpmessage.put("name", editetxt_name.getText().toString());
                    inpmessage.put("password", editetxt_pwd.getText().toString());
                    inpmessage.put("email", editetxt_email.getText().toString());
                    inpmessage.put("id", db.getMetadata("userid"));
                    try {
                        if (mService == null) {
                            Log.e("mService", "mService register is null");
                        }
                        mService.sendNetworkData(inpmessage, MetaData.UserInfoKey);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        });



        db=new DatabaseHandler(getApplicationContext());

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

                Intent DetailsCapture = new Intent(DetailsCapture.this, DetailsCapture.class);


                try {
                    JSONObject rep=new JSONObject(intent.getStringExtra("result"));

                    if(rep.getString("auth").equals("true")) {

                        db.updateMetadata("logged","true");
                        startActivity(DetailsCapture);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "User data update Fail", Toast.LENGTH_LONG).show();
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
