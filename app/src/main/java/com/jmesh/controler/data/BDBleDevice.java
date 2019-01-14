package com.jmesh.controler.data;

import com.jmesh.controler.R;
import com.jmesh.controler.ui.devicecontrol.ControlAirConditioner;
import com.jmesh.controler.ui.devicecontrol.ControlBase;
import com.jmesh.controler.ui.devicecontrol.ControlCenterAirConditioner;
import com.jmesh.controler.ui.devicecontrol.ControlFourLight;
import com.jmesh.controler.ui.devicecontrol.ControlLight;
import com.jmesh.controler.ui.devicecontrol.ControlMeter;
import com.jmesh.controler.ui.devicecontrol.ControlSocket;

/**
 * Created by Administrator on 2018/11/2.
 */

public class BDBleDevice {
    public static final int TYPE_BD_ENERGY_METER = 0;
    public static final int TYPE_BD_WALL_SOCKET = 1;
    public static final int TYPE_BD_MOBILE_SOCKET = 2;
    public static final int TYPE_BD_AIR_CONDITIONER = 3;
    public static final int TYPE_BD_GUIDEWAY__METER = 4;
    public static final int TYPE_BD_LIGHT = 5;
    public static final int TYPE_BD_WATER_METER = 6;
    public static final int TYPE_BD_CENTER_AIRCONDITIONER = 7;
    public static final int TYPE_BD_FOUR_LIGHT = 8;

    public static final String DI_ENERGY_CONSUM = "00000000";//电量
    public static final String DI_VOLTAGE = "02010100";//电压
    public static final String DI_CURRENT = "02020100";//电流
    public static final String DI_POWER = "02030000";//功率
    public static final String DI_FREQENCY = "02800002";//频率
    public static final String DI_POWER_FACTOR = "02060000";//功率因数


    public static String getName(int type) {
        switch (type) {
            case TYPE_BD_ENERGY_METER:
                return "电表";
            case TYPE_BD_WALL_SOCKET:
                return "墙挂式插座表";
            case TYPE_BD_MOBILE_SOCKET:
                return "移动式插座表";
            case TYPE_BD_AIR_CONDITIONER:
                return "空调控制器";
            case TYPE_BD_GUIDEWAY__METER:
                return "导轨表";
            case TYPE_BD_LIGHT:
                return "灯控";
            case TYPE_BD_WATER_METER:
                return "水表";
            case TYPE_BD_CENTER_AIRCONDITIONER:
                return "中央空调";
            case TYPE_BD_FOUR_LIGHT:
                return "四路灯控";
        }
        return "未知设备";
    }

    public static String getNativeImg(int type) {
        switch (type) {
            case TYPE_BD_ENERGY_METER:
                return "icon_device_bd_energy_meter_grey";
            case TYPE_BD_WALL_SOCKET:
                return "icon_device_bd_socket_grey";
            case TYPE_BD_MOBILE_SOCKET:
                return "icon_device_bd_socket_grey";
            case TYPE_BD_AIR_CONDITIONER:
                return "icon_device_bd_water_air_conditioner_grey";
            case TYPE_BD_GUIDEWAY__METER:
                return "icon_device_bd_energy_meter_grey";
            case TYPE_BD_LIGHT:
                return "icon_device_bd_light_grey";
            case TYPE_BD_WATER_METER:
                return "icon_device_bd_water_meter_grey";
            case TYPE_BD_CENTER_AIRCONDITIONER:
                return "icon_device_bd_water_air_conditioner_grey";
            case TYPE_BD_FOUR_LIGHT:
                return "icon_device_bd_light_grey";
        }
        return "icon_device_bd_energy_meter_grey";
    }

    public static String getNativeBigImg(int type) {
        switch (type) {
            case TYPE_BD_ENERGY_METER:
                return "icon_device_bd_energy_meter_big";
            case TYPE_BD_WALL_SOCKET:
                return "icon_device_bd_socket_big";
            case TYPE_BD_MOBILE_SOCKET:
                return "icon_device_bd_socket_big";
            case TYPE_BD_AIR_CONDITIONER:
                return "icon_device_bd_water_air_conditioner_big";
            case TYPE_BD_GUIDEWAY__METER:
                return "icon_device_bd_energy_meter_big";
            case TYPE_BD_LIGHT:
                return "icon_device_bd_light_big";
            case TYPE_BD_WATER_METER:
                return "icon_device_bd_water_meter_big";
            case TYPE_BD_CENTER_AIRCONDITIONER:
                return "icon_device_bd_water_air_conditioner_big";
            case TYPE_BD_FOUR_LIGHT:
                return "icon_device_bd_for_light_big";
        }
        return "icon_device_bd_energy_meter_big";
    }

    public static ControlBase getControl(int type) {
        switch (type) {
            case TYPE_BD_ENERGY_METER:
                return new ControlMeter();
            case TYPE_BD_WALL_SOCKET:
                return new ControlSocket();
            case TYPE_BD_MOBILE_SOCKET:
                return new ControlSocket();
            case TYPE_BD_AIR_CONDITIONER:
                return new ControlAirConditioner();
            case TYPE_BD_GUIDEWAY__METER:
                return new ControlMeter();
            case TYPE_BD_LIGHT:
                return new ControlLight();
            case TYPE_BD_WATER_METER:
                return null;
            case TYPE_BD_FOUR_LIGHT:
                return new ControlFourLight();
            case TYPE_BD_CENTER_AIRCONDITIONER:
                return new ControlCenterAirConditioner();
        }
        return null;
    }

}
