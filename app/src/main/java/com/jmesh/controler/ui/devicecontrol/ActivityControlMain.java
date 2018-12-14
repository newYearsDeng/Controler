package com.jmesh.controler.ui.devicecontrol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.jmesh.appbase.base.ToastUtils;
import com.jmesh.blebase.base.BleManager;
import com.jmesh.blebase.callback.BleScanCallback;
import com.jmesh.blebase.state.BleDevice;
import com.jmesh.controler.base.ReadingTaskHandler;
import com.jmesh.controler.data.BDBleDevice;
import com.jmesh.controler.data.DataResolver;
import com.jmesh.controler.ui.ControlerBaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2018/11/5.
 */

public class ActivityControlMain extends ControlerBaseActivity {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getIntentData();
        if (!TextUtils.isEmpty(mac)) {
            initControl(true);
        } else {
            initControl(false);
        }
        setTitle(controler.getTitle());
    }

    public static final String CONTROL_MAIN_TYPE = "type";
    public static final String CONTROL_MAIN_METERCODE = "meter_code";
    public static final String CONTROL_MAIN_MAC = "mac";


    public static void openActivity(Context context, int type, String meterCode, String mac) {
        Intent intent = new Intent();
        intent.setClass(context, ActivityControlMain.class);
        intent.putExtra(CONTROL_MAIN_TYPE, type);
        intent.putExtra(CONTROL_MAIN_METERCODE, meterCode);
        intent.putExtra(CONTROL_MAIN_MAC, mac);
        context.startActivity(intent);
    }

    public static void openActivity(Context context, int type, String meterCode) {
        Intent intent = new Intent();
        intent.setClass(context, ActivityControlMain.class);
        intent.putExtra(CONTROL_MAIN_TYPE, type);
        intent.putExtra(CONTROL_MAIN_METERCODE, meterCode);
        context.startActivity(intent);
    }

    int type;
    String meterCode = "";
    String mac = "";

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        type = intent.getIntExtra(CONTROL_MAIN_TYPE, 0);
        meterCode = intent.getStringExtra(CONTROL_MAIN_METERCODE);
        mac = intent.getStringExtra(CONTROL_MAIN_MAC);
        if (TextUtils.isEmpty(mac)) {
            BleManager.getInstance().scan(new BleScanCallback() {
                @Override
                public void onScanFinished(List<BleDevice> list) {

                }

                @Override
                public void onScanStarted(boolean b) {

                }

                @Override
                public void onScanning(BleDevice bleDevice) {
                    DataResolver.RawData rawData = new DataResolver.RawData(bleDevice);
                    if (rawData.isBDDevice() && rawData.getMeterCode().equals(meterCode)) {
                        controler.setMac(bleDevice.getMac());
                        controler.init();
                    }
                }
            }, 5000);
        }
    }

    private void initControl(boolean hasMac) {
        controler = BDBleDevice.getControl(type);
        if (controler == null) {
            ToastUtils.showToast("未知设备！");
            finish();
        }
        controler.setActivity(this);
        controler.setContentView();
        controler.setType(type);
        controler.setMeterCode(meterCode);
        if (hasMac) {
            controler.setMac(mac);
            controler.init();
        }
    }

    ControlBase controler;

    @Override
    public void onNavRightBnClicked() {
        controler.onNavRightBnClicked();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (controler != null) {
            EventBus.getDefault().unregister(controler);
            controler.setConnecting(false);
            controler.onDestroy();
            ReadingTaskHandler.getInstance().clearAllTask();
        }
    }

}
