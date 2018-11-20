package com.jmesh.controler.task;

import com.jmesh.controler.jni.MeterJniConnecter;

/**
 * Created by Administrator on 2018/11/7.
 */

public class TaskMeterMeterFrequency extends TaskMeterCmd {
    public TaskMeterMeterFrequency(String meterCode) {
        super(meterCode);
    }

    @Override
    public String getDI() {
        return MeterJniConnecter.METER_DI_FREQENCY;
    }
}
