package com.jmesh.controler.task;

import com.jmesh.controler.jni.MeterJniConnecter;

/**
 * Created by Administrator on 2018/11/19.
 */

public class TaskMeterGetSwitchStatus extends TaskMeterCmd {
    public TaskMeterGetSwitchStatus(String meterCode) {
        super(meterCode);
    }

    @Override
    public String getDI() {
        return MeterJniConnecter.METER_DI_GET_SWITCH_STATE;
    }
}
