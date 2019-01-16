package com.jmesh.controler.ui.devicecontrol;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jmesh.appbase.base.ToastUtils;
import com.jmesh.appbase.ui.widget.JmeshDraweeView;
import com.jmesh.appbase.ui.widget.MyToggleButton;
import com.jmesh.blebase.utils.JMeshLog;
import com.jmesh.controler.R;
import com.jmesh.controler.base.ReadingTaskHandler;
import com.jmesh.controler.data.MeterBaseData;
import com.jmesh.controler.data.MeterData;
import com.jmesh.controler.data.dao.DBHelper;
import com.jmesh.controler.data.dao.DeviceState;
import com.jmesh.lib645.task.TaskBase;
import com.jmesh.lib645.task.TaskLightSwitchOff;
import com.jmesh.lib645.task.TaskLightSwitchOn;
import com.jmesh.lib645.task.TaskMeterMeterCurrent;
import com.jmesh.lib645.task.TaskMeterMeterEnergyConsume;
import com.jmesh.lib645.task.TaskMeterMeterFrequency;
import com.jmesh.lib645.task.TaskMeterMeterPower;
import com.jmesh.lib645.task.TaskMeterMeterPowerFactor;
import com.jmesh.lib645.task.TaskMeterMeterVolt;
import com.jmesh.controler.ui.widget.DlgBaseData;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/9.
 */

public class ControlLight extends ControlBase implements MyToggleButton.SwitchListener, View.OnClickListener {

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void init() {
        assignViews();
        ReadingTaskHandler.getInstance().setCallback(this);
        ReadingTaskHandler.getInstance().clearAllTask();
        connecedDevice();
        DeviceState deviceState = DBHelper.getInstance().getDeviceState(meterCode);
        if (deviceState != null) {
            meterData.init(deviceState);
            refreshMeterData();
            initToggleButton(deviceState);
        }
    }

    @Override
    public int getContentView() {
        return R.layout.control_light;
    }

    @Override
    public String getTitle() {
        return "蓝牙灯";
    }

    private ConstraintLayout controlMeterTop;
    private JmeshDraweeView controlHeadIcon;
    private View controlHeadDivision;
    private TextView controlHeadFirstInfo;
    private TextView controlHeadSecondInfo;
    private TextView controlHeadThirdInfo;
    private TextView controlHeadMore;
    private MyToggleButton controlLightSwitch;
    private View controlLightRefreshData;

    private void assignViews() {
        controlMeterTop = activity.findViewById(R.id.control_light_head);
        controlHeadDivision = activity.findViewById(R.id.control_head_division);
        controlHeadFirstInfo = activity.findViewById(R.id.control_head_first_info);
        controlHeadSecondInfo = activity.findViewById(R.id.control_head_second_info);
        controlHeadThirdInfo = activity.findViewById(R.id.control_head_third_info);
        controlHeadMore = activity.findViewById(R.id.control_head_more);
        controlLightSwitch = activity.findViewById(R.id.control_light_switch);
        controlLightRefreshData = activity.findViewById(R.id.control_light_refresh_data);
        controlLightRefreshData.setOnClickListener(this);
        controlLightSwitch.setOnSwitchListener(this);
        controlMeterTop.setOnClickListener(this);
        controlHeadIcon = activity.findViewById(R.id.control_head_icon);
        controlHeadIcon.setNativeDrawable(R.mipmap.icon_device_bd_light_big);
    }


    ReadingTaskHandler readingTaskHandler;

    @Override
    public void onNavRightBnClicked() {

    }

    @Override
    protected void deviceConnectSuccess() {
        readingTaskHandler = ReadingTaskHandler.getInstance();
        readingTaskHandler.setMac(mac);
        readingTaskHandler.setCallback(this);
        startReadData();
    }

    private void startReadData() {
        if (readingTaskHandler == null || (!isDeviceConneced())) {
            return;
        }
        readingTaskHandler.addTask(new TaskMeterMeterEnergyConsume(meterCode));
        readingTaskHandler.addTask(new TaskMeterMeterVolt(meterCode));
        readingTaskHandler.addTask(new TaskMeterMeterCurrent(meterCode));
        readingTaskHandler.addTask(new TaskMeterMeterFrequency(meterCode));
        readingTaskHandler.addTask(new TaskMeterMeterPower(meterCode));
        readingTaskHandler.addTask(new TaskMeterMeterPowerFactor(meterCode));
    }

