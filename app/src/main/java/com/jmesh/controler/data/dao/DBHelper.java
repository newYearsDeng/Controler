package com.jmesh.controler.data.dao;

/**
 * Created by Administrator on 2018/11/12.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jmesh.controler.R;
import com.jmesh.controler.base.BleBaseApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2018/7/2.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static String DB_NAME = "control_0.db";

    public static final String TABLE_READ = "table_gatt_read";
    public static final String TABLE_WRITE = "table_gatt_write";
    public static final String TABLE_DEVICE = "table_device";
    public static final String TABLE_ROOM = "table_room";

    public static final int DB_VERSION = 1;
    private static DBHelper gattDbHelper;

    public static DBHelper getInstance() {
        if (gattDbHelper == null) {
            synchronized (DBHelper.class) {
                gattDbHelper = new DBHelper(BleBaseApplication.context, DB_NAME, null, DB_VERSION);
            }
        }
        return gattDbHelper;
    }


    private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addDevice(Device device) {
        TableDevice.addDevice(getReadableDatabase(), device);
    }

    public void deleteDevice(Device device) {
        TableDevice.deleteDevice(getReadableDatabase(), device);
    }

    public void deleteDevice(String meterCode) {
        TableDevice.deleteDevice(getReadableDatabase(), meterCode);
    }

    public List<Device> getAllDevice() {
        return TableDevice.getAllDevice(getReadableDatabase());
    }

}
