package com.jmesh.appbase.base;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by M on 2016/7/16.
 */
public class PermissionUtils {
    public static final String Camera = Manifest.permission.CAMERA;
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    public static boolean hasPermission(Activity activity, String permission, int requestCode) {
        boolean hasPermission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                hasPermission = false;
            }
        }
        return hasPermission;
    }

    public static boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasPermissionsGranted(Context context, String permission) {
        boolean hasPermission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                hasPermission = false;
            }
        }
        return hasPermission;
    }

    public static boolean hasPermissionsMore(Activity activity, String[] permission, int requestCode) {
        boolean hasPermission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String p : permission)
                if (ContextCompat.checkSelfPermission(activity, p) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, permission, requestCode);
                    hasPermission = false;
                }
        }
        return hasPermission;
    }

    //跳转到该应用的设置界面(通用权限处理)
    public static void handlePemissionGranted(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }

    //处理miui8的权限问题 成功适配机型(Mi Note)
    public static void gotoMiuiPermission(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.setComponent(componentName);
        intent.putExtra("extra_pkgname", context.getPackageName());
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //处理oppo的权限问题 成功适配机型(oppo R11)
    public static void gotoOppoPermission(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", context.getPackageName());
        ComponentName comp = new ComponentName("com.coloros.securitypermission", "com.coloros.securitypermission.permission.PermissionAppAllPermissionActivity");
        intent.setComponent(comp);
        context.startActivity(intent);

    }

    //处理华为权限问题，适配机型华为p9
    public static void gotoHuaweiPermission(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", context.getPackageName());
        ComponentName componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
        intent.setComponent(componentName);
        context.startActivity(intent);

    }


    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    public static boolean isNotificationEnabled(Context context) {//查询是否允许通知,api19可用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                ApplicationInfo appInfo = context.getApplicationInfo();
                String pkg = context.getApplicationContext().getPackageName();
                int uid = appInfo.uid;

                Class appOpsClass = null;
    /* Context.APP_OPS_MANAGER */
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod =
                        appOpsClass.getMethod(
                                CHECK_OP_NO_THROW,
                                Integer.TYPE,
                                Integer.TYPE,
                                String.class
                        );
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (int) opPostNotificationValue.get(Integer.class);
                return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
            } catch (Throwable e) {
//                e.printStackTrace();
            }

        }
        return true;
    }
}

