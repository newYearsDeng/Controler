package com.jmesh.lib645.task;


import com.jmesh.lib645.jni.AirConditionerSwitchConnector;

/**
 * Created by Administrator on 2018/11/15.
 */

public class TaskACSwitchOff extends TaskBase {
    public TaskACSwitchOff(String meterCode, byte[] cmd) {
        super(meterCode);
        this.cmd = cmd;
    }

    byte[] cmd;

    @Override
    public int getTaskType() {
        return 0;
    }

    @Override
    public byte[] getCmd() {
        return AirConditionerSwitchConnector.resolveAirConditionerCmd(meterCode.getBytes(), "1A".getBytes(), cmd);
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return AirConditionerSwitchConnector.resolveAirConditionerCmd(meterCode.getBytes(), "1A".getBytes(), bluetoothCallback);
    }
}
