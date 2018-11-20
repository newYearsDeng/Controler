package com.jmesh.controler.data.dao;

import android.content.ContentValues;

import com.jmesh.appbase.dao.CVParse;
import com.jmesh.appbase.dao.DBUtils;
import com.jmesh.controler.data.BDBleDevice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/12.
 */

public class Device extends CVParse implements IDevice {

    public static final String kName = "device_name";
    public static final String kMeterCode = "device_meter_code";
    public static final String kType = "device_type";
    public static final String kMac = "device_mac";

    public String name;
    public String meterCode;
    public int type;
    public String mac;

    public Device(ContentValues contentValues) {
        resolveCV(contentValues);
    }

    public Device(String name, String meterCode, int type, String mac) {
        this.name = name;
        this.meterCode = meterCode;
        this.type = type;
        this.mac = mac;
    }


    @Override
    public String getName() {
        return BDBleDevice.getName(type);
    }

    @Override
    public String getMeterCode() {
        return meterCode;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getMac() {
        return mac;
    }

    @Override
    public ContentValues parseToCV() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(kType, type);
        contentValues.put(kName, name);
        contentValues.put(kMeterCode, meterCode);
        contentValues.put(kMac, mac);
        return contentValues;
    }

    @Override
    public void resolveCV(ContentValues contentValues) {
        if (contentValues == null) {
            return;
        }
        this.name = contentValues.getAsString(kName);
        this.meterCode = contentValues.getAsString(kMeterCode);
        this.type = contentValues.getAsInteger(kType);
        this.mac = contentValues.getAsString(kMac);
    }

    public static final Map<String, String> getFieldMap() {
        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put(kMeterCode, DBUtils.TEXT);
        fieldMap.put(kName, DBUtils.TEXT);
        fieldMap.put(kType, DBUtils.INTEGER);
        fieldMap.put(kMac, DBUtils.TEXT);
        return fieldMap;
    }
}
