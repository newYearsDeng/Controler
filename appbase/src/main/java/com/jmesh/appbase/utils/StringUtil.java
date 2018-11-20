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

    public static String reserve(int number, String data) {
        try {
            Float dataFloat = Float.parseFloat((String) data);
            return String.format("%." + Integer.toString(number) + "f", dataFloat);
        } catch (Throwable throwable) {

        }
        return data;
    }


}
