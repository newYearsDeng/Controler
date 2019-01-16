package com.jmesh.lib645.task;


import com.jmesh.lib645.jni.SocketJniConnector;

/**
 * Created by Administrator on 2018/11/13.
 */

public class TaskSocketSwitchOn extends TaskBase {
    public TaskSocketSwitchOn(String meterCode) {
        super(meterCode);
    }

    @Override
    public int getTaskType() {
        return 0;
    }

    @Override
    public byte[] getCmd() {
        return SocketJniConnector.transCmd(meterCode.getBytes(), "97200102".getBytes());
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return new byte[0];
    }
}
