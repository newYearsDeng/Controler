package com.jmesh.lib645.task;

/**
 * Created by Administrator on 2018/11/7.
 */

public class TaskMeterSwitchOn extends TaskMeterSwitch {

    public TaskMeterSwitchOn(String meterCode) {
        super(meterCode);
    }

    @Override
    public byte[] getData() {
        String dataStr = new StringBuilder().append(getN1Flag(true)).append(getN2()).append(getTime()).toString();
        return dataStr.getBytes();
    }

}
