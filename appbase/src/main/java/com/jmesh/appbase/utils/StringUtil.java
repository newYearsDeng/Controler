package com.jmesh.appbase.utils;

/**
 * Created by BC119 on 2018/6/27.
 */

public class StringUtil {
    public static byte[] strToByteArray(String str) {
        if (str == null) {
            return null;
        }
        byte[] byteArray = str.getBytes();
        return byteArray;
    }
}
