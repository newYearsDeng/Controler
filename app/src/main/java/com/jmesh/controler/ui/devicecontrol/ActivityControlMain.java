package com.jmesh.controler.ui.devicecontrol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jmesh.appbase.ui.BaseActivity;
import com.jmesh.blebase.base.BleManager;
import com.jmesh.blebase.state.BleDevice;
import com.jmesh.controler.data.BDBleDevice;
import com.jmesh.controler.ui.ControlerBaseActivity;

/**
 * Created by Administrator on 2018/11/5.
 */

public class ActivityControlMain extends ControlerBaseActivity {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getIntentData();
        initControl();
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

    int type;
    String meterCode;
    String mac;

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        type = intent.getIntExtra(CONTROL_MAIN_TYPE, 0);
        meterCode = intent.getStringExtra(CONTROL_MAIN_METERCODE);
        mac = intent.getStringExtra(CONTROL_MAIN_MAC);
    }

    private void initControl() {
        controler = BDBleDevice.getControl(type);
        controler.setActivity(this);
        controler.setContentView();
        controler.setType(type);
        controler.setMeterCode(meterCode);
        controler.setMac(mac);
        controler.init();
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
            controler.setConnecting(false);
            controler.onDestroy();
        }
        BleDevice bleDevice = BleManager.getInstance().getConnectedDeviceByMac(mac);
        if (bleDevice != null) {
            BleManager.getInstance().disconnect(bleDevice);
        }

    }

}
