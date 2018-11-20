package com.jmesh.appbase.utils;

import android.util.Log;

/**
 * Created by Administrator on 2018/10/31.
 */

public class Logger {

    public static boolean isPrint = true;
    private static String defaultTag = "logger";
    private static StringBuilder sStringBuilder = new StringBuilder();

    public static void printE(String... msg) {

    }

    public static void d(String... msg) {
        String result = appendStr(msg);
        Log.d(defaultTag, result);
    }

    private static String appendStr(String... msg) {
        initStringBuilder();
        for (String str : msg) {
            sStringBuilder.append(str);
            sStringBuilder.append(" ");
        }
        return sStringBuilder.toString();
    }

    private static void initStringBuilder() {
        if (sStringBuilder == null) {
            sStringBuilder = new StringBuilder();
        }
        sStringBuilder.delete(0, sStringBuilder.length());
    }

    public static void i(String... msg) {
        String result = appendStr(msg);
        Log.i(defaultTag, result);
    }

    public static void w(String... msg) {
        String result = appendStr(msg);
        Log.w(defaultTag, result);
    }

    public static void e(String... msg) {
        String result = appendStr(msg);
        Log.e(defaultTag, result);
    }

}
