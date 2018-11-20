package com.jmesh.controler.task;

import com.jmesh.controler.jni.SocketJniConnector;

/**
 * Created by Administrator on 2018/11/13.
 */

public class TaskSocketSwitchOff extends TaskBase {
    public TaskSocketSwitchOff(String meterCode) {
        super(meterCode);
    }

    @Override
    public int getTaskType() {
        return 0;
    }

    @Override
    public byte[] getCmd() {
        return SocketJniConnector.transCmd(meterCode.getBytes(), "97200101".getBytes());
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return new byte[0];
    }
}
