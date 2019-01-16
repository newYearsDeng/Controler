package com.jmesh.controler.ui.devicecontrol;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;

import com.jmesh.appbase.BaseApplication;
import com.jmesh.appbase.base.HandlerUtil;
import com.jmesh.appbase.base.ToastUtils;
import com.jmesh.appbase.ui.widget.JmeshDraweeView;
import com.jmesh.appbase.utils.StringUtil;
import com.jmesh.blebase.base.BleManager;
import com.jmesh.blebase.callback.BleGattCallback;
import com.jmesh.blebase.exception.BleException;
import com.jmesh.blebase.state.BleDevice;
import com.jmesh.blebase.utils.JMeshLog;
import com.jmesh.controler.R;
import com.jmesh.controler.base.ReadingTaskHandler;
import com.jmesh.controler.data.MeterBaseData;
import com.jmesh.controler.data.MeterData;
import com.jmesh.controler.data.dao.DBHelper;
import com.jmesh.controler.data.dao.Device;
import com.jmesh.lib645.task.TaskBase;
import com.jmesh.lib645.task.TaskMeterMeterCurrent;
import com.jmesh.lib645.task.TaskMeterMeterEnergyConsume;
import com.jmesh.lib645.task.TaskMeterMeterFrequency;
import com.jmesh.lib645.task.TaskMeterMeterPower;
import com.jmesh.lib645.task.TaskMeterMeterPowerFactor;
import com.jmesh.lib645.task.TaskMeterMeterVolt;
import com.jmesh.lib645.util.HexUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public static Set<String> connectingDeviceList = new HashSet<>();

    public abstract void onNavRightBnClicked();

    public boolean isDeviceConneced() {
        final String mac = getMac();
        BleDevice bleDevice = BleManager.getInstance().getConnectedDeviceByMac(mac);
        if (bleDevice != null) {
            return true;
        } else return false;
    }

    BluetoothGatt bluetoothGatt;

    public void cancelConnect() {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
        }
    }

    public void connecedDevice() {
        final BleDevice bleDevice = BleManager.getInstance().getConnectedDeviceByMac(mac);
        if (bleDevice != null) {
            if (callback != null) {
                HandlerUtil.runOnUiThreadDelay(new Runnable() {
                    @Override
                    public void run() {
                        callback.onConnectSuccess(bleDevice, null, 0);
                    }
                }, 500);
                return;
            }
        }
        connecting = true;
        if (connectingDeviceList.contains(mac)) {
            return;
        } else {
            bluetoothGatt = BleManager.getInstance().connect(mac, callback, 5000/*ms*/);
            connectingDeviceList.add(mac);
        }
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
                JMeshLog.e("onEvent", "connect_success");
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
            connectingDeviceList.remove(bleDevice.getMac());
            EventBus.getDefault().post(new EventConnectState(EventConnectState.connectFailed));
        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt bluetoothGatt, int i) {
            JMeshLog.e("callback", "onconnectSuccess");
            connectingDeviceList.remove(bleDevice.getMac());
            EventBus.getDefault().post(new EventConnectState(EventConnectState.connectSuccess));
        }

        @Override
        public void onDisConnected(boolean b, BleDevice bleDevice, BluetoothGatt bluetoothGatt, int i) {
            ToastUtils.showToast(BaseApplication.context.getString(R.string.disconnect));
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

    protected MeterData meterData = new MeterData();

    protected abstract void refreshMeterData();

    @Override
    public void onDataCallback(TaskBase data) {
        byte[] resultData = data.getResultData();
        String resultStr = new String(resultData);
        JMeshLog.e(resultStr);
        if (data instanceof TaskMeterMeterEnergyConsume) {
            meterData.setEnergyConsume(new MeterBaseData("耗能", StringUtil.reserve(2, resultStr), "KW·h"));
        } else if (data instanceof TaskMeterMeterVolt) {
            meterData.setVolt(new MeterBaseData("电压", StringUtil.reserve(1, resultStr), "V"));
        } else if (data instanceof TaskMeterMeterCurrent) {
            meterData.setCurrent(new MeterBaseData("电流", StringUtil.reserve(3, resultStr), "A"));
        } else if (data instanceof TaskMeterMeterFrequency) {
            meterData.setFrequency(new MeterBaseData("频率", StringUtil.reserve(2, resultStr), "Hz"));
        } else if (data instanceof TaskMeterMeterPower) {
            meterData.setPower(new MeterBaseData("功率", StringUtil.reserve(4, resultStr), "kw"));
        } else if (data instanceof TaskMeterMeterPowerFactor) {
            meterData.setPowerFactor(new MeterBaseData("功率因数", StringUtil.reserve(3, resultStr), ""));
        }
        refreshMeterData();
    }

}
