package com.jmesh.controler.task;

import com.jmesh.controler.jni.AirConditionerConnector;

/**
 * Created by Administrator on 2018/11/12.
 */

public class TaskAirConditioner extends TaskBase {


    public TaskAirConditioner(String meterCode) {
        super(meterCode);
    }

    byte[] code;

    public void setCode(byte[] bytes) {
        this.code = bytes;
    }

    @Override
    public int getTaskType() {
        return 0;
    }

    @Override
    public byte[] getCmd() {
        return AirConditionerConnector.getAirConditionerCmd(meterCode.getBytes(), code);
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return new byte[0];
    }
}
