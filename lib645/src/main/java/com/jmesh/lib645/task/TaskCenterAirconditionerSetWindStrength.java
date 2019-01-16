package com.jmesh.lib645.task;


import com.jmesh.lib645.AirConditionerData;
import com.jmesh.lib645.jni.CenterAirConditionerConnector;

/**
 * Created by Administrator on 2018/12/13.
 */

public class TaskCenterAirconditionerSetWindStrength extends TaskBase {
    int windStrength;

    public static final String DI = "bcaa0002";

    public TaskCenterAirconditionerSetWindStrength(String meterCode, int windStrength) {
        super(meterCode);
        this.windStrength = windStrength;
    }

    @Override
    public int getTaskType() {
        return 0;
    }

    @Override
    public byte[] getCmd() {
        byte[] windStrengthData;
        switch (windStrength) {
            case AirConditionerData.windStrengthLowLevel:
                windStrengthData = "01".getBytes();
                break;
            case AirConditionerData.windStrengthMidLevel:
                windStrengthData = "02".getBytes();
                break;
            case AirConditionerData.windStrengthHighLevel:
                windStrengthData = "03".getBytes();
                break;
            case AirConditionerData.windStrengthAuto:
                windStrengthData = "81".getBytes();
                break;
            default:
                windStrengthData = "81".getBytes();
                break;
        }
        return CenterAirConditionerConnector.setAirConditioner(meterCode.getBytes(), DI.getBytes(), windStrengthData);
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return new byte[0];
    }
}
