package com.jmesh.controler.jni;

import com.jmesh.controler.util.HexUtils;

/**
 * Created by Administrator on 2018/12/12.
 */

public class CenterAirConditionerConnector {
    static {
        System.loadLibrary("control-lib");
    }

    public static native int getAirConditionerStatus(byte[] meterCode, byte[] resultData);

    public static native int resolveAirConditionerStatus(byte[] meterCode, byte[] srcData, byte[] resultData);

    public static native int setAirConditioner(byte[] meterCode, byte[] di, byte[] srcData, byte[] resultData);

    public static byte[] getAirConditionerStatus(byte[] meterCode) {
        byte[] resultData = new byte[200];
        int dataLength = getAirConditionerStatus(meterCode, resultData);
        return HexUtils.intercept(resultData, dataLength);
    }

    public static byte[] resolveAirConditionerStatus(byte[] meterCode, byte[] srcData) {
        byte[] resultData = new byte[200];
        int dataLength = resolveAirConditionerStatus(meterCode, srcData, resultData);
        return HexUtils.intercept(resultData, dataLength);
    }

    public static byte[] setAirConditioner(byte[] meterCode, byte[] di, byte[] srcData) {
        byte[] resultData = new byte[200];
        int dataLength = setAirConditioner(meterCode, di, srcData, resultData);
        return HexUtils.intercept(resultData, dataLength);
    }

}
