package com.jmesh.controler.ui.devicecontrol;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.jmesh.appbase.ui.widget.JmeshDraweeView;
import com.jmesh.appbase.ui.widget.MyToggleButton;
import com.jmesh.blebase.utils.JMeshLog;
import com.jmesh.lib645.AirConditionerData;
import com.jmesh.controler.R;
import com.jmesh.controler.base.ReadingTaskHandler;
import com.jmesh.controler.data.BluetoothData;
import com.jmesh.controler.data.MeterBaseData;
import com.jmesh.controler.data.dao.DBHelper;
import com.jmesh.controler.data.dao.DeviceState;
import com.jmesh.lib645.task.TaskBase;
import com.jmesh.lib645.task.TaskCenterAirconditionerSetModeNTemp;
import com.jmesh.lib645.task.TaskCenterAirconditionerSetPower;
import com.jmesh.lib645.task.TaskCenterAirconditionerSetWindStrength;
import com.jmesh.lib645.task.TaskGetCenterAirConditionerStatus;
import com.jmesh.controler.ui.widget.DlgBaseData;
import com.jmesh.controler.ui.widget.DlgChooseMode;
import com.jmesh.controler.ui.widget.DlgChooseWindStrength;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/13.
 */

public class ControlCenterAirConditioner extends ControlBase implements View.OnClickListener {
    @Override
    public void onNavRightBnClicked() {

    }


    @Override
    protected void deviceConnectSuccess() {
        ReadingTaskHandler.getInstance().setMac(mac);
        startReadData();
    }

    private void startReadData() {
        if (!isDeviceConneced()) {
            return;
        }
        ReadingTaskHandler.getInstance().addTask(new TaskGetCenterAirConditionerStatus(meterCode));
    }

    @Override
    public void onDestroy() {

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
        initEvent();
        initAirConditionerData();
    }

    private void assignViews() {
        controlMeterTop = activity.findViewById(R.id.control_air_conditioner_head);
        controlHeadIcon = activity.findViewById(R.id.control_head_icon);
        controlHeadIcon.setNativeDrawable(R.mipmap.icon_device_bd_water_air_conditioner_big);
        controlHeadDivision = activity.findViewById(R.id.control_head_division);
        controlHeadFirstInfo = activity.findViewById(R.id.control_head_first_info);
        controlHeadSecondInfo = activity.findViewById(R.id.control_head_second_info);
        controlHeadThirdInfo = activity.findViewById(R.id.control_head_third_info);
        controlHeadMore = activity.findViewById(R.id.control_head_more);
        controlAirConditionerMinus = activity.findViewById(R.id.control_air_conditioner_minus);
        controlAirConditionerSwitch = activity.findViewById(R.id.control_air_conditioner_switch);
        controlAirConditionerPlus = activity.findViewById(R.id.control_air_conditioner_plus);
        controlAirConditionerPower = activity.findViewById(R.id.control_air_conditioner_power);
        controlAirConditionerSignal = activity.findViewById(R.id.control_air_conditioner_signal);
        controlAirConditionerFreshData = activity.findViewById(R.id.control_air_conditioner_fresh_data);
        controlAirConditionerMode = activity.findViewById(R.id.control_air_conditioner_mode);
        controlAirConditionerWindStrength = activity.findViewById(R.id.control_air_conditioner_wind_strength);
        controlAirConditionerHorizontalScavenging = activity.findViewById(R.id.control_air_conditioner_horizontal_scavenging);
        controlAirConditionerVerticalScavenging = activity.findViewById(R.id.control_air_conditioner_vertical_scavenging);
        tempertureTextView = activity.findViewById(R.id.control_head_top_text);
        tempertureTextView.setVisibility(View.VISIBLE);
        controlHeadIcon.setVisibility(View.INVISIBLE);
        controlAirConditionerHorizontalScavenging.setVisibility(View.GONE);
        controlAirConditionerVerticalScavenging.setVisibility(View.GONE);
    }

    private void initEvent() {
        controlAirConditionerMinus.setOnClickListener(this);
        controlAirConditionerPlus.setOnClickListener(this);
        controlAirConditionerPower.setOnClickListener(this);
        controlAirConditionerFreshData.setOnClickListener(this);
        controlAirConditionerMode.setOnClickListener(this);
        controlAirConditionerWindStrength.setOnClickListener(this);
        controlMeterTop.setOnClickListener(this);
        controlAirConditionerSignal.setOnClickListener(this);
    }

