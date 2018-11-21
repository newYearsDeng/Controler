package com.jmesh.controler.data.dao;

import android.content.ContentValues;

import com.jmesh.appbase.dao.CVParse;
import com.jmesh.appbase.dao.DBUtils;
import com.jmesh.controler.data.MeterBaseData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/21.
 */

public class DeviceState extends CVParse {
    public static final String kEnergyConsume = "energy_consume";
    public static final String kVolt = "volt";
    public static final String kCurrent = "current";
    public static final String kPower = "power";
    public static final String kPowerFactor = "power_factor";
    public static final String kFrequency = "frequency";
    public static final String kSwitchState = "switch_state";
    public static final String kMeterCode = "meter_code";


    public DeviceState() {

    }

    public DeviceState(ContentValues contentValues) {
        resolveCV(contentValues);
    }

    private String meterCode;
    private float energyConsume;
    private float volt;
    private float current;
    private float power;
    private float powerFactor;
    private float frequency;
    private boolean switchState;

    public static Map<String, String> getFieldMap() {
        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put(kEnergyConsume, DBUtils.FLOAT);
        fieldMap.put(kVolt, DBUtils.FLOAT);
        fieldMap.put(kCurrent, DBUtils.FLOAT);
        fieldMap.put(kPower, DBUtils.FLOAT);
        fieldMap.put(kPowerFactor, DBUtils.FLOAT);
        fieldMap.put(kFrequency, DBUtils.FLOAT);
        fieldMap.put(kSwitchState, DBUtils.INTEGER);
        fieldMap.put(kMeterCode, DBUtils.TEXT);
        return fieldMap;
    }

    public String getMeterCode() {
        return meterCode;
    }

    public void setMeterCode(String meterCode) {
        this.meterCode = meterCode;
    }

    public Float getEnergyConsume() {
        return energyConsume;
    }

    public void setEnergyConsume(float energyConsume) {
        this.energyConsume = energyConsume;
    }

    public Float getVolt() {
        return volt;
    }

    public void setVolt(float volt) {
        this.volt = volt;
    }

    public Float getCurrent() {
        return current;
    }

    public void setCurrent(float current) {
        this.current = current;
    }

    public Float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public Float getPowerFactor() {
        return powerFactor;
    }

    public void setPowerFactor(float powerFactor) {
        this.powerFactor = powerFactor;
    }

    public Float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public boolean isSwitchState() {
        return switchState;
    }

    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
    }


    @Override
    public ContentValues parseToCV() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(kMeterCode, meterCode);
        contentValues.put(kEnergyConsume, energyConsume);
        contentValues.put(kVolt, volt);
        contentValues.put(kCurrent, current);
        contentValues.put(kPower, power);
        contentValues.put(kPowerFactor, powerFactor);
        contentValues.put(kFrequency, frequency);
        contentValues.put(kSwitchState, switchState ? 1 : 0);
        return contentValues;
    }

    @Override
    public void resolveCV(ContentValues contentValues) {
        if (contentValues == null) {
            return;
        }
        meterCode = contentValues.getAsString(kMeterCode);
        energyConsume = contentValues.getAsFloat(kEnergyConsume);
        volt = contentValues.getAsFloat(kVolt);
        current = contentValues.getAsFloat(kCurrent);
        power = contentValues.getAsFloat(kPower);
        powerFactor = contentValues.getAsFloat(kPowerFactor);
        frequency = contentValues.getAsFloat(kFrequency);
        switchState = contentValues.getAsInteger(kSwitchState) == 1;
    }
}
