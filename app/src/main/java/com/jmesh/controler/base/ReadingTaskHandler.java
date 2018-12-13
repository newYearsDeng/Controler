package com.jmesh.controler.base;

import com.jmesh.blebase.advertiser.BleAdvertiser;
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

    public static final String SEND_CHARACTERIST = "00002afe00001000800000805f9b34fb";
    public static final String NOTIFY_CHARACTERIST = "00002aff00001000800000805f9b34fb";

    private String macStr;
    private TaskBase currentTask;

    public void setMac(String mac) {
        this.macStr = mac;
    }


    List<TaskBase> taskBaseList = new ArrayList<>();

    public void addTask(TaskBase taskBase) {
        JMeshLog.e("getTask", taskBase.getClass().getSimpleName());
        if (taskBase == null) {
            return;
        }
        for (TaskBase task : taskBaseList) {
            if (task.getClass() == taskBase.getClass()) {//如果存在相同的任务，便不在执行
                return;
            }
        }
        JMeshLog.e("taskSize_now", taskBaseList.size() + "");
        if (this.taskBaseList.size() > 1) {
            this.taskBaseList.add(1, taskBase);
        } else {
            this.taskBaseList.add(taskBase);
        }
        if (taskBaseList.size() <= 1) {
            start();
        }
    }

    public void clearAllTask() {
        JMeshLog.e("clear");
        taskBaseList.clear();
        GattHandler.getInstance().clearAllCmd();
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
            currentTask = null;
        }
    }

    private void setTask(TaskBase task) {
        this.currentTask = task;
    }

    private void send(byte[] data) {
        JMeshLog.e(LogTag, new String(data));
        BleDevice bleDevice = BleManager.getInstance().getConnectedDeviceByMac(macStr);
        if (bleDevice == null) {
            return;
        }
        GattHandler.getInstance().setOnNotifyCallback(this);
        if (!bleDevice.hasNotified(NOTIFY_CHARACTERIST)) {
            GattHandler.getInstance().setMtu(bleDevice.getKey(), 100);
            GattHandler.getInstance().enableNotifyByUUID(bleDevice.getKey(), NOTIFY_CHARACTERIST);
            bleDevice.setNotifyAttInstance(NOTIFY_CHARACTERIST);
        }
        GattHandler.getInstance().writeByUuid(bleDevice.getKey(), SEND_CHARACTERIST, data);
    }

    public void start() {
        if (hasNextTask()) {
            setTask(getNextTask());
            handle();
        }
    }

    private void handle() {
        byte[] cmd = currentTask.getCmd();
        byte[] hexCmd = HexUtils.hexStringToBytes(new String(cmd));
        send(hexCmd);
    }


    @Override
    public void onNotifyCallback(int i, int i1, byte[] bytes, String uuid) {
        if (currentTask == null) {
            start();
            return;
        }
        byte[] resolvedData = currentTask.resolveData(bytes);
        if (callback != null) {
            currentTask.setResultData(resolvedData);
            callback.onDataCallback(currentTask);
        }
        JMeshLog.e("taskSize", taskBaseList.size() + "");
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
