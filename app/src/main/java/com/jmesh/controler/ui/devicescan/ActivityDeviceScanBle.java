package com.jmesh.controler.ui.devicescan;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.jmesh.appbase.base.PermissionUtils;
import com.jmesh.appbase.ui.widget.MyToggleButton;
import com.jmesh.blebase.base.BleManager;
import com.jmesh.blebase.callback.BleScanCallback;
import com.jmesh.blebase.state.BleDevice;
import com.jmesh.controler.R;
import com.jmesh.controler.data.DataResolver;
import com.jmesh.controler.ui.ControlerBaseActivity;

import java.util.List;

/**
 * Created by Administrator on 2018/10/31.
 */

public class ActivityDeviceScanBle extends ControlerBaseActivity implements MyToggleButton.SwitchListener, View.OnClickListener {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_device_scan);
        init();
    }

    private void init() {
        initView();
        checkPermission();
    }

    private void checkPermission() {
        PermissionUtils.hasPermissionsMore(this, new String[]{PermissionUtils.Camera,
                PermissionUtils.ACCESS_FINE_LOCATION}, RequestCodePermission);
    }

    private void initView() {
        assignViews();
        initEvent();
        initRecyclerView();
    }

    private void initRecyclerView() {
        if (adapterDeviceScan == null) {
            adapterDeviceScan = new AdapterDeviceScan(this);
        }
        deviceScanList.setLayoutManager(new LinearLayoutManager(this));
        deviceScanList.setAdapter(adapterDeviceScan);
    }

    private void initEvent() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isEnabled()) {//蓝牙处于开启的状态，关闭蓝牙
            deviceScanOpenBtBn.setInitState(true);
        } else {//蓝牙处于关闭的状态，开启蓝牙
            deviceScanOpenBtBn.setInitState(false);
        }
        deviceScanOpenBtBn.setOnSwitchListener(this);
        deviceScanBn.setOnClickListener(this);
    }

    private TextView deviceScanBtTx;
    private MyToggleButton deviceScanOpenBtBn;
    private TextView deviceScanTxScanDevices;
    private RecyclerView deviceScanList;
    private TextView deviceScanBn;
    private AdapterDeviceScan adapterDeviceScan;


    private void assignViews() {
        deviceScanBtTx = (TextView) findViewById(R.id.device_scan_bt_tx);
        deviceScanOpenBtBn = (MyToggleButton) findViewById(R.id.device_scan_open_bt_bn);
        deviceScanTxScanDevices = (TextView) findViewById(R.id.device_scan_tx_scan_devices);
        deviceScanList = (RecyclerView) findViewById(R.id.device_scan_list);
        deviceScanBn = findViewById(R.id.device_scan_bn);
    }

    @Override
    public void onSwitchStateChange(View view, boolean state) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isEnabled()) {//蓝牙处于开启的状态，关闭蓝牙
            if (!state) {
                BleManager.getInstance().disableBluetooth();
            }
        } else {//蓝牙处于关闭的状态，开启蓝牙
            if (state) {
                BleManager.getInstance().enableBluetooth();
            }
        }

    }


    private void refreshDevice() {
        BleManager.getInstance().clearBleDevice();
        adapterDeviceScan.clearData();
        BleManager.getInstance().scan(callback, 20000);
    }

    BleScanCallback callback = new BleScanCallback() {
        @Override
        public void onScanFinished(List<BleDevice> list) {

        }

        @Override
        public void onScanStarted(boolean b) {

        }

        @Override
        public void onScanning(BleDevice bleDevice) {
            DataResolver.RawData rawData = new DataResolver.RawData(bleDevice);
            if (rawData.isBDDevice()) {
                adapterDeviceScan.addDevice(rawData);
            }
        }
    };

    public static void openActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ActivityDeviceScanBle.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == deviceScanBn) {
            refreshDevice();
        }
    }
}
