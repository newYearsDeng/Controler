package com.jmesh.lib645.task;

import com.jmesh.lib645.AirConditionerData;
import com.jmesh.lib645.jni.CenterAirConditionerConnector;

/**
 * Created by Administrator on 2018/12/13.
 */

public class TaskCenterAirconditionerSetModeNTemp extends TaskBase {
    int mode;
    int temperture;
    public static final String DI = "bcaa0001";

    public TaskCenterAirconditionerSetModeNTemp(String meterCode, int mode, int temperture) {
        super(meterCode);
        this.mode = mode;
        this.temperture = temperture;
    }

    @Override
    public int getTaskType() {
        return 0;
    }

    @Override
    public byte[] getCmd() {
        String taskData = null;
        switch (mode) {
            case AirConditionerData.modeRefrigeration:
                taskData = "00" + Integer.toString(temperture);
                break;
            case AirConditionerData.modeHeat:
                taskData = "01" + Integer.toString(temperture);
                break;
            default:
                taskData = "00" + Integer.toString(temperture);
                break;
        }
        return CenterAirConditionerConnector.setAirConditioner(meterCode.getBytes(), DI.getBytes(), taskData.getBytes());
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return new byte[0];
    }
}
