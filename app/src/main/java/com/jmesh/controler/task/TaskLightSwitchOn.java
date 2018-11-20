package com.jmesh.controler.task;

import com.jmesh.controler.jni.LightJniConnector;

/**
 * Created by Administrator on 2018/11/9.
 */

public class TaskLightSwitchOn extends TaskBase {
    public TaskLightSwitchOn(String meterCode) {
        super(meterCode);
    }

    @Override
    public int getTaskType() {
        return 0;
    }

    @Override
    public byte[] getCmd() {
        return LightJniConnector.getLightSwitchCmd(meterCode.getBytes(), "01".getBytes());
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return new byte[0];
    }
}
