package com.jmesh.controler.jni;

import com.jmesh.controler.util.HexUtils;

/**
 * Created by Administrator on 2018/11/12.
 */

public class AirConditionerConnector {
    static {
        System.loadLibrary("control-lib");
    }

    public static native int getAirConditionerCmd(byte[] meterCode, byte[] srcData, byte[] resultData);

    public static native int resolveAirConditionerCmd(byte[] meterCode, byte[] srcData, byte[] resultData);


    public static byte[] getAirConditionerCmd(byte[] meterCode, byte[] srcData) {
        byte[] resultData = new byte[200];
        int dataLength = getAirConditionerCmd(meterCode, srcData, resultData);
        return HexUtils.intercept(resultData, dataLength);
    }

    public static byte[] resolveAirConditionerCmd(byte[] meterCode, byte[] srcData) {
        byte[] resultData = new byte[200];
        int dataLength = resolveAirConditionerCmd(meterCode, srcData, resultData);
        return HexUtils.intercept(resultData, dataLength);
    }

}
