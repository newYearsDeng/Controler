package com.jmesh.lib645.jni;


import com.jmesh.lib645.util.HexUtils;

/**
 * Created by Administrator on 2018/11/13.
 */

public class SocketJniConnector {
    public static native int transCmd(byte[] meterCode, byte[] di, byte[] resultData);

    public static native int resolveResult(byte[] meterCode, byte[] srcData, byte[] resultData);

    public static byte[] transCmd(byte[] meterCode, byte[] di) {
        byte[] result = new byte[200];
        int dataLength = transCmd(meterCode, di, result);
        byte[] newByte = HexUtils.intercept(result, dataLength);
        return newByte;
    }

    public static byte[] resolveResult(byte[] meterCode, byte[] di) {
        byte[] result = new byte[200];
        int dataLength = resolveResult(meterCode, di, result);
        byte[] newByte = HexUtils.intercept(result, dataLength);
        return newByte;
    }

}
