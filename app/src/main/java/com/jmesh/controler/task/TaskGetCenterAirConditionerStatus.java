package com.jmesh.controler.task;

import com.jmesh.controler.jni.AirConditionerConnector;
import com.jmesh.controler.jni.CenterAirConditionerConnector;
import com.jmesh.controler.util.HexUtils;

/**
 * Created by Administrator on 2018/12/12.
 */

public class TaskGetCenterAirConditionerStatus extends TaskBase {
    public TaskGetCenterAirConditionerStatus(String meterCode) {
        super(meterCode);
    }

    @Override
    public int getTaskType() {
        return 0;
    }

    @Override
    public byte[] getCmd() {
        return CenterAirConditionerConnector.getAirConditionerStatus(meterCode.getBytes());
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return CenterAirConditionerConnector.resolveAirConditionerStatus(meterCode.getBytes(), HexUtils.formatHexString(bluetoothCallback).getBytes());
    }
}