    AirConditionerData airConditionerData;

    private void initAirConditionerData() {
        airConditionerData = new AirConditionerData();
        airConditionerData.setPower(true);
        airConditionerData.setMode(AirConditionerData.modeRefrigeration);
        airConditionerData.setTemperture(20);
        airConditionerData.setVerticalScavenging(true);
        airConditionerData.setHorizontalScavenging(true);
        airConditionerData.setWindStrength(AirConditionerData.windStrengthMidLevel);
        refreshView();
    }

    private void refreshView() {
        if (airConditionerData == null) {
            return;
        }
        refreshTemperture(airConditionerData.temperture);
        refreshPower(airConditionerData.isPower());
        refreshMode(airConditionerData.mode);
        refreshWindStrength(airConditionerData.windStrength);
    }


    private ConstraintLayout controlMeterTop;
    private JmeshDraweeView controlHeadIcon;
    private View controlHeadDivision;
    private TextView controlHeadFirstInfo;
    private TextView controlHeadSecondInfo;
    private TextView controlHeadThirdInfo;
    private TextView controlHeadMore;
    private JmeshDraweeView controlAirConditionerMinus;
    private MyToggleButton controlAirConditionerSwitch;
    private JmeshDraweeView controlAirConditionerPlus;
    private JmeshDraweeView controlAirConditionerPower;
    private JmeshDraweeView controlAirConditionerSignal;
    private JmeshDraweeView controlAirConditionerFreshData;
    private JmeshDraweeView controlAirConditionerMode;
    private JmeshDraweeView controlAirConditionerWindStrength;
    private JmeshDraweeView controlAirConditionerHorizontalScavenging;
    private JmeshDraweeView controlAirConditionerVerticalScavenging;
    private TextView tempertureTextView;

    @Override
    public int getContentView() {
        return R.layout.control_air_conditioner;
    }

