package com.jmesh.lib645.task;

import com.jmesh.lib645.jni.MeterJniConnecter;

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
