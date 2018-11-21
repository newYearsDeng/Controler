package com.jmesh.controler.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmesh.appbase.dao.TableBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/12.
 */

public class TableDevice extends TableBase {
    public static final String TABLE_NAME = "table_device";

    public static void createTable(SQLiteDatabase database) {
        createTable(database, TABLE_NAME, Device.getFieldMap());
    }

    public static void dropTable(SQLiteDatabase database) {
        dropTable(TABLE_NAME, database);
    }

    public static void addDevice(SQLiteDatabase db, Device device) {
        deleteDevice(db, device);
        db.insert(TABLE_NAME, null, device.parseToCV());
    }

    public static void deleteDevice(SQLiteDatabase database, Device device) {
        if (device != null) {
            deleteDevice(database, device.getMeterCode());
        }
    }

    public static void deleteDevice(SQLiteDatabase database, String meterCode) {
        database.delete(TABLE_NAME, Device.kMeterCode + "=?", new String[]{meterCode});
    }

    public static List<Device> getAllDevice(SQLiteDatabase sqLiteDatabase) {
        String sql = queryAllSql(TABLE_NAME);
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null, null);
        List<Device> deviceList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int columnCount = cursor.getColumnCount();
            ContentValues contentValues = new ContentValues();
            for (int i = 0; i < columnCount; i++) {
                String columnName = cursor.getColumnName(i);
                contentValues.put(columnName, cursor.getString(i));
            }
            Device device = new Device(contentValues);
            deviceList.add(device);
        }
        return deviceList;
    }


}
