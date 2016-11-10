package com.tuktuk.dmth.tuktuk.Service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tuktuk.dmth.tuktuk.NetServiceinterface;
import com.tuktuk.dmth.tuktuk.Utils.AccessMetadata;
import com.tuktuk.dmth.tuktuk.Utils.MetaData;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nrv on 9/15/16.
 */
public class NetService extends Service {

    private String TAG="NetService";

    private final NetServiceinterface.Stub mbinder= new NetServiceinterface.Stub() {
        @Override
        public String sendNetworkData(Map urldata, String requestfor) throws RemoteException {
            try {
                AccessMetadata urlmetadata = MetaData.getURLForRequest(requestfor);

                urldata.put("requesttype", urlmetadata.method);
                urldata.put("requesturl", urlmetadata.url);
                new HTTPRequest().execute(urldata);
                return "OK";
            }
            catch (Exception e){
                return null;
            }

        }
    };


    public String parseURLGet(String url, Map<String, String> params)
    {

        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (String key : params.keySet())
        {
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }


    public String parseURLPost(Map<String, String> params)
    {

        Uri.Builder builder = Uri.parse("").buildUpon();
        for (String key : params.keySet())
        {
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.d(TAG, "onBind done");
        return mbinder;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }



    class HTTPRequest extends AsyncTask<Map,String,JSONObject>{

        @Override
        protected JSONObject doInBackground(Map... params) {
            Map curreq=params[0];
            if(curreq.get("requesttype").equals(MetaData.GetMethod)) {
                curreq.remove("requesttype");//All data comes in hashmap object. And before add parametes as url paramerrs we must have to remove unwanted data
                String url=curreq.get("requesturl").toString();
                curreq.remove("requesturl");
                String fulurl=parseURLGet(url,curreq);
                //ToDo execute get request
                JSONObject jobject=new JSONObject();
                String reply;
                try{
                    reply=doGet(fulurl);
                    jobject.put("resultobject",reply);
                    return jobject;
                }
                catch (Exception e){
                    reply=null;
                    e.printStackTrace();
                }

                return null;
            }
            else if(curreq.get("requesttype").equals(MetaData.PostMethod)) {
                curreq.remove("requesttype");
                String url=curreq.get("requesturl").toString();
                curreq.remove("requesturl");
                String parameters=parseURLPost(curreq);
                Log.e("NetServicePOST",url);
                JSONObject jobject=new JSONObject();
                String response=null;
                try {
                    response=doPost(url,parameters);
                    jobject.put("resultobject",response);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAGDoPOST",e.getMessage());
                }


                return jobject;
            }
            return null;
        }

        public String doGet(String urlstr) throws IOException {

                InputStream is = null;
                // Only display the first 500 characters of the retrieved
                // web page content.



                    URL url = new URL(urlstr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    // Starts the query
                    conn.connect();
                    int response = conn.getResponseCode();
                    Log.d("DEBUG_TAG", "The response is: " + response);
                    is = conn.getInputStream();

                    // Convert the InputStream into a string
                    String contentAsString = convertToString(is);



                    return contentAsString;

        }

        public String doPost(String urlstr,String params) throws IOException {

            URL obj = new URL(urlstr);
            Log.e("ServicePOST",urlstr);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");


            // For POST only - START
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(params.getBytes());
            os.flush();
            os.close();
            String finalout=null;
            int responseCode = con.getResponseCode();
            System.out.println(responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                //System.out.println(response.toString());
                finalout=response.toString();
            } else {
                //System.out.println("POST request not worked");
            }
            return finalout;

        }

        public String convertToString(InputStream in) throws IOException {

            BufferedInputStream bis = new BufferedInputStream(in);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result = bis.read();
            while(result != -1) {
                byte b = (byte)result;
                buf.write(b);
                result = bis.read();
            }
            return buf.toString();
        }


        @Override
        protected void onPostExecute(JSONObject s) {
            super.onPostExecute(s);

            Intent dataIntent = new Intent("tukme.tcpdirectcall.result");//ToDo add intent filetr name
            if(s==null || s.equals("{}")){
                dataIntent.putExtra("status",MetaData.MSG_Fail);
                Log.e("Service","NULL");
            }
            else {
                Log.e("Service",s.toString());
                dataIntent.putExtra("status",MetaData.MSG_Done);
                try {
                    dataIntent.putExtra("result",s.getString("resultobject"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataIntent.putExtra("status", MetaData.MSG_Fail);
                }

            }
            sendBroadcast(dataIntent);
        }
    }




}
