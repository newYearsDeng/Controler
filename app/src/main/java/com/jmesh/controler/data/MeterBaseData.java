package com.jmesh.controler.data;

/**
 * Created by Administrator on 2018/11/9.
 */

public class MeterBaseData {
    private String name;
    private String value;
    private String unit;

    public MeterBaseData(String name, String value, String unit) {
        this.name = name;
        this.value = value;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
