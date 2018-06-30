package com.mmm.flash.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtil {
    public static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.setTimeout(60000);
    }

    public static void get(String urlString, AsyncHttpResponseHandler res) {
        client.get(urlString, res);
    }

    public static void post(String urlString, AsyncHttpResponseHandler res) {
        client.post(urlString, res);
    }

    public static void get(String urlString, RequestParams params, AsyncHttpResponseHandler res) {
        client.get(urlString, params, res);
    }

    public static void post(String urlString, RequestParams params,
                            AsyncHttpResponseHandler res) {
        client.post(urlString, params, res);
    }


    public static void get(String urlString, RequestParams params,
                           JsonHttpResponseHandler res) {
        client.get(urlString, params, res);
    }

    public static void get(String uString, BinaryHttpResponseHandler bHandler) {
        client.get(uString, bHandler);
    }

    public static void post(String uString, BinaryHttpResponseHandler bHandler) {
        client.post(uString, bHandler);
    }

    public static void delete(String uString, AsyncHttpResponseHandler handler) {
        client.delete(uString, handler);
    }
    public static AsyncHttpClient getClient() {
        return client;
    }

}