package com.jmesh.controler.ui.devicecontrol;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;

import com.jmesh.appbase.base.ToastUtils;
import com.jmesh.blebase.base.BleManager;
import com.jmesh.blebase.callback.BleGattCallback;
import com.jmesh.blebase.exception.BleException;
import com.jmesh.blebase.state.BleDevice;
import com.jmesh.controler.R;
import com.jmesh.controler.base.ReadingTaskHandler;
import com.jmesh.controler.data.dao.DBHelper;
import com.jmesh.controler.data.dao.Device;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2018/11/5.
 */

public abstract class ControlBase implements ReadingTaskHandler.OnDataCallback {
    protected Activity activity;
    protected int type;
    protected String meterCode;
    protected String mac;

    public ControlBase() {
        EventBus.getDefault().register(this);
    }

    public abstract void onNavRightBnClicked();

    public boolean isDeviceConneced() {
        final String mac = getMac();
        BleDevice bleDevice = BleManager.getInstance().getConnectedDeviceByMac(mac);
        if (bleDevice != null) {
            return true;
        } else return false;
    }

    public void connecedDevice() {
        connecting = true;
        BleManager.getInstance().connect(mac, callback);
    }

    @Subscribe
    public void onEvent(EventConnectState eventConnectState) {
        switch (eventConnectState.getState()) {
            case EventConnectState.connectFailed:
                onConnectedFailed();
                break;
            case EventConnectState.connecting:
                onConnecting();
                break;
            case EventConnectState.connectSuccess:
                onConnectedSuccess();
                break;
            case EventConnectState.disconnected:
                onDisConnected();
                break;
        }
    }


    private boolean connecting;

    public boolean isConnecting() {
        return connecting;
    }

    public void setConnecting(boolean connecting) {
        this.connecting = connecting;
    }

    private void onConnectedFailed() {
        ToastUtils.showToast(getActivity().getString(R.string.connecting_failed));
        if (isConnecting()) {
            connecedDevice();
        }
    }

    private void onConnectedSuccess() {
        ToastUtils.showToast(getActivity().getString(R.string.connecting_success));
        setConnecting(false);
        deviceConnectSuccess();
        saveDevice();
    }

    private void saveDevice() {
        Device device = new Device("", meterCode, type, mac);
        DBHelper.getInstance().addDevice(device);
    }

    private void onDisConnected() {
        ToastUtils.showToast(getActivity().getString(R.string.disconnect));
    }

    private void onConnecting() {
        ToastUtils.showToast(getActivity().getString(R.string.connecting));
    }

    protected abstract void deviceConnectSuccess();

    public static BleGattCallback callback = new BleGattCallback() {
        @Override
        public void onStartConnect() {
            EventBus.getDefault().post(new EventConnectState(EventConnectState.connecting));
        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException e) {
            EventBus.getDefault().post(new EventConnectState(EventConnectState.connectFailed));
        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt bluetoothGatt, int i) {
            EventBus.getDefault().post(new EventConnectState(EventConnectState.connectSuccess));
        }

        @Override
        public void onDisConnected(boolean b, BleDevice bleDevice, BluetoothGatt bluetoothGatt, int i) {
            EventBus.getDefault().post(new EventConnectState(EventConnectState.disconnected));
        }
    };

    public static class EventConnectState {
        public int state;
        public static final int connecting = 10;
        public static final int connectFailed = 11;
        public static final int connectSuccess = 12;
        public static final int disconnected = 14;

        public EventConnectState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }

    public abstract void onDestroy();

    public abstract void init();

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setContentView() {
        activity.setContentView(getContentView());
    }

    public abstract int getContentView();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMeterCode() {
        return meterCode;
    }

    public void setMeterCode(String meterCode) {
        this.meterCode = meterCode;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public abstract String getTitle();

}
