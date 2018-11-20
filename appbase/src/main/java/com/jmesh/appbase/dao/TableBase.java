package com.jmesh.appbase.dao;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.Map;

/**
 * Created by Administrator on 2018/7/2.
 */

public class TableBase {
    public static String LogTag = TableBase.class.getSimpleName();


    public static void createTable(SQLiteDatabase db, String tableName, Map<String, String> field) {
        if (!canCreate(db, tableName)) {
            return;
        }
        if (field == null) {
            return;
        }
        try {
            int keySetSize = field.keySet().size();
            String[] fieldStrs = new String[keySetSize];
            int index = 0;
            for (String str : field.keySet()) {
                String type = field.get(str);
                String singleFieledStr = DBUtils.createColumn(str, type);
                fieldStrs[index] = singleFieledStr;
                index++;
            }
            String fieldDesc = DBUtils.appendColumn(fieldStrs);
            createSql(db, tableName, fieldDesc);
        } catch (Throwable t) {

        }
    }


    public static void dropTable(final String tabName, SQLiteDatabase db) {
        if (db != null && !TextUtils.isEmpty(tabName)) {
            db.execSQL("drop table if exists " + tabName);
        }
    }

    public static boolean tableExist(SQLiteDatabase db, String table) {
        return DBUtils.getInstance().tableIsExist(db, table);
    }

    protected static boolean canCreate(SQLiteDatabase db, String tableName) {
        if (db == null) {
            return false;
        }
        if (TextUtils.isEmpty(tableName)) {
            return false;
        }
        if (DBUtils.getInstance().tableIsExist(db, tableName)) {
            return false;
        }
        return true;
    }

    public static void createSql(SQLiteDatabase db, String tableName, String field) {
        db.execSQL("create table if not exists " + tableName + field + ";");
    }


    public static String queryAllSql(String tableName) {
        String sql = "SELECT * FROM " + tableName;
        return sql;
    }

    public static String querySingleSql(String tableName, String key) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + key + "=?";
        return sql;
    }

}
