package com.jmesh.lib645.task;


import com.jmesh.lib645.jni.FourLightConnector;
import com.jmesh.lib645.util.HexUtils;

/**
 * Created by Administrator on 2018/12/13.
 */

public class TaskFourLightDisable extends TaskBase {
    byte[] control;

    public TaskFourLightDisable(String meterCode, byte control) {
        super(meterCode);
        this.control = new byte[]{control};
    }

    @Override
    public int getTaskType() {
        return 0;
    }

    @Override
    public byte[] getCmd() {
        return FourLightConnector.setLightStatedisable(meterCode.getBytes(), HexUtils.formatHexString(control).getBytes());
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return new byte[0];
    }
}
