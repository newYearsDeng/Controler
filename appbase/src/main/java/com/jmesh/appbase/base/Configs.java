package com.jmesh.appbase.base;

import android.content.pm.ApplicationInfo;
import android.os.Environment;

import com.jmesh.appbase.BaseApplication;
import com.jmesh.appbase.utils.Logger;

import java.io.File;

/**
 * Created by Administrator on 2018/10/31.
 */

public class Configs {
    public static String LogTag = Configs.class.getSimpleName();
    public static final String LOCAL_HOST = "http://47.107.74.219:8080";

    public static boolean isDebug() {
        try {
            ApplicationInfo info = BaseApplication.context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            Logger.printE(LogTag, e.toString());
        }
        return false;
    }

    public static File catch_dir() {
        return new File(Environment.getExternalStorageDirectory(), "/jmesh/crash/");
    }
}
