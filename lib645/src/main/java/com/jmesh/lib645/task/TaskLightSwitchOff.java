package com.jmesh.lib645.task;


import com.jmesh.lib645.jni.LightJniConnector;

/**
 * Created by Administrator on 2018/11/9.
 */

public class TaskLightSwitchOff extends TaskBase {
    public TaskLightSwitchOff(String meterCode) {
        super(meterCode);
    }

    @Override
    public int getTaskType() {
        return 0;
    }

    @Override
    public byte[] getCmd() {
        return LightJniConnector.getLightSwitchCmd(meterCode.getBytes(), "00".getBytes());
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return new byte[0];
    }
}
