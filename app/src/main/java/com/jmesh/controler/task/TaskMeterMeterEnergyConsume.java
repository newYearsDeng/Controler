package com.jmesh.controler.task;

import com.jmesh.controler.jni.MeterJniConnecter;

/**
 * Created by Administrator on 2018/11/6.
 */

public class TaskMeterMeterEnergyConsume extends TaskMeterCmd {

    public TaskMeterMeterEnergyConsume(String meterCode) {
        super(meterCode);
    }

    @Override
    public String getDI() {
        return MeterJniConnecter.METER_DI_ENERGY_CONSUM;
    }
}
