package com.jmesh.controler.ui.devicecontrol;

import com.jmesh.controler.R;
import com.jmesh.controler.task.TaskMeterGetSwitchStatus;
import com.jmesh.controler.task.TaskSocketSwitchOff;
import com.jmesh.controler.task.TaskSocketSwitchOn;

/**
 * Created by Administrator on 2018/11/13.
 */

public class ControlSocket extends ControlMeter {
    @Override
    public void switchOff() {
        if (readingTaskHandler == null || (!isDeviceConneced())) {
            return;
        }
        readingTaskHandler.addTask(new TaskSocketSwitchOff(meterCode));
        readingTaskHandler.addTask(new TaskMeterGetSwitchStatus(meterCode));
    }

    @Override
    public String getTitle() {
        return "蓝牙插座";
    }

    @Override
    protected void assignViews() {
        controlMeterTop = activity.findViewById(R.id.control_meter_top);
        controlMeterFirstInfo = activity.findViewById(R.id.control_head_first_info);
        controlMeterSecondInfo = activity.findViewById(R.id.control_head_second_info);
        controlMeterThirdInfo = activity.findViewById(R.id.control_head_third_info);
        controlMeterSwitch = activity.findViewById(R.id.control_meter_switch);
        controlMeterRefreshData = activity.findViewById(R.id.control_meter_refresh_data);
        controlMeterSwitch.setOnSwitchListener(this);
        controlMeterRefreshData.setOnClickListener(this);
        controlMeterTop.setOnClickListener(this);
        controlHeadIcon = activity.findViewById(R.id.control_head_icon);
        controlHeadIcon.setNativeDrawable(R.mipmap.icon_device_bd_socket_big);
    }


    @Override
    public void switchOn() {
        if (readingTaskHandler == null || (!isDeviceConneced())) {
            return;
        }
        readingTaskHandler.addTask(new TaskSocketSwitchOn(meterCode));
        readingTaskHandler.addTask(new TaskMeterGetSwitchStatus(meterCode));
    }
}
