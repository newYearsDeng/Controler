package com.jmesh.controler.jni;

import com.jmesh.controler.util.HexUtils;

/**
 * Created by Administrator on 2018/11/15.
 */

public class AirConditionerSwitchConnector {
    static {
        System.loadLibrary("control-lib");
    }

    public static native int getAirConditionerSwitchCmd(byte[] meterCode, byte[] srcData, byte[] style);

    public static native int resolveAirConditionerSwitchCmd(byte[] meterCode, byte[] style, byte[] srcData, byte[] resultData);


    public static byte[] getAirConditionerCmd(byte[] meterCode, byte[] style) {
        byte[] resultData = new byte[200];
        int dataLength = getAirConditionerSwitchCmd(meterCode, style, resultData);
        return HexUtils.intercept(resultData, dataLength);
    }

    public static byte[] resolveAirConditionerCmd(byte[] meterCode, byte[] style, byte[] srcData) {
        byte[] resultData = new byte[200];
        int dataLength = resolveAirConditionerSwitchCmd(meterCode, style, srcData, resultData);
        return HexUtils.intercept(resultData, dataLength);
    }
}
