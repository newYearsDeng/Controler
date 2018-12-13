package com.jmesh.controler.task;

import com.jmesh.controler.jni.CenterAirConditionerConnector;
import com.jmesh.controler.util.HexUtils;

/**
 * Created by Administrator on 2018/12/13.
 */

public class TaskCenterAirconditionerSetPower extends TaskBase {
    boolean state;
    public static final String DI = "bcaa0103";

    public TaskCenterAirconditionerSetPower(String meterCode, boolean state) {
        super(meterCode);
        this.state = state;
    }

    @Override
    public int getTaskType() {
        return 0;
    }

    @Override
    public byte[] getCmd() {
        byte[] taskData;
        if (state) {
            taskData = "aa".getBytes();
        } else {
            taskData ="55".getBytes();
        }
        return CenterAirConditionerConnector.setAirConditioner(meterCode.getBytes(), DI.getBytes(), taskData);
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return new byte[0];
    }
}
