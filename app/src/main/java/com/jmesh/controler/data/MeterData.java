package com.jmesh.controler.data;

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
