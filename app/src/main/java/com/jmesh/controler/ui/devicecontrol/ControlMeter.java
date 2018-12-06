package com.jmesh.controler.ui.devicecontrol;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jmesh.appbase.base.ToastUtils;
import com.jmesh.appbase.ui.widget.JmeshDraweeView;
import com.jmesh.appbase.ui.widget.MyToggleButton;
import com.jmesh.controler.R;
import com.jmesh.controler.base.ReadingTaskHandler;
import com.jmesh.controler.data.MeterBaseData;
import com.jmesh.controler.data.MeterData;
import com.jmesh.controler.data.dao.DBHelper;
import com.jmesh.controler.data.dao.DeviceState;
import com.jmesh.controler.task.TaskBase;
import com.jmesh.controler.task.TaskMeterGetSwitchStatus;
import com.jmesh.controler.task.TaskMeterMeterCurrent;
import com.jmesh.controler.task.TaskMeterMeterEnergyConsume;
import com.jmesh.controler.task.TaskMeterMeterFrequency;
import com.jmesh.controler.task.TaskMeterMeterPower;
import com.jmesh.controler.task.TaskMeterMeterPowerFactor;
import com.jmesh.controler.task.TaskMeterMeterVolt;
import com.jmesh.controler.task.TaskMeterSwitchOff;
import com.jmesh.controler.task.TaskMeterSwitchOn;
import com.jmesh.controler.task.TaskSocketSwitchOff;
import com.jmesh.controler.task.TaskSocketSwitchOn;
import com.jmesh.controler.ui.widget.DlgBaseData;
import com.jmesh.controler.ui.widget.GridDataContainer;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/5.
 */

public class ControlMeter extends ControlBase implements View.OnClickListener, MyToggleButton.SwitchListener {


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
        return R.layout.control_meter_x;
    }

    @Override
    public String getTitle() {
        return "蓝牙电表";
    }


    protected ConstraintLayout controlMeterTop;
    protected TextView controlMeterFirstInfo;
    protected TextView controlMeterSecondInfo;
    protected TextView controlMeterThirdInfo;
    protected MyToggleButton controlMeterSwitch;
    protected TextView controlMeterRefreshData;
    protected JmeshDraweeView controlHeadIcon;

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
        controlHeadIcon.setNativeDrawable(R.mipmap.icon_device_bd_energy_meter_big);
    }


    @Override
    public void onNavRightBnClicked() {

    }

    @Override
    protected void deviceConnectSuccess() {
        readingTaskHandler = ReadingTaskHandler.getInstance();
        readingTaskHandler.setMac(mac);
        startReadData();
    }

    ReadingTaskHandler readingTaskHandler;

    private void startReadData() {
        if (!isDeviceConneced()) {
            return;
        }
        readingTaskHandler.addTask(new TaskMeterMeterEnergyConsume(meterCode));
        readingTaskHandler.addTask(new TaskMeterMeterVolt(meterCode));
        readingTaskHandler.addTask(new TaskMeterMeterCurrent(meterCode));
        readingTaskHandler.addTask(new TaskMeterMeterFrequency(meterCode));
        readingTaskHandler.addTask(new TaskMeterMeterPower(meterCode));
        readingTaskHandler.addTask(new TaskMeterMeterPowerFactor(meterCode));
        readingTaskHandler.addTask(new TaskMeterGetSwitchStatus(meterCode));
    }

    List<GridDataContainer.Data> list = new ArrayList<>();


    @Override
    public void onDataCallback(TaskBase data) {
        super.onDataCallback(data);
        byte[] resultData = data.getResultData();
        String resultStr = new String(resultData);
        if (data instanceof TaskMeterGetSwitchStatus) {

        } else if ((data instanceof TaskMeterSwitchOn) || (data instanceof TaskSocketSwitchOn)) {
            meterData.setSwitchState(new MeterBaseData("跳合闸状态", "合闸", ""));
        } else if ((data instanceof TaskMeterSwitchOff) || (data instanceof TaskSocketSwitchOff)) {
            meterData.setSwitchState(new MeterBaseData("跳合闸状态", "跳闸", ""));
        }
        refreshMeterData();
        DBHelper.getInstance().addDeviceState(meterData.getDeviceState(meterCode));
    }

    @Override
    protected void refreshMeterData() {
        if (meterData.getEnergyConsume() != null) {
            controlMeterFirstInfo.setText(meterData.getEnergyConsume().getName() + ":" + meterData.getEnergyConsume().getValue() + meterData.getEnergyConsume().getUnit());
        }
        if (meterData.getVolt() != null) {
            controlMeterSecondInfo.setText(meterData.getVolt().getName() + ":" + meterData.getVolt().getValue() + meterData.getVolt().getUnit());
        }
        if (meterData.getCurrent() != null) {
            controlMeterThirdInfo.setText(meterData.getCurrent().getName() + ":" + meterData.getCurrent().getValue() + meterData.getCurrent().getUnit());
        }
    }

    private void initToggleButton(DeviceState deviceState) {
        if (deviceState.isSwitchState()) {
            controlMeterSwitch.setInitState(true);
        } else {
            controlMeterSwitch.setInitState(false);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.control_meter_refresh_data:
                startReadData();
                break;
            case R.id.control_meter_top:
                displayData();
                break;
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


    public void switchOff() {
        if (readingTaskHandler == null) {
            return;
        }
        readingTaskHandler.addTask(new TaskMeterSwitchOff(meterCode));
    }

    public void switchOn() {
        if (readingTaskHandler == null) {
            return;
        }
        readingTaskHandler.addTask(new TaskMeterSwitchOn(meterCode));
    }

    @Override
    public void onSwitchStateChange(View view, boolean state) {
        if (state) {
            switchOn();
        } else {
            switchOff();
        }
    }
}
