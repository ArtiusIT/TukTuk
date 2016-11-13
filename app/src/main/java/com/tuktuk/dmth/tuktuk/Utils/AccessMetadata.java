package com.tuktuk.dmth.tuktuk.Utils;

/**
 * Created by nrv on 11/1/16.
 */
public class AccessMetadata{
    public String url;
    public String method;

    public AccessMetadata(String method,String url) {
        this.url = url;
        this.method = method;
    }
}