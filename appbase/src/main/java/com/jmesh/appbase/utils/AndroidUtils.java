package com.jmesh.appbase.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;

import com.jmesh.appbase.R;

import java.util.List;

/**
 * Created by Administrator on 2018/6/30.
 */

public class AndroidUtils {
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //判断是否为Debug
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }


    //判断某一个类是否存在任务栈里面
    public static boolean isExistActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);  //获取从栈顶开始往下查找的10个activity
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        return flag;
    }

    public static void createShortCut(Context context) {
        if (isAddedShortCut(context)) {
            return;
        }
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //shortcut的名字
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        //不允许重复添加
        shortcut.putExtra("duplicate", false);
        //设置shortcut的图标
        Parcelable icon = Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher_jmesh);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        //设置用来启动的intent
        //如果用以下Intent会造成从快捷方式进入和从应用集合进入 会开启两遍SplashActivity的问题
        //解决的关键在于添加Action_Main
        //Intent intent = new Intent(this, SplashActivity.class);
        ComponentName comp = new ComponentName(context.getPackageName(), "com.bcmeter.jmeshdriver.ui.WelcomeActivity");
        Intent intent = new Intent(Intent.ACTION_MAIN).setComponent(comp);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        //发送广播让Launcher接收来创建shortcut
        context.sendBroadcast(shortcut);

    }

    public static boolean isAddedShortCut(Context context) {
        boolean isAdded = false;
        //调用Activity的getContentResolver();
        final ContentResolver cr = context.getContentResolver();
        String authority = getAuthorityFromPermission(context.getApplicationContext(), "com.android.launcher.permission.READ_SETTINGS");
        //默认"com.android.launcher2.settings";
        final Uri contentUri = Uri.parse("content://" + authority + "/favorites?notify=true");
        Cursor c = cr.query(contentUri, new String[]{"title", "iconResource"}, "title=?"
                , new String[]{context.getString(R.string.app_name)}, null);
        if (c != null && c.getCount() > 0) {
            isAdded = true;
        }
        return isAdded;
    }

    public static String getAuthorityFromPermission(Context context, String permission) {
        if (permission == null) return null;
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
        if (packs != null) {
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (permission.equals(provider.readPermission)) return provider.authority;
                        if (permission.equals(provider.writePermission)) return provider.authority;
                    }
                }
            }
        }
        return null;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

}
