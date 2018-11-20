package com.jmesh.controler.jni;

import com.jmesh.controler.util.HexUtils;

/**
 * Created by Administrator on 2018/11/9.
 */

public class LightJniConnector {
    static {
        System.loadLibrary("control-lib");
    }

    public static native int getLightSwitchCmd(byte[] meterCode, byte[] srcData, byte[] resultData);

    public static native int resolveLightSwitchCmd(byte[] meterCode, byte[] srcData, byte[] resultData);


    public static byte[] getLightSwitchCmd(byte[] meterCode, byte[] srcData) {
        byte[] resultData = new byte[200];
        int dataLength = getLightSwitchCmd(meterCode, srcData, resultData);
        return HexUtils.intercept(resultData, dataLength);
    }

    public static byte[] resolveLightSwitchCmd(byte[] meterCode, byte[] srcData) {
        byte[] resultData = new byte[200];
        int dataLength = resolveLightSwitchCmd(meterCode, srcData, resultData);
        return HexUtils.intercept(resultData, dataLength);
    }
}
