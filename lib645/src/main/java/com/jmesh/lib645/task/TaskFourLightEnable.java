package com.jmesh.lib645.task;


import com.jmesh.lib645.jni.FourLightConnector;
import com.jmesh.lib645.util.HexUtils;

/**
 * Created by Administrator on 2018/12/12.
 */

public class TaskFourLightEnable extends TaskBase {
    byte[] control;

    public TaskFourLightEnable(String meterCode, byte control) {
        super(meterCode);
        this.control = new byte[]{control};
    }

    @Override
    public int getTaskType() {
        return 0;
    }

    @Override
    public byte[] getCmd() {
        return FourLightConnector.setLightStateEnable(meterCode.getBytes(), HexUtils.formatHexString(control).getBytes());
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return new byte[0];
    }
}
