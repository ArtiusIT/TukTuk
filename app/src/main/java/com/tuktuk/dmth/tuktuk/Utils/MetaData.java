package com.tuktuk.dmth.tuktuk.Utils;

import java.util.HashMap;

/**
 * Created by nrv on 10/1/16.
 */
public class MetaData {


    public final static int MSG_Login=1;
    public final static int MSG_Reg=2;

    public final static int MSG_Done=3;
    public final static int MSG_Fail=4;

    public final static String regUrl="";

    public final static String GetMethod="GET";
    public final static String PostMethod="POST";



    public final static String registerKey="Register";
    public final static String registerURL="http://54.254.184.125:8080/TukMeAPI/InsertUser/user/insert";

    public final static String AuthFirstKey="AuthFirst";
    public final static String AuthFirstURL="http://54.254.184.125:8080/TukMeAPI/Authentication/authentication/firsttime";

    public final static String LoginKey="Login";
    public final static String LoginURL="http://54.254.184.125:8080/TukMeAPI/Authentication/authentication/regular";

    public final static String GetUserInfoKey="GetUserInfo";
    public final static String GetUserInfoURL="http://54.254.184.125:8080/TukMeAPI/SelectUser/info/userInfo";

    public final static String UpdateUserLocationKey="UpdateUserLocation";
    public final static String UpdateUserLocationURL="http://54.254.184.125:8080/TukMeAPI/UpdateUser/update/location";

    public final static String UpdateMilageKey="UpdateMilage";
    public final static String UpdateMilageURL="http://54.254.184.125:8080/TukMeAPI/UpdateUser/update/location";

    public final static String UserInfoKey="UserInfo";
    public final static String UserInfoURL="http://54.254.184.125:8080/TukMeAPI/UpdateUser/update/userInfo";

    public final static String BookTaxiKey="BookTaxi";
    public final static String BookTaxiURL="http://54.254.184.125:8080/TukMeAPI/BookTaxi/booktaxi/insert";

    public final static String CancelBookingKey="CancelBooking";
    public final static String CancelBookingURL="http://54.254.184.125:8080/TukMeAPI/BookTaxi/booktaxi/cancel";

    public final static String bookinghistoryKey="bookinghistory";
    public final static String bookinghistoryURL="http://54.254.184.125:8080/TukMeAPI/BookTaxi/booktaxi/upcoming";

    public final static String vehiclesettingsKey="vehiclesettings";
    public final static String vehiclesettingsURL="http://54.254.184.125:8080/TukMeAPI/GetVehicle/vehicle/type";

    public final static String savehirerecordsKey="savehirerecords";
    public final static String savehirerecordsURL="http://54.254.184.125:8080/TukMeAPI/Hires/hire/insert";

    public final static String DriverhirerecordsKey="Driverhirerecords";
    public final static String DriverhirerecordsURL="http://54.254.184.125:8080/TukMeAPI/Hires/hire/Driverhistory";

    public final static String UserhirerecordsKey="Userhirerecords";
    public final static String UserhirerecordsURL="http://54.254.184.125:8080/TukMeAPI/Hires/hire/userhistory";

    public final static String PaymenthistoryKey="Paymenthistory";
    public final static String PaymenthistoryURL="http://54.254.184.125:8080/TukMeAPI/Payment/payment/history";

    public final static String blockdriversKey="blockdrivers";
    public final static String blockdriversURL="http://54.254.184.125:8080/TukMeAPI/BlockingDriver/block/driver";




    static HashMap<String,AccessMetadata> urlList=new HashMap<String,AccessMetadata>();


    public static AccessMetadata getURLForRequest(String request){
        return urlList.get(request);
    }

    public static void initiateMetadaList(){
        urlList.put(registerKey,new AccessMetadata(GetMethod,registerURL));
        urlList.put(AuthFirstKey,new AccessMetadata(PostMethod,AuthFirstURL));
        urlList.put(LoginKey,new AccessMetadata(GetMethod,LoginURL));
        urlList.put(GetUserInfoKey,new AccessMetadata(GetMethod,GetUserInfoURL));
        urlList.put(UpdateUserLocationKey,new AccessMetadata(GetMethod,UpdateUserLocationURL));
        urlList.put(UpdateMilageKey,new AccessMetadata(GetMethod,UpdateMilageURL));
        urlList.put(UserInfoKey,new AccessMetadata(GetMethod,UserInfoURL));
        urlList.put(BookTaxiKey,new AccessMetadata(GetMethod,BookTaxiURL));
        urlList.put(CancelBookingKey,new AccessMetadata(GetMethod,CancelBookingURL));
        urlList.put(bookinghistoryKey,new AccessMetadata(GetMethod,bookinghistoryURL));
        urlList.put(vehiclesettingsKey,new AccessMetadata(GetMethod,vehiclesettingsURL));
        urlList.put(savehirerecordsKey,new AccessMetadata(GetMethod,savehirerecordsURL));
        urlList.put(DriverhirerecordsKey,new AccessMetadata(GetMethod,DriverhirerecordsURL));
        urlList.put(UserhirerecordsKey,new AccessMetadata(GetMethod,UserhirerecordsURL));
        urlList.put(PaymenthistoryKey,new AccessMetadata(GetMethod,PaymenthistoryURL));
        urlList.put(blockdriversKey,new AccessMetadata(GetMethod,blockdriversURL));
    }

    public static boolean isinit(){
        if(urlList.size()!=0){
            return true;
        }
        return false;
    }



}