    @Override
    public String getTitle() {
        return getActivity().getString(R.string.center_airconditioner);
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
            case R.id.control_air_conditioner_fresh_data:
                refreshData();
                break;
            case R.id.control_air_conditioner_head:
                displayData();
                break;
            case R.id.control_air_conditioner_minus:
                minsTemperture();
                break;
            case R.id.control_air_conditioner_plus:
                plusTemperture();
                break;
            case R.id.control_air_conditioner_mode:
                chooseMode();
                break;
            case R.id.control_air_conditioner_wind_strength:
                changeWindStrength();
                break;
            case R.id.control_air_conditioner_power:
                changePower();
                break;
            case R.id.control_air_conditioner_signal:
                shake();
                break;
        }
    }

    private void changePower() {
        if (airConditionerData == null) {
            return;
        }
        JMeshLog.e("现在的状态", airConditionerData.isPower() ? "关" : "开");
        TaskBase taskBase = new TaskCenterAirconditionerSetPower(meterCode, !airConditionerData.isPower());
        ReadingTaskHandler.getInstance().addTask(taskBase);
        airConditionerData.setPower(!airConditionerData.isPower());
        refreshView();
    }

    private void minsTemperture() {
        if (airConditionerData.getTemperture() > 20) {
            airConditionerData.setTemperture(airConditionerData.getTemperture() - 1);
        }
        TaskBase taskBase = new TaskCenterAirconditionerSetModeNTemp(meterCode, airConditionerData.mode, airConditionerData.temperture);
        ReadingTaskHandler.getInstance().addTask(taskBase);
        refreshView();
    }

    private void plusTemperture() {
        if (airConditionerData.getTemperture() < 30) {
            airConditionerData.setTemperture(airConditionerData.getTemperture() + 1);
        }
        TaskBase taskBase = new TaskCenterAirconditionerSetModeNTemp(meterCode, airConditionerData.mode, airConditionerData.temperture);
        ReadingTaskHandler.getInstance().addTask(taskBase);
        refreshView();
    }

    private void chooseMode() {
        if (airConditionerData == null) {
            return;
        }
        DlgChooseMode dlgChooseMode = new DlgChooseMode(getActivity());
        dlgChooseMode.disableAuto();
        dlgChooseMode.disableBlow();
        dlgChooseMode.disableHumidification();
        dlgChooseMode.setChooseCallback(new DlgChooseMode.ModeChooseCallback() {
            @Override
            public void onChoose(int mode) {
                airConditionerData.setMode(mode);
                TaskBase taskBase = new TaskCenterAirconditionerSetModeNTemp(meterCode, airConditionerData.mode, airConditionerData.temperture);
                ReadingTaskHandler.getInstance().addTask(taskBase);
                refreshView();
            }
        });
        dlgChooseMode.show();

    }

    private void changeWindStrength() {
        if (airConditionerData == null) {
            return;
        }
        DlgChooseWindStrength dlgChooseWindStrength = new DlgChooseWindStrength(getActivity());
        dlgChooseWindStrength.setChooseCallback(new DlgChooseWindStrength.WindStrengthChooseCallback() {
            @Override
            public void onChoose(int windStrength) {
                airConditionerData.setWindStrength(windStrength);
                TaskBase taskBase = new TaskCenterAirconditionerSetWindStrength(meterCode, airConditionerData.windStrength);
                ReadingTaskHandler.getInstance().addTask(taskBase);
                refreshView();
            }
        });
        dlgChooseWindStrength.show();
    }

    private void refreshTemperture(int temperture) {
        tempertureTextView.setText(new StringBuilder().append(Integer.toString(temperture)).append(activity.getString(R.string.temperture_unit)));
    }

    private void refreshPower(boolean enable) {
        if (enable) {
            controlAirConditionerPower.setNativeDrawable(R.mipmap.icon_airconditioner_power_pressed);
        } else {
            controlAirConditionerPower.setNativeDrawable(R.mipmap.icon_airconditioner_power_unpressed);
        }
    }

    private void shake() {
        TranslateAnimation translateAnimation = new TranslateAnimation(-5, 5, 0, 0);
        translateAnimation.setRepeatCount(10);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        controlAirConditionerSignal.startAnimation(translateAnimation);
    }

    private void refreshMode(int mode) {
        switch (mode) {
            case AirConditionerData.modeAuto:
                controlAirConditionerMode.setNativeDrawable(R.mipmap.icon_airconditioner_wind_strength_auto_unpressed);
                break;
            case AirConditionerData.modeHumidification:
                controlAirConditionerMode.setNativeDrawable(R.mipmap.icon_airconditioner_dehumidification_unpressed);
                break;
            case AirConditionerData.modeRefrigeration:
                controlAirConditionerMode.setNativeDrawable(R.mipmap.icon_airconditioner_refrigeration_unpressed);
                break;
            case AirConditionerData.modeHeat:
                controlAirConditionerMode.setNativeDrawable(R.mipmap.icon_airconditioner_heat_unpressed);
                break;
            case AirConditionerData.modeSimpleBlow:
                controlAirConditionerMode.setNativeDrawable(R.mipmap.icon_airconditioner_fan_unpressed);
                break;
        }
    }

    private void refreshWindStrength(int windStrength) {
        switch (windStrength) {
            case AirConditionerData.windStrengthAuto:
                controlAirConditionerWindStrength.setNativeDrawable(R.mipmap.icon_airconditioner_wind_strength_auto_unpressed);
                break;
            case AirConditionerData.windStrengthLowLevel:
                controlAirConditionerWindStrength.setNativeDrawable(R.mipmap.icon_airconditioner_wind_strength_low_unpressed);
                break;
            case AirConditionerData.windStrengthMidLevel:
                controlAirConditionerWindStrength.setNativeDrawable(R.mipmap.icon_airconditioner_wind_strength_mid_unpressed);
                break;
            case AirConditionerData.windStrengthHighLevel:
                controlAirConditionerWindStrength.setNativeDrawable(R.mipmap.icon_airconditioner_wind_strength_high_unpressed);
                break;
            default:
                controlAirConditionerWindStrength.setNativeDrawable(R.mipmap.icon_airconditioner_wind_strength_auto_unpressed);
                break;
        }
    }

    private void refreshData() {
        startReadData();
        RotateAnimation rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(500);
        rotate.setRepeatCount(2);
        rotate.setFillAfter(true);
        rotate.setStartOffset(0);
        controlAirConditionerFreshData.startAnimation(rotate);
    }

    @Override
    public void onDataCallback(TaskBase data) {
        byte[] resultData = data.getResultData();
        String resultStr = new String(resultData);
        JMeshLog.e(resultStr);
        if (data instanceof TaskGetCenterAirConditionerStatus) {
            BluetoothData bluetoothData = new BluetoothData(resultStr);
            meterData = bluetoothData.getMeterData();
            refreshMeterData();
            DBHelper.getInstance().addDeviceState(meterData.getDeviceState(meterCode));
        }

    }


}
