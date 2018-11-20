package com.jmesh.appbase.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * Created by Administrator on 2018/7/2.
 */

public class DBUtils {
    public static String INTEGER = "INTEGER";//2个字节
    public static String INTEGER_PRIMARY_KEY = "INTERGER PRIMARY KEY";
    public static String TEXT = "TEXT";//
    public static String BIG_INTEGER = "BIGINT";
    public static String INTEGER_PRIMARY_KEY_AUTOINCREMENT ="INTEGER PRIMARY KEY AUTOINCREMENT";
    public static String LONG = "LONG";//四个字节


    public static String createColumn(String name,String type){
        return name+" "+type;
    }

    public static String appendColumn(String... columns){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        stringBuilder.append(columns[0]);
        for(int index =1;index<columns.length;index++){
            stringBuilder.append(",");
            stringBuilder.append(columns[index]);
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }



    private static volatile DBUtils INSTANCE;
    public static DBUtils getInstance() {
        if (INSTANCE == null) {
            synchronized (DBUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DBUtils();
                }
            }
        }
        return INSTANCE;
    }


    public synchronized boolean tableIsExist(SQLiteDatabase db, String tableName) {
        if (db == null || TextUtils.isEmpty(tableName)) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='"
                    + tableName + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            cursor = null;
        }
        return false;
    }
}