    @Override
    public void onSwitchStateChange(View view, boolean state) {
        if (state) {
            switchOn();
        } else {
            switchOff();
        }
    }

    private void switchOn() {
        if (!isDeviceConneced()) {
            return;
        }
        ReadingTaskHandler readingTaskHandler = ReadingTaskHandler.getInstance();
        readingTaskHandler.setMac(mac);
        readingTaskHandler.setCallback(this);
        readingTaskHandler.addTask(new TaskLightSwitchOn(meterCode));
    }

    private void switchOff() {
        if (!isDeviceConneced()) {
            return;
        }
        ReadingTaskHandler readingTaskHandler = ReadingTaskHandler.getInstance();
        readingTaskHandler.setMac(mac);
        readingTaskHandler.setCallback(this);
        readingTaskHandler.addTask(new TaskLightSwitchOff(meterCode));
    }

    @Override
    public void onDataCallback(TaskBase data) {
        super.onDataCallback(data);
        byte[] resultData = data.getResultData();
        String resultStr = new String(resultData);
        if (data instanceof TaskLightSwitchOn) {
            meterData.setSwitchState(new MeterBaseData("跳合闸状态", "合闸", ""));
        } else if (data instanceof TaskLightSwitchOff) {
            meterData.setSwitchState(new MeterBaseData("跳合闸状态", "跳闸", ""));
        }
        refreshMeterData();
        DBHelper.getInstance().addDeviceState(meterData.getDeviceState(meterCode));
    }

    @Override
    protected void refreshMeterData() {
        if (meterData.getEnergyConsume() != null) {
            controlHeadFirstInfo.setText(meterData.getEnergyConsume().getName() + ":" + meterData.getEnergyConsume().getValue() + meterData.getEnergyConsume().getUnit());
        }
        if (meterData.getVolt() != null) {
            controlHeadSecondInfo.setText(meterData.getVolt().getName() + ":" + meterData.getVolt().getValue() + meterData.getVolt().getUnit());
        }
        if (meterData.getCurrent() != null) {
            controlHeadThirdInfo.setText(meterData.getCurrent().getName() + ":" + meterData.getCurrent().getValue() + meterData.getCurrent().getUnit());
        }
    }

    private void initToggleButton(DeviceState deviceState) {
        if (deviceState.isSwitchState()) {
            controlLightSwitch.setInitState(true);
        } else {
            controlLightSwitch.setInitState(false);
        }

    }

    private void displayData() {
        DlgBaseData dlgBaseData = new DlgBaseData(activity);
        dlgBaseData.setData(getDataList());
        dlgBaseData.show();
    }

    private List<String> getDataList() {
        List<String> data = new ArrayList<>();
        if (meterData == null) {
            return data;
        }
        if (meterData.getEnergyConsume() != null) {
            data.add(getDisplay(meterData.getEnergyConsume()));
        }
        if (meterData.getVolt() != null) {
            data.add(getDisplay(meterData.getVolt()));
        }
        if (meterData.getCurrent() != null) {
            data.add(getDisplay(meterData.getCurrent()));
        }
        if (meterData.getFrequency() != null) {
            data.add(getDisplay(meterData.getFrequency()));
        }
        if (meterData.getPower() != null) {
            data.add(getDisplay(meterData.getPower()));
        }
        if (meterData.getPowerFactor() != null) {
            data.add(getDisplay(meterData.getPowerFactor()));
        }
        if (meterData.getSwitchState() != null) {
            data.add(getDisplay(meterData.getSwitchState()));
        }
        return data;
    }

    private String getDisplay(MeterBaseData meterBaseData) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(meterBaseData.getName());
        stringBuilder.append(":");
        stringBuilder.append(meterBaseData.getValue());
        stringBuilder.append(meterBaseData.getUnit());
        return stringBuilder.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.control_light_head:
                displayData();
                break;
            case R.id.control_light_refresh_data:
                refreshData();
                break;
        }
    }

    public void refreshData() {
        startReadData();
    }


}
