package com.jmesh.controler;

import android.bluetooth.BluetoothGatt;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jmesh.appbase.base.ToastUtils;
import com.jmesh.appbase.ui.BaseActivity;
import com.jmesh.blebase.base.BleManager;
import com.jmesh.blebase.bluetooth.GattHandler;
import com.jmesh.blebase.callback.BleGattCallback;
import com.jmesh.blebase.exception.BleException;
import com.jmesh.blebase.state.BleDevice;
import com.jmesh.blebase.utils.JMeshLog;
import com.jmesh.controler.util.HexUtils;

/**
 * Created by Administrator on 2018/11/2.
 */

public class ActivityTest extends BaseActivity implements View.OnClickListener, GattHandler.OnNotifyCallback {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_test);
//        assignViews();
//        connectBn.setOnClickListener(this);
//        send.setOnClickListener(this);
//        receive.setOnClickListener(this);
    }

    private EditText macEnter;
    private Button connectBn;
    private EditText dataEnter;
    private Button send;
    private TextView receive;


    private void assignViews() {
        macEnter = (EditText) findViewById(R.id.mac_enter);
        macEnter.setText("201811011804");
        connectBn = (Button) findViewById(R.id.connect_bn);
        dataEnter = (EditText) findViewById(R.id.data_enter);
        send = (Button) findViewById(R.id.send);
        receive = (TextView) findViewById(R.id.receive);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_bn:
                connect();
                break;
            case R.id.send:
                send();
                break;
            case R.id.receive:
                break;
        }
    }

    String macStr;

    private void connect() {
        byte[] mac = HexUtils.hexStringToBytes(macEnter.getText().toString());
        String macX = HexUtils.convertToMacString(mac);
        BleManager.getInstance().connect(macX, new BleGattCallback() {
            @Override
            public void onStartConnect() {

            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException e) {

            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt bluetoothGatt, int i) {
                macStr = bleDevice.getMac();
                ToastUtils.showToast("连接成功！");

            }

            @Override
            public void onDisConnected(boolean b, BleDevice bleDevice, BluetoothGatt bluetoothGatt, int i) {

            }
        });
    }
    //11-02 14:20:01.617 2966-2966/com.jmesh.controler E/JMesh: BleDevice character_uuid 00002a00-0000-1000-8000-00805f9b34fb character_instanceID 3
    //11-02 14:20:01.618 2966-2966/com.jmesh.controler E/JMesh: BleDevice character_uuid 00002a01-0000-1000-8000-00805f9b34fb character_instanceID 5
    //            11-02 14:20:01.618 2966-2966/com.jmesh.controler E/JMesh: BleDevice character_uuid 00002a04-0000-1000-8000-00805f9b34fb character_instanceID 7
    //            11-02 14:20:01.618 2966-2966/com.jmesh.controler E/JMesh: BleDevice character_uuid 00002a05-0000-1000-8000-00805f9b34fb character_instanceID 10
    //            11-02 14:20:01.619 2966-2966/com.jmesh.controler E/JMesh: BleDevice character_uuid 00002afe-0000-1000-8000-00805f9b34fb character_instanceID 14
    //            11-02 14:20:01.619 2966-2966/com.jmesh.controler E/JMesh: BleDevice character_uuid 00002aff-0000-1000-8000-00805f9b34fb character_instanceID 16

    public static final String SendCharacterist = "00002afe-0000-1000-8000-00805f9b34fb";
    public static final String NotifyCharacterist = "00002aff-0000-1000-8000-00805f9b34fb";

    private void send() {
        BleDevice bleDevice = BleManager.getInstance().getConnectedDeviceByMac(macStr);
        if (bleDevice == null) {
            return;
        }
        int notifyInstance = bleDevice.getInstanceIdByUuid(NotifyCharacterist);
        int sendInstance = bleDevice.getInstanceIdByUuid(SendCharacterist);
        GattHandler.getInstance().setOnNotifyCallback(this);
        GattHandler.getInstance().enableNotify(bleDevice.getKey(), notifyInstance);
        bleDevice.getCharacteristicsByInstanceId(sendInstance);
        GattHandler.getInstance().write(bleDevice.getKey(), sendInstance, HexUtils.hexStringToBytes("68d2000208182068110433333333c516"));
    }

    @Override
    public void onNotifyCallback(int i, int i1, byte[] bytes, String uuid) {
        JMeshLog.e("callback", HexUtils.formatHexString(bytes));
    }
}
