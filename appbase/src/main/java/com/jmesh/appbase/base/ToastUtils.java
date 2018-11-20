package com.jmesh.appbase.base;

import android.widget.Toast;

import com.jmesh.appbase.BaseApplication;

/**
 * Created by BC119 on 2018/6/25.
 */

public class ToastUtils {
    public static void showToast(String toast) {
        Toast.makeText(BaseApplication.context, toast, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(String toastStr, int gravity) {
        Toast toast = Toast.makeText(BaseApplication.context, toastStr, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }


    public static void showToast(String... toast) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : toast) {
            stringBuilder.append(s);
            stringBuilder.append(" ");
        }
        Toast.makeText(BaseApplication.context, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
    }
}
