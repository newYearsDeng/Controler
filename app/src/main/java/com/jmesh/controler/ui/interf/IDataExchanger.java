package com.jmesh.controler.ui.interf;

/**
 * Created by Administrator on 2018/11/5.
 */

public interface  IDataExchanger {
    void connect();

    void send();

    void onDataReceive();
}
