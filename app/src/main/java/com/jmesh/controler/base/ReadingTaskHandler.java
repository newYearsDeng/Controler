package com.jmesh.controler.base;

import com.jmesh.appbase.base.ToastUtils;
import com.jmesh.blebase.base.BleManager;
import com.jmesh.blebase.bluetooth.GattHandler;
import com.jmesh.blebase.state.BleDevice;
import com.jmesh.blebase.utils.JMeshLog;
import com.jmesh.controler.task.TaskBase;
import com.jmesh.controler.util.HexUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/6.
 */

public class ReadingTaskHandler implements GattHandler.OnNotifyCallback {
    public static final String LogTag = ReadingTaskHandler.class.getSimpleName();

    private ReadingTaskHandler() {

    }

    private static ReadingTaskHandler readingTaskHandler;

    public static ReadingTaskHandler getInstance() {
        if (readingTaskHandler == null) {
            readingTaskHandler = new ReadingTaskHandler();
        }
        return readingTaskHandler;
    }

    public static final String SendCharacterist = "00002afe-0000-1000-8000-00805f9b34fb";
    public static final String NotifyCharacterist = "00002aff-0000-1000-8000-00805f9b34fb";

    private String macStr;
    private TaskBase taskBase;

    public void setMac(String mac) {
        this.macStr = mac;
    }


    List<TaskBase> taskBaseList = new ArrayList<>();

    public void addTask(TaskBase taskBase) {
        this.taskBaseList.add(taskBase);
        if (taskBaseList.size() <= 1) {
            start();
        }
    }

    public void clearAllTask() {
        taskBaseList.clear();
    }

    private boolean hasNextTask() {
        if (taskBaseList.size() > 0) {
            return true;
        } else return false;
    }

    private TaskBase getNextTask() {
        if (taskBaseList.size() < 1) {
            return null;
        } else {
            return taskBaseList.get(0);
        }
    }

    private void removeTask() {
        if (taskBaseList.size() > 0) {
            taskBaseList.remove(0);
        }
    }

    private void setTask(TaskBase task) {
        this.taskBase = task;
    }

    private void send(byte[] data) {
        JMeshLog.e(LogTag, new String(data));
        BleDevice bleDevice = BleManager.getInstance().getConnectedDeviceByMac(macStr);
        if (bleDevice == null) {
            return;
        }
        int notifyInstance = bleDevice.getInstanceIdByUuid(NotifyCharacterist);
        int sendInstance = bleDevice.getInstanceIdByUuid(SendCharacterist);
        GattHandler.getInstance().setOnNotifyCallback(this);
        if (!bleDevice.hasNotified(NotifyCharacterist)) {
            GattHandler.getInstance().setMtu(bleDevice.getKey(), 100);
            GattHandler.getInstance().enableNotify(bleDevice.getKey(), notifyInstance);
        }
        bleDevice.getCharacteristicsByInstanceId(sendInstance);
        GattHandler.getInstance().write(bleDevice.getKey(), sendInstance, data);
    }

    public void start() {
        if (hasNextTask()) {
            setTask(getNextTask());
            handle();
        }
    }

    private void handle() {
        byte[] cmd = taskBase.getCmd();
        byte[] hexCmd = HexUtils.hexStringToBytes(new String(cmd));
        send(hexCmd);
    }


    @Override
    public void onNotifyCallback(int i, int i1, byte[] bytes) {
        byte[] resolvedData = taskBase.resolveData(bytes);
        if (callback != null) {
            taskBase.setResultData(resolvedData);
            callback.onDataCallback(taskBase);
        }
        removeTask();
        start();
    }

    private OnDataCallback callback;

    public void setCallback(OnDataCallback callback) {
        this.callback = callback;
    }

    public interface OnDataCallback {
        void onDataCallback(TaskBase data);
    }

}
