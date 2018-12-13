package com.jmesh.controler.task;

import com.jmesh.appbase.utils.StringUtil;
import com.jmesh.controler.data.AirConditionerData;
import com.jmesh.controler.jni.CenterAirConditionerConnector;
import com.jmesh.controler.util.HexUtils;

/**
 * Created by Administrator on 2018/12/13.
 */

public class TaskCenterAirconditionerSetModeNTemp extends TaskBase {
    int mode;
    int temperture;
    public static final String DI = "bcaa0001";

    public TaskCenterAirconditionerSetModeNTemp(String meterCode, int mode, int temperture) {
        super(meterCode);
        this.mode = mode;
        this.temperture = temperture;
    }

    @Override
    public int getTaskType() {
        return 0;
    }

    @Override
    public byte[] getCmd() {
        String taskData = null;
        switch (mode) {
            case AirConditionerData.modeRefrigeration:
                taskData = "00" +Integer.toString(temperture);
                break;
            case AirConditionerData.modeHeat:
                taskData = "01" + Integer.toString(temperture);
                break;
            default:
                taskData = "00" + Integer.toString(temperture);
                break;
        }
        return CenterAirConditionerConnector.setAirConditioner(meterCode.getBytes(), DI.getBytes(), taskData.getBytes());
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return new byte[0];
    }
}
