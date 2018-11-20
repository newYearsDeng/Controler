package com.jmesh.controler.task;

/**
 * Created by Administrator on 2018/11/7.
 */

public class TaskMeterSwitchOff extends TaskMeterSwitch {

    public TaskMeterSwitchOff(String meterCode) {
        super(meterCode);
    }

    @Override
    public byte[] getData() {
        String dataStr = new StringBuilder().append(getN1Flag(false)).append(getN2()).append(getTime()).toString();
        return dataStr.getBytes();
    }


}
