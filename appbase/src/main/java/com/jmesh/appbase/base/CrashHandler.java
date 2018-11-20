package com.jmesh.appbase.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import com.jmesh.appbase.utils.Logger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/1.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String LogTag = CrashHandler.class.getSimpleName();
    Context context;
    Thread.UncaughtExceptionHandler mDefaultHandler;

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!handleException(e) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(t, e);
        }
    }

    public CrashHandler(Context context) {
        this.context = context;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    private boolean handleException(Throwable ex) {
        boolean result = false;
        if (ex == null) {
            return result;
        }
        collectDeviceInfo(context);
        saveCrashInfo2File(ex);
        /*
        这里可以用作用户友好。
        同时处理一些可以处理的Exception
        然后修改result的结果
         */
//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();//准备发消息的MessageQueue
//                Toast.makeText(context, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
//                Looper.loop();
//            }
//        }.start();

        return result;
    }

    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private Map<String, String> infos = new HashMap<String, String>();

    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(LogTag, "an error occured when collect package info", e.toString());
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Logger.d(LogTag, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Logger.e(LogTag, "an error occured when collect crash info", e.toString());
            }
        }
    }

    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                File dir = Configs.catch_dir();
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Logger.e(LogTag, "an error occured while writing file...", e.toString());
        }
        return null;
    }

}
