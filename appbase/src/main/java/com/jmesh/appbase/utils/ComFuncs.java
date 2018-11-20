package com.jmesh.appbase.utils;

import android.content.Context;
import com.jmesh.appbase.BaseApplication;

/**
 * Created by BC119 on 2018/6/20.
 */

public class ComFuncs {
    public static final String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length);
        String sTemp;
        for (int i = 0; i < bytes.length; i++) {
            sTemp = Integer.toHexString(0xFF & bytes[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static int getMipmapResource(String imageName) {
        Context context = BaseApplication.context;
        int resId = context.getResources().getIdentifier(imageName, "mipmap", context.getPackageName());
        return resId;
    }

    public static int getDrawableResource(String imageName) {
        Context context = BaseApplication.context;
        int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        return resId;
    }
}
