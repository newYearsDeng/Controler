package com.jmesh.controler.task;

import com.jmesh.controler.jni.FourLightConnector;
import com.jmesh.controler.util.HexUtils;

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
