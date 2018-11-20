package com.jmesh.controler.base;

import android.content.Context;

import com.jmesh.appbase.BaseApplication;
import com.jmesh.blebase.base.BleManager;

/**
 * Created by Administrator on 2018/10/31.
 */

public class BleBaseApplication extends BaseApplication {
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        this.applicationContext = getApplicationContext();
    }

    private void init() {
        initBluetooth();
    }

    private void initBluetooth() {
        BleManager.getInstance().init(this);
    }

    public static Context getApplicataionContext() {
        return applicationContext;
    }
}
