package com.jmesh.controler.data;

import com.jmesh.appbase.utils.StringUtil;
import com.jmesh.controler.data.dao.DeviceState;

/**
 * Created by Administrator on 2018/11/9.
 */

public class MeterData {
    private MeterBaseData energyConsume;
    private MeterBaseData volt;
    private MeterBaseData current;
    private MeterBaseData power;
    private MeterBaseData powerFactor;
    private MeterBaseData frequency;
    private MeterBaseData switchState;

    public MeterData() {

    }


    public DeviceState getDeviceState(String meterCode) {
        DeviceState deviceState = new DeviceState();
        deviceState.setMeterCode(meterCode);
        if (energyConsume != null) {
            deviceState.setEnergyConsume(Float.parseFloat(energyConsume.getValue()));
        }
        if (volt != null) {
            deviceState.setVolt(Float.parseFloat(volt.getValue()));
        }
        if (current != null) {
            deviceState.setCurrent(Float.parseFloat(current.getValue()));
        }
        if (power != null) {
            deviceState.setPower(Float.parseFloat(power.getValue()));
        }
        if (powerFactor != null) {
            deviceState.setPowerFactor(Float.parseFloat(powerFactor.getValue()));
        }
        if (frequency != null) {
            deviceState.setFrequency(Float.parseFloat(frequency.getValue()));
        }
        if (switchState != null) {
            deviceState.setSwitchState(switchState.getValue().equals("合闸"));
        }
        return deviceState;
    }

    public void init(DeviceState deviceState) {
        setEnergyConsume(new MeterBaseData("耗能", StringUtil.reserve(2, deviceState.getEnergyConsume().toString()), "KW·h"));
        setVolt(new MeterBaseData("电压", StringUtil.reserve(1, deviceState.getVolt().toString()), "V"));
        setCurrent(new MeterBaseData("电流", StringUtil.reserve(3, deviceState.getCurrent().toString()), "A"));
        setFrequency(new MeterBaseData("频率", StringUtil.reserve(2, deviceState.getFrequency().toString()), "Hz"));
        setPower(new MeterBaseData("功率", StringUtil.reserve(1, deviceState.getPower().toString()), "W"));
        setPowerFactor(new MeterBaseData("功率因数", StringUtil.reserve(3, deviceState.getPowerFactor().toString()), ""));
        setSwitchState(new MeterBaseData("跳合闸状态", deviceState.isSwitchState() ? "合闸" : "跳闸", ""));
    }

    public MeterBaseData getSwitchState() {
        return switchState;
    }

    public void setSwitchState(MeterBaseData switchState) {
        this.switchState = switchState;
    }

    public MeterBaseData getEnergyConsume() {
        return energyConsume;
    }

    public void setEnergyConsume(MeterBaseData energyConsume) {
        this.energyConsume = energyConsume;
    }

    public MeterBaseData getVolt() {
        return volt;
    }

    public void setVolt(MeterBaseData volt) {
        this.volt = volt;
    }

    public MeterBaseData getCurrent() {
        return current;
    }

    public void setCurrent(MeterBaseData current) {
        this.current = current;
    }

    public MeterBaseData getPower() {
        return power;
    }

    public void setPower(MeterBaseData power) {
        this.power = power;
    }

    public MeterBaseData getPowerFactor() {
        return powerFactor;
    }

    public void setPowerFactor(MeterBaseData powerFactor) {
        this.powerFactor = powerFactor;
    }

    public MeterBaseData getFrequency() {
        return frequency;
    }

    public void setFrequency(MeterBaseData frequency) {
        this.frequency = frequency;
    }

}
