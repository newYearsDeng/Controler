package com.jmesh.lib645.task;


import com.jmesh.lib645.jni.MeterJniConnecter;
import com.jmesh.lib645.util.HexUtils;

/**
 * Created by Administrator on 2018/11/7.
 */

public abstract class TaskMeterCmd extends TaskBase {

    public TaskMeterCmd(String meterCode) {
        super(meterCode);
    }

    public abstract String getDI();

    @Override
    public int getTaskType() {
        return TASK_TYPE_CMD;
    }

    @Override
    public byte[] getCmd() {
        return MeterJniConnecter.transCmd(meterCode.getBytes(), getDI().getBytes());
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return MeterJniConnecter.resolveReult(meterCode.getBytes(), HexUtils.formatHexString(bluetoothCallback).getBytes(), getDI().getBytes());
    }
}
