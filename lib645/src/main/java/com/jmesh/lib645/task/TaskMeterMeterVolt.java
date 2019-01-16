package com.jmesh.lib645.task;


import com.jmesh.lib645.jni.MeterJniConnecter;

/**
 * Created by Administrator on 2018/11/7.
 */

public class TaskMeterMeterVolt extends TaskMeterCmd {
    public TaskMeterMeterVolt(String meterCode) {
        super(meterCode);
    }

    @Override
    public String getDI() {
        return MeterJniConnecter.METER_DI_VOLTAGE;
    }
}
