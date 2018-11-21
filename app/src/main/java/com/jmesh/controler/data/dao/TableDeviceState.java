package com.jmesh.controler.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.MotionEvent;

import com.jmesh.appbase.dao.TableBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/21.
 */

public class TableDeviceState extends TableBase {
    public static final String TABLE_NAME = "TABLE_DEVICE_STATE";

    public static void createTable(SQLiteDatabase database) {
        createTable(database, TABLE_NAME, DeviceState.getFieldMap());
    }

    public static void dropTable(SQLiteDatabase database) {
        dropTable(TABLE_NAME, database);
    }

    public static void insertDeviceState(SQLiteDatabase database, DeviceState deviceState) {
        deleteDeviceState(database, deviceState.getMeterCode());
        database.insert(TABLE_NAME, null, deviceState.parseToCV());
    }

    public static void deleteDeviceState(SQLiteDatabase database, DeviceState deviceState) {
        deleteDeviceState(database, deviceState.getMeterCode());
    }

    public static void deleteDeviceState(SQLiteDatabase database, String meterCode) {
        database.delete(TABLE_NAME, DeviceState.kMeterCode + "=?", new String[]{meterCode});
    }

    public static DeviceState getDeviceState(SQLiteDatabase database, String meterCode) {
        String sql = querySingleSql(TABLE_NAME, DeviceState.kMeterCode);
        Cursor cursor = database.rawQuery(sql, new String[]{meterCode});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int columnCount = cursor.getColumnCount();
            ContentValues contentValues = new ContentValues();
            for (int i = 0; i < columnCount; i++) {
                String columnName = cursor.getColumnName(i);
                contentValues.put(columnName, cursor.getString(i));
            }
            DeviceState device = new DeviceState(contentValues);
            return device;
        }
        return null;
    }

}
