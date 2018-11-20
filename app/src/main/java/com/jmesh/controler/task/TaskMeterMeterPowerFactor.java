package com.jmesh.controler.task;

import com.jmesh.controler.jni.MeterJniConnecter;

/**
 * Created by Administrator on 2018/11/7.
 */

public class TaskMeterMeterPowerFactor extends TaskMeterCmd {
    public TaskMeterMeterPowerFactor(String meterCode) {
        super(meterCode);
    }

    @Override
    public String getDI() {
        return MeterJniConnecter.METER_DI_POWER_FACTOR;
    }
}
