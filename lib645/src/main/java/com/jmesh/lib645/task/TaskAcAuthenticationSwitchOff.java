package com.jmesh.lib645.task;

import com.jmesh.lib645.jni.AirConditionerSwitchConnector;
import com.jmesh.lib645.util.HexUtils;

/**
 * Created by Administrator on 2018/11/15.
 */

public class TaskAcAuthenticationSwitchOff extends TaskBase {
    public TaskAcAuthenticationSwitchOff(String meterCode) {
        super(meterCode);
    }

    @Override
    public int getTaskType() {
        return 0;
    }

    @Override
    public byte[] getCmd() {
        return AirConditionerSwitchConnector.getAirConditionerCmd(meterCode.getBytes(), "1A".getBytes());
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return AirConditionerSwitchConnector.resolveAirConditionerCmd(meterCode.getBytes(), "1A".getBytes(), HexUtils.formatHexString(bluetoothCallback).getBytes());
    }
}
