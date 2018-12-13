package com.jmesh.controler.task;

import com.jmesh.controler.data.AirConditionerData;
import com.jmesh.controler.jni.CenterAirConditionerConnector;
import com.jmesh.controler.util.HexUtils;

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
