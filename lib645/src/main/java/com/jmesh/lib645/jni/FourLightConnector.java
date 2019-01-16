package com.jmesh.lib645.jni;


import com.jmesh.lib645.util.HexUtils;

/**
 * Created by Administrator on 2018/12/12.
 */

public class FourLightConnector {
    public static native int getLightSwitchStateCmd(byte[] meterCode, byte[] resultData);

    public static native int resolveLightSwitchStateResult(byte[] meterCode, byte[] srcdata, byte[] resultData);

    public static native int setLightStateEnable(byte[] meterCode, byte[] srcdata, byte[] resultData);

    public static native int setLightStatedisable(byte[] meterCode, byte[] srcdata, byte[] resultData);

    public static byte[] getLightSwitchStateCmd(byte[] meterCode) {
        byte[] resultData = new byte[200];
        int dataLength = getLightSwitchStateCmd(meterCode, resultData);
        return HexUtils.intercept(resultData, dataLength);
    }

    public static byte[] resolveLightSwitchStateResult(byte[] meterCode, byte[] srcData) {
        byte[] resultData = new byte[200];
        int dataLength = resolveLightSwitchStateResult(meterCode, srcData, resultData);
        return HexUtils.intercept(resultData, dataLength);
    }

    public static byte[] setLightStateEnable(byte[] meterCode, byte[] srcData) {
        byte[] resultData = new byte[200];
        int dataLength = setLightStateEnable(meterCode, srcData, resultData);
        return HexUtils.intercept(resultData, dataLength);
    }

    public static byte[] setLightStatedisable(byte[] meterCode, byte[] srcData) {
        byte[] resultData = new byte[200];
        int dataLength = setLightStatedisable(meterCode, srcData, resultData);
        return HexUtils.intercept(resultData, dataLength);
    }

}
