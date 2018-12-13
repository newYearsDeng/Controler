package com.jmesh.controler.ui.devicecontrol;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jmesh.appbase.ui.widget.JmeshDraweeView;
import com.jmesh.appbase.ui.widget.MyToggleButton;
import com.jmesh.appbase.utils.StringUtil;
import com.jmesh.blebase.utils.HexUtils;
import com.jmesh.blebase.utils.JMeshLog;
import com.jmesh.controler.R;
import com.jmesh.controler.base.ReadingTaskHandler;
import com.jmesh.controler.data.BluetoothData;
import com.jmesh.controler.data.FourLightState;
import com.jmesh.controler.data.MeterBaseData;
import com.jmesh.controler.data.dao.DBHelper;
import com.jmesh.controler.data.dao.DeviceState;
import com.jmesh.controler.task.TaskBase;
import com.jmesh.controler.task.TaskFourLightDisable;
import com.jmesh.controler.task.TaskFourLightEnable;
import com.jmesh.controler.task.TaskGetCenterAirConditionerStatus;
import com.jmesh.controler.task.TaskGetFourLightState;
import com.jmesh.controler.task.TaskMeterGetSwitchStatus;
import com.jmesh.controler.task.TaskMeterMeterCurrent;
import com.jmesh.controler.task.TaskMeterMeterEnergyConsume;
import com.jmesh.controler.task.TaskMeterMeterFrequency;
import com.jmesh.controler.task.TaskMeterMeterPower;
import com.jmesh.controler.task.TaskMeterMeterPowerFactor;
import com.jmesh.controler.task.TaskMeterMeterVolt;
import com.jmesh.controler.ui.widget.DlgBaseData;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/12.
 */

public class ControlFourLight extends ControlBase implements View.OnClickListener, MyToggleButton.SwitchListener {
    @Override
    public void onNavRightBnClicked() {

    }

    @Override
    protected void deviceConnectSuccess() {
        readingTaskHandler = ReadingTaskHandler.getInstance();
        readingTaskHandler.setMac(mac);
        startReadData();
    }

    private void startReadData() {
        if (!isDeviceConneced()) {
            return;
        }
        ReadingTaskHandler.getInstance().addTask(new TaskGetCenterAirConditionerStatus(meterCode));
    }

