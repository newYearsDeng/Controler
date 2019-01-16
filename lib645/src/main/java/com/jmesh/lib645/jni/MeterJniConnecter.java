package com.jmesh.lib645.jni;


import android.util.Log;

import com.jmesh.lib645.util.HexUtils;


public class MeterJniConnecter {
    public static final String LogTag = MeterJniConnecter.class.getSimpleName();

    static {
        System.loadLibrary("control-lib");
    }

    public static final String METER_DI_ENERGY_CONSUM = "00000000";//电量
    public static final String METER_DI_VOLTAGE = "02010100";//电压
    public static final String METER_DI_CURRENT = "02020100";//电流
    public static final String METER_DI_POWER = "02030000";//功率
    public static final String METER_DI_FREQENCY = "02800002";//频率
    public static final String METER_DI_POWER_FACTOR = "02060000";//功率因数
    public static final String METER_DI_GET_SWITCH_STATE = "04000503";//获取闭合闸状态

    public static native int transCmd(byte[] meterCode, byte[] di, byte[] resultData);

    public static native int dataResult(byte[] meterCode, byte[] srcData, byte[] di, byte[] resultData);

    public static native int dataPack(byte[] meterCode, byte[] srcData, byte[] di, byte[] resultData);

    public static native int getMeterSwitchCmd(byte[] meterCode, byte[] srcData, byte[] resultData);

    public static native int resolveMeterSwitchCmd(byte[] meterCode, byte[] srcData, byte[] resulterData);

    public static byte[] getMeterSwitchCmd(byte[] meterCode, byte[] srcData) {
        Log.e(LogTag, "meterCode:" + new String(meterCode) + " srcData:" + new String(srcData));
        byte[] resultData = new byte[200];
        int length = getMeterSwitchCmd(meterCode, srcData, resultData);
        return HexUtils.intercept(resultData, length);
    }

    public static byte[] resolveMeterSwitchCmd(byte[] meterCode, byte[] srcData) {
        Log.e(LogTag, "meterCode:" + new String(meterCode) + " srcData:" + new String(srcData));
        byte[] resultData = new byte[200];
        int length = resolveMeterSwitchCmd(meterCode, srcData, resultData);
        return HexUtils.intercept(resultData, length);
    }


    public static byte[] transCmd(byte[] meterCode, byte[] di) {
        byte[] resultData = new byte[200];
        int length = transCmd(meterCode, di, resultData);
        return HexUtils.intercept(resultData, length);
    }

    public static byte[] transData(byte[] meterCode, byte[] srcData, byte[] di) {
        byte[] resultData = new byte[200];
        int length = dataPack(meterCode, srcData, di, resultData);
        return HexUtils.intercept(resultData, length);
    }

    public static byte[] resolveReult(byte[] meterCode, byte[] srcData, byte[] di) {
        byte[] resultData = new byte[200];
        int length = dataResult(meterCode, srcData, di, resultData);
        return HexUtils.intercept(resultData, length);
    }
}
