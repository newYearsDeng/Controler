package com.jmesh.lib645.task;


import com.jmesh.lib645.jni.MeterJniConnecter;
import com.jmesh.lib645.util.HexUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/11/7.
 */

public abstract class TaskMeterSwitch extends TaskBase {

    public TaskMeterSwitch(String meterCode) {
        super(meterCode);
    }

    public abstract byte[] getData();

    @Override
    public int getTaskType() {
        return TASK_TYPE_TRANS_DATA;
    }

    @Override
    public byte[] resolveData(byte[] bluetoothCallback) {
        return MeterJniConnecter.resolveMeterSwitchCmd(meterCode.getBytes(), HexUtils.formatHexString(bluetoothCallback).getBytes());
    }

    @Override
    public byte[] getCmd() {
        return MeterJniConnecter.getMeterSwitchCmd(meterCode.getBytes(), getData());
    }


    public static String getN1Flag(boolean flag) {//跳合闸标志位
        if (flag) {//合闸
            return "1B";
        } else {//跳闸
            return "1A";
        }
    }

    public static String getN2() {
        return "00";
    }

    public static String getTime() {
        long now = System.currentTimeMillis() + 1000 * 60;//1分钟的延迟效果
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ssmmHHddMMyy");
        return simpleDateFormat.format(new Date(now));
    }


}