    ReadingTaskHandler readingTaskHandler;

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
        }
    }

    protected ConstraintLayout controlMeterTop;
    protected TextView controlMeterFirstInfo;
    protected TextView controlMeterSecondInfo;
    protected TextView controlMeterThirdInfo;
    private MyToggleButton controlFourLightSwitch;
    private TextView controlFourLightData;
    private MyToggleButton leftSwitchButton;
    private MyToggleButton rightSwitchButton;
    private MyToggleButton topSwitchButton;
    private MyToggleButton bottomSwitchButton;
    private JmeshDraweeView controlHeadIcon;

    private void assignViews() {
        controlMeterTop = activity.findViewById(R.id.control_four_light_top);
        controlHeadIcon = activity.findViewById(R.id.control_head_icon);
        controlMeterFirstInfo = activity.findViewById(R.id.control_head_first_info);
        controlMeterSecondInfo = activity.findViewById(R.id.control_head_second_info);
        controlMeterThirdInfo = activity.findViewById(R.id.control_head_third_info);
        controlFourLightSwitch = (MyToggleButton) getActivity().findViewById(R.id.control_four_light_switch);
        controlFourLightData = (TextView) getActivity().findViewById(R.id.control_four_light_data);
        leftSwitchButton = (MyToggleButton) getActivity().findViewById(R.id.left_switch_button);
        rightSwitchButton = (MyToggleButton) getActivity().findViewById(R.id.right_switch_button);
        topSwitchButton = (MyToggleButton) getActivity().findViewById(R.id.top_switch_button);
        bottomSwitchButton = (MyToggleButton) getActivity().findViewById(R.id.bottom_switch_button);
        controlFourLightData.setOnClickListener(this);
        controlMeterTop.setOnClickListener(this);
        controlHeadIcon = activity.findViewById(R.id.control_head_icon);
        controlHeadIcon.setNativeDrawable(R.mipmap.icon_device_bd_for_light_big);

        leftSwitchButton.setOnSwitchListener(this);
        topSwitchButton.setOnSwitchListener(this);
        rightSwitchButton.setOnSwitchListener(this);
        bottomSwitchButton.setOnSwitchListener(this);
        controlFourLightSwitch.setOnSwitchListener(this);
    }

    @Override
    public int getContentView() {
        return R.layout.control_four_light;
    }

    @Override
    public String getTitle() {
        return getActivity().getString(R.string.four_light);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.control_four_light_data:
                startReadData();
                break;
            case R.id.control_four_light_top:
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

    @Override
    public void onSwitchStateChange(View view, boolean state) {
        switch (view.getId()) {
            case R.id.left_switch_button:
                onLeftSwitch(state);
                break;
            case R.id.top_switch_button:
                onTopSwitch(state);
                break;
            case R.id.right_switch_button:
                onRightSwitch(state);
                break;
            case R.id.bottom_switch_button:
                onBottomSwitch(state);
                break;
            case R.id.control_four_light_switch:
                onFourLightSwitch(state);
                break;
        }
    }

    FourLightState fourLightState;

    public FourLightState getFourLightState() {
        if (fourLightState == null) {
            fourLightState = new FourLightState();
        }
        return fourLightState;
    }

    public static final byte LEFT_MASK = (byte) 1;
    public static final byte TOP_MASK = (byte) 2;
    public static final byte RIGHT_MASK = (byte) 4;
    public static final byte BOTTOM_MASK = (byte) 8;
    public static final byte ALL_MASK = (byte) 15;

    private void onLeftSwitch(boolean state) {
        TaskBase taskBase = getTask(state, LEFT_MASK);
        ReadingTaskHandler.getInstance().addTask(taskBase);
    }

    private TaskBase getTask(boolean enable, byte control) {
        TaskBase taskBase = null;
        if (enable) {
            taskBase = new TaskFourLightEnable(meterCode, control);
        } else {
            taskBase = new TaskFourLightDisable(meterCode, control);
        }
        return taskBase;
    }

    private void onTopSwitch(boolean state) {
        TaskBase taskBase = getTask(state, TOP_MASK);
        ReadingTaskHandler.getInstance().addTask(taskBase);
    }

    private void onRightSwitch(boolean state) {
        TaskBase taskBase = getTask(state, RIGHT_MASK);
        ReadingTaskHandler.getInstance().addTask(taskBase);
    }

    private void onBottomSwitch(boolean state) {
        TaskBase taskBase = getTask(state, BOTTOM_MASK);
        ReadingTaskHandler.getInstance().addTask(taskBase);
    }



    private void onFourLightSwitch(boolean state) {
        TaskBase taskBase = getTask(state, ALL_MASK);
        ReadingTaskHandler.getInstance().addTask(taskBase);
        leftSwitchButton.setInitState(state);
        rightSwitchButton.setInitState(state);
        bottomSwitchButton.setInitState(state);
        topSwitchButton.setInitState(state);
    }


    @Override
    public void onDataCallback(TaskBase data) {
        byte[] resultData = data.getResultData();
        String resultStr = new String(resultData);
        if (data instanceof TaskGetCenterAirConditionerStatus) {
            BluetoothData bluetoothData = new BluetoothData(resultStr);
            meterData = bluetoothData.getMeterData();
            refreshMeterData();
            initButton(bluetoothData);
        }
    }

    private void initButton(BluetoothData bluetoothData) {
        if (bluetoothData == null) {
            return;
        }
        String fourLightStatusStr = bluetoothData.getData();
        byte[] fourLightStatusBytes = HexUtils.hexStringToBytes(fourLightStatusStr);
        byte fourLightStatus = fourLightStatusBytes[0];
        if (initLeftLight(fourLightStatus) &&
                initRightLight(fourLightStatus) &&
                initTopLight(fourLightStatus) &&
                initBottomLight(fourLightStatus)) {
            controlFourLightSwitch.setInitState(true);
        } else {
            controlFourLightSwitch.setInitState(false);
        }

    }

    private boolean initLeftLight(byte fourLightStatus) {
        if ((fourLightStatus & (LEFT_MASK)) == LEFT_MASK) {
            leftSwitchButton.setInitState(true);
            return true;
        } else {
            leftSwitchButton.setInitState(false);
            return false;
        }
    }

    private boolean initRightLight(byte fourLightStatus) {
        if ((fourLightStatus & (RIGHT_MASK)) == RIGHT_MASK) {
            rightSwitchButton.setInitState(true);
            return true;
        } else {
            rightSwitchButton.setInitState(false);
            return false;
        }
    }

    private boolean initTopLight(byte fourLightStatus) {
        if ((fourLightStatus & (TOP_MASK)) == TOP_MASK) {
            topSwitchButton.setInitState(true);
            return true;
        } else {
            topSwitchButton.setInitState(false);
            return false;
        }
    }

    private boolean initBottomLight(byte fourLightStatus) {
        if ((fourLightStatus & (BOTTOM_MASK)) == BOTTOM_MASK) {
            bottomSwitchButton.setInitState(true);
            return true;
        } else {
            bottomSwitchButton.setInitState(false);
            return false;
        }
    }


}
