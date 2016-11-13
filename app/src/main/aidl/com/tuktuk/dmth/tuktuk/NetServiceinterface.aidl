// NetService.aidl
package com.tuktuk.dmth.tuktuk;




// Declare any non-default types here with import statements

interface NetServiceinterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    /*void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);*/

    String sendNetworkData(in Map urldata,in String requestfor);
}
