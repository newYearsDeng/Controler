package com.jmesh.controler.task;

import com.jmesh.controler.jni.FourLightConnector;
import com.jmesh.controler.util.HexUtils;

/**
 * Created by Administrator on 2018/12/12.
 */

public class TaskGetFourLightState extends TaskBase {

    public TaskGetFourLightState(String meterCode) {
        super(meterCode);
    }

    @Override
    public int getTaskType() {
        return 0;
    }

    @Override
    public byte[] getCmd() {
        return FourLightConnector.getLightSwitchStateCmd(meterCode.getBytes());
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return FourLightConnector.resolveLightSwitchStateResult(meterCode.getBytes(), HexUtils.formatHexString(bluetoothCallback).getBytes());
    }
}
