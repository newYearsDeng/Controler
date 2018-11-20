package com.jmesh.controler.task;

import com.jmesh.controler.jni.AirConditionerSwitchConnector;

/**
 * Created by Administrator on 2018/11/15.
 */

public class TaskACSwitchOn extends TaskBase {
    public TaskACSwitchOn(String meterCode, byte[] cmd) {
        super(meterCode);
        this.cmd = cmd;
    }

    @Override
    public int getTaskType() {
        return 0;
    }

    private byte[] cmd;

    @Override
    public byte[] getCmd() {
        return AirConditionerSwitchConnector.resolveAirConditionerCmd(meterCode.getBytes(), "4B".getBytes(), cmd);
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return AirConditionerSwitchConnector.resolveAirConditionerCmd(meterCode.getBytes(), "4B".getBytes(), bluetoothCallback);
    }
}
