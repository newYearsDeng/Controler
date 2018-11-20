package com.jmesh.controler.data;

/**
 * Created by Administrator on 2018/11/14.
 */

public class AirConditionerBrand {
    public String name;
    public int brandId;
    public boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public AirConditionerBrand(String name, int brandId) {
        this.name = name;
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }
}