package com.jmesh.appbase.network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Administrator on 2018/7/6.
 */

public class HttpParams extends HashMap<String, String> {
    public HttpParams(Object... objects) {
        int length = objects.length;
        for (int index = 0; index + 1 < length; index += 2) {
            String key = (String) objects[index];
            put(key, String.valueOf(objects[index + 1]));
        }
    }

    public HttpParams put(String key, int value) {
        put(key, Integer.toString(value));
        return this;
    }

    public HttpParams put(String key, long value) {
        put(key, Long.toString(value));
        return this;
    }

    public HttpParams put(String key, float value) {
        put(key, Float.toString(value));
        return this;
    }

    public HttpParams put(String key, double value) {
        put(key, Double.toString(value));
        return this;
    }

    public HttpParams put(String key, JSONObject value) {
        put(key, value.toString());
        return this;
    }

    public HttpParams put(String key, JSONArray value) {
        put(key, value.toString());
        return this;
    }

    private static LinkedList<HttpParams> sReuseList = new LinkedList<>();

    //    TODO 只有主线程可使用，非线程安全
    static void addToReuse(HttpParams params) {

    }

    public static HttpParams genValidParams() {
        return new HttpParams();
    }

    public static HttpParams genValidParams(Object... objects) {
        return new HttpParams(objects);
    }
}
