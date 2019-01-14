package com.jmesh.controler.ui.devicecontrol;

import android.bluetooth.BluetoothAdapter;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jmesh.appbase.ui.widget.JmeshDraweeView;
import com.jmesh.appbase.ui.widget.MyToggleButton;
import com.jmesh.blebase.utils.JMeshLog;
import com.jmesh.controler.R;
import com.jmesh.controler.airconditioner.GreeFrequency;
import com.jmesh.controler.airconditioner.IAirConditioner;
import com.jmesh.controler.airconditioner.Midea;
import com.jmesh.controler.base.ReadingTaskHandler;
import com.jmesh.controler.data.AirConditionerBrand;
import com.jmesh.controler.data.AirConditionerData;
import com.jmesh.controler.data.MeterBaseData;
import com.jmesh.controler.data.dao.DBHelper;
import com.jmesh.controler.data.dao.DeviceState;
import com.jmesh.controler.task.TaskACAuthenticationSwitchOn;
import com.jmesh.controler.task.TaskACSwitchOff;
import com.jmesh.controler.task.TaskACSwitchOn;
import com.jmesh.controler.task.TaskAcAuthenticationSwitchOff;
import com.jmesh.controler.task.TaskAirConditioner;
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
import com.jmesh.controler.ui.ControlerBaseActivity;
import com.jmesh.controler.ui.widget.DlgBaseData;
import com.jmesh.controler.ui.widget.DlgChooseAirConditionerBrand;
import com.jmesh.controler.ui.widget.DlgChooseMode;
import com.jmesh.controler.ui.widget.DlgChooseWindStrength;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/12.
 */

