package com.jmesh.controler.task;

import bolts.Task;

/**
 * Created by Administrator on 2018/11/6.
 */

public abstract class TaskBase {
    public static final int TASK_TYPE_CMD = 100;
    public static final int TASK_TYPE_TRANS_DATA = 101;

    public String meterCode;
    public byte[] resultData;

    public TaskBase(String meterCode) {
        this.meterCode = meterCode;
    }

    public String getMeterCode() {
        return meterCode;
    }

    public void setMeterCode(String meterCode) {
        this.meterCode = meterCode;
    }

    public byte[] getResultData() {
        return resultData;
    }

    public void setResultData(byte[] resultData) {
        this.resultData = resultData;
    }

    public abstract int getTaskType();

    public abstract byte[] getCmd();

    public abstract byte[] resolveData(byte[] bluetoothCallback);
}
