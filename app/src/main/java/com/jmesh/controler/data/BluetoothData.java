package com.jmesh.controler.data;
import com.jmesh.appbase.base.JSONCachable;
import com.jmesh.appbase.utils.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/12/13.
 */

public class BluetoothData implements JSONCachable {
    public static final String kPower = "zyggl";
    public static final String kCurrent = "dl";
    public static final String kVolt = "dy";
    public static final String kConsume = "ygzdn";
    public static final String kPowerFactor = "glys";
    public static final String kFrequency = "dwpl";
    public static final String kData = "ktkgzt";

    private String power;
    private String current;
    private String volt;
    private String consume;
    private String powerFactor;
    private String frequency;
    private String data;

    public BluetoothData(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            parseJson(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJson() {
        return null;
    }

    @Override
    public void parseJson(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        power = jsonObject.optString(kPower);
        current = jsonObject.optString(kCurrent);
        volt = jsonObject.optString(kVolt);
        consume = jsonObject.optString(kConsume);
        powerFactor = jsonObject.optString(kPowerFactor);
        frequency = jsonObject.optString(kFrequency);
        data = jsonObject.optString(kData);
    }

    public MeterData getMeterData() {
        MeterData meterData = new MeterData();
        meterData.setEnergyConsume(new MeterBaseData("耗能", StringUtil.reserve(2, consume), "KW·h"));
        meterData.setVolt(new MeterBaseData("电压", StringUtil.reserve(1, volt), "V"));
        meterData.setCurrent(new MeterBaseData("电流", StringUtil.reserve(3, current), "A"));
        meterData.setFrequency(new MeterBaseData("频率", StringUtil.reserve(2, frequency), "Hz"));
        meterData.setPower(new MeterBaseData("功率", StringUtil.reserve(4, power), "kw"));
        meterData.setPowerFactor(new MeterBaseData("功率因数", StringUtil.reserve(3, powerFactor), ""));
        return meterData;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getVolt() {
        return volt;
    }

    public void setVolt(String volt) {
        this.volt = volt;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public String getPowerFactor() {
        return powerFactor;
    }

    public void setPowerFactor(String powerFactor) {
        this.powerFactor = powerFactor;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