public class ControlAirConditioner extends ControlBase implements View.OnClickListener, MyToggleButton.SwitchListener {

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
        chooseAirconditionerBrand();
        initNavRightContent();
    }

    @Override
    public int getContentView() {
        return R.layout.control_air_conditioner;
    }

    @Override
    public String getTitle() {
        BluetoothAdapter bluetoothAdapter;
        return "分体空调";
    }


    private void initNavRightContent() {
        TextView textView = new TextView(getActivity());
        textView.setTextColor(getActivity().getResources().getColor(R.color.white));
        textView.setTextSize(18);
        textView.setText(getActivity().getString(R.string.brand_choose));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(layoutParams);
        ((ControlerBaseActivity) activity).setRightImageContent(textView);
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


    ReadingTaskHandler readingTaskHandler;

    @Override
    public void onNavRightBnClicked() {
        chooseAirconditionerBrand();
    }

    private void chooseAirconditionerBrand() {
        DlgChooseAirConditionerBrand dlg = new DlgChooseAirConditionerBrand(getActivity());
        List<AirConditionerBrand> list = new ArrayList<>();
        list.add(new AirConditionerBrand("格力", 0, true));
        list.add(new AirConditionerBrand("美的", 1));
        dlg.setAirConditionerBrandList(list);
        dlg.show();
        dlg.setCallback(new DlgChooseAirConditionerBrand.OnCheckCallback() {
            @Override
            public void onCheck(AirConditionerBrand airConditionerBrand) {
                if (airConditionerBrand.getBrandId() == 1) {
                    airConditioner = new Midea();
                } else {
                    airConditioner = new GreeFrequency();
                }
            }
        });
    }

    IAirConditioner airConditioner = new GreeFrequency();

    @Override
    protected void deviceConnectSuccess() {
        readingTaskHandler = ReadingTaskHandler.getInstance();
        readingTaskHandler.setMac(mac);
        readingTaskHandler.setCallback(this);
        readData();
    }

    private void readData() {
        if (readingTaskHandler == null || (!isDeviceConneced())) {
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

    private void initEvent() {
        controlAirConditionerMinus.setOnClickListener(this);
        controlAirConditionerPlus.setOnClickListener(this);
        controlAirConditionerPower.setOnClickListener(this);
        controlAirConditionerFreshData.setOnClickListener(this);
        controlAirConditionerMode.setOnClickListener(this);
        controlAirConditionerWindStrength.setOnClickListener(this);
        controlMeterTop.setOnClickListener(this);
        controlAirConditionerVerticalScavenging.setOnClickListener(this);
        controlAirConditionerHorizontalScavenging.setOnClickListener(this);
//        controlAirConditionerSwitch.setOnSwitchListener(this);
        controlAirConditionerSignal.setOnClickListener(this);
    }

    private void refreshData() {
        readData();
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
            case R.id.control_air_conditioner_vertical_scavenging:
                changeVerticalScavenging();
                break;
            case R.id.control_air_conditioner_horizontal_scavenging:
                changeHorizontalScavenging();
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

    private void shake() {
        TranslateAnimation translateAnimation = new TranslateAnimation(-5, 5, 0, 0);
        translateAnimation.setRepeatCount(10);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        controlAirConditionerSignal.startAnimation(translateAnimation);
    }

    private void minsTemperture() {
        if (airConditionerData.getTemperture() > 16) {
            airConditionerData.setTemperture(airConditionerData.getTemperture() - 1);
        }
        commit();
    }

    private void plusTemperture() {
        if (airConditionerData.getTemperture() < 30) {
            airConditionerData.setTemperture(airConditionerData.getTemperture() + 1);
        }
        commit();
    }

    private void chooseMode() {
        if (airConditionerData == null) {
            return;
        }
        DlgChooseMode dlgChooseMode = new DlgChooseMode(getActivity());
        dlgChooseMode.setChooseCallback(new DlgChooseMode.ModeChooseCallback() {
            @Override
            public void onChoose(int mode) {
                airConditionerData.setMode(mode);
                commit();
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
                commit();
            }
        });
        dlgChooseWindStrength.show();
    }

    private void changeHorizontalScavenging() {
        if (airConditionerData == null) {
            return;
        }
        if (airConditionerData.isHorizontalScavenging()) {
            airConditionerData.setHorizontalScavenging(false);
        } else {
            airConditionerData.setHorizontalScavenging(true);
        }
        commit();
    }

    private void changeVerticalScavenging() {
        if (airConditionerData == null) {
            return;
        }
        if (airConditionerData.isVerticalScavenging()) {
            airConditionerData.setVerticalScavenging(false);
        } else {
            airConditionerData.setVerticalScavenging(true);
        }
        commit();
    }

    private void commit() {
        if (readingTaskHandler == null) {
            return;
        }
        if (!isDeviceConneced()) {
            return;
        }
        refreshView();
        byte[] code = airConditioner.getCode(airConditionerData).getBytes();
        TaskAirConditioner taskAirConditioner = new TaskAirConditioner(meterCode);
        taskAirConditioner.setCode(code);
        readingTaskHandler.addTask(taskAirConditioner);
    }

    private void changePower() {
        if (airConditionerData == null) {
            return;
        }
        airConditionerData.setPower(!airConditionerData.isPower());
        commit();
    }

    AirConditionerData airConditionerData;

    private void initAirConditionerData() {
        airConditionerData = new AirConditionerData();
        airConditionerData.setPower(false);
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
        refreshHorizontalScavenging(airConditionerData.isHorizontalScavenging());
        refreshVerticalScavengint(airConditionerData.isVerticalScavenging());
    }

    private void refreshTemperture(int temperture) {
        tempertureTextView.setText(new StringBuilder().append(Integer.toString(temperture)).append(activity.getString(R.string.temperture_unit)));
    }

    private void refreshPower(boolean enable) {
        if (enable) {
            controlAirConditionerPower.setNativeDrawable(R.mipmap.icon_airconditioner_power_unpressed);
        } else {
            controlAirConditionerPower.setNativeDrawable(R.mipmap.icon_airconditioner_power_pressed);
        }
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

    private void refreshHorizontalScavenging(boolean enable) {
        if (enable) {
            controlAirConditionerHorizontalScavenging.setNativeDrawable(R.mipmap.icon_airconditioner_scavenging_horizontal_pressed);
        } else {
            controlAirConditionerHorizontalScavenging.setNativeDrawable(R.mipmap.icon_airconditioner_scavenging_horizontal_unpressed);
        }
    }

    private void refreshVerticalScavengint(boolean enable) {
        if (enable) {
            controlAirConditionerVerticalScavenging.setNativeDrawable(R.mipmap.icon_airconditioner_scavenging_vertical_pressed);
        } else {
            controlAirConditionerVerticalScavenging.setNativeDrawable(R.mipmap.icon_airconditioner_scavenging_vertical_unpressed);
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
        if (state) {
            switchOff();
        } else {
            swithOn();
        }
    }

    public void swithOn() {
        if (!isDeviceConneced()) {
            return;
        }
        TaskACAuthenticationSwitchOn taskACAuthenticationSwitchOn = new TaskACAuthenticationSwitchOn(meterCode);
        readingTaskHandler.addTask(taskACAuthenticationSwitchOn);
    }

    public void switchOff() {
        if (!isDeviceConneced()) {
            return;
        }
        TaskAcAuthenticationSwitchOff taskAcAuthenticationSwitchOff = new TaskAcAuthenticationSwitchOff(meterCode);
        readingTaskHandler.addTask(taskAcAuthenticationSwitchOff);
    }

    @Override
    public void onDataCallback(TaskBase data) {
        super.onDataCallback(data);
        byte[] resultData = data.getResultData();
        String resultStr = new String(resultData);
        JMeshLog.e("callback_str", resultStr);
        if (data instanceof TaskAirConditioner) {
            shake();
        } else if (data instanceof TaskMeterSwitchOn) {

        } else if (data instanceof TaskMeterSwitchOff) {

        } else if (data instanceof TaskACAuthenticationSwitchOn) {
            TaskACSwitchOn taskACSwitchOn = new TaskACSwitchOn(meterCode, resultData);
            readingTaskHandler.addTask(taskACSwitchOn);
        } else if (data instanceof TaskAcAuthenticationSwitchOff) {
            TaskACSwitchOff taskACSwitchOff = new TaskACSwitchOff(meterCode, resultData);
            readingTaskHandler.addTask(taskACSwitchOff);
        } else if (data instanceof TaskACSwitchOn) {

        } else if (data instanceof TaskACSwitchOff) {

        }
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
}
