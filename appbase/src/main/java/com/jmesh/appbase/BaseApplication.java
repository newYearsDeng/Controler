package com.jmesh.appbase;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jmesh.appbase.base.CrashHandler;
import com.jmesh.appbase.network.NetWork;
import com.jmesh.appbase.utils.AndroidUtils;
import com.jmesh.appbase.utils.Logger;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Administrator on 2018/10/31.
 */

public class BaseApplication extends Application {
    public static final String LogTag = BaseApplication.class.getSimpleName();
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        init();
    }

    private void init() {
        Logger.e(LogTag, "onAppCreate");
        initFresco();
//        initLeakcanary();
        initNetWork();
        initExceptionHandler();
        AndroidUtils.createShortCut(this);
    }

    CrashHandler crashHandler;

    private void initExceptionHandler() {
        crashHandler = new CrashHandler(this.getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    public static final String kFromDesktop = "FROM_DESKTOP";


    private void initNetWork() {
        NetWork.setNetWork(new NetWork(this));
    }

    private void initFresco() {
        Fresco.initialize(getApplicationContext());
    }

    private void initLeakcanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        Logger.e(LogTag, "Leakcanary init");
        LeakCanary.install(this);
    }


}
