package com.jmesh.controler.task;

import com.jmesh.controler.jni.AirConditionerSwitchConnector;
import com.jmesh.controler.util.HexUtils;

/**
 * Created by Administrator on 2018/11/15.
 */

public class TaskACAuthenticationSwitchOn extends TaskBase {

    public TaskACAuthenticationSwitchOn(String meterCode) {
        super(meterCode);
    }

    @Override
    public int getTaskType() {
        return 0;
    }

    @Override
    public byte[] getCmd() {
        return AirConditionerSwitchConnector.getAirConditionerCmd(meterCode.getBytes(), "1a".getBytes());
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return AirConditionerSwitchConnector.resolveAirConditionerCmd(meterCode.getBytes(), "1a".getBytes(), HexUtils.formatHexString(bluetoothCallback).getBytes());
    }
}
