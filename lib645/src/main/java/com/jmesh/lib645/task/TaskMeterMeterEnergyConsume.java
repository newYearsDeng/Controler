package com.jmesh.lib645.task;


import com.jmesh.lib645.jni.MeterJniConnecter;

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
