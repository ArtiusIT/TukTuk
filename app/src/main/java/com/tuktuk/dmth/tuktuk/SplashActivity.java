package com.tuktuk.dmth.tuktuk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.tuktuk.dmth.tuktuk.Activity.Registration;
import com.tuktuk.dmth.tuktuk.Database.DatabaseHandler;
import com.tuktuk.dmth.tuktuk.Utils.MetaData;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseHandler db=new DatabaseHandler(getApplicationContext());
        if(enableServices()){
            proceedToNextView();
        }
        else{

        }

        MetaData.initiateMetadaList();

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void proceedToNextView(){
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 3 seconds
                    sleep(3*1000);

                    // After 3 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(),Registration.class);
                    startActivity(i);

                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();

    }


    private boolean enableServices(){
        if(!checkforGps()){
            buildAlertMessageNoService(0);//0 for GPS
        }
        if(checkforGps()){
            if (!isNetworkConnected()) {
                buildAlertMessageNoService(1);//1 for NET
            }
        }

        boolean isrequirementdone=checkforGps();
        isrequirementdone=isrequirementdone && isNetworkConnected();
        return isrequirementdone;
    }
    private boolean checkforGps() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        }
        return true;
    }

        private void buildAlertMessageNoService(int num) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String msg="";
            final String Intentname;

            if (num==0){// 0 Means GPS not avaialable
                msg="Your GPS seems to be disabled, do you want to enable it?";
                Intentname=android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS;
            }
            else{
                msg="Your Internet link seems to be disabled, do you want to enable it?";
                Intentname= Settings.ACTION_SETTINGS;
            }

            builder.setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(Intentname));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


}
