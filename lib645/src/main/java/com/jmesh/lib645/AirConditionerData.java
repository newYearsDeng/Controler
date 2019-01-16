package com.jmesh.lib645;

/**
 * Created by Administrator on 2018/11/13.
 */

public class AirConditionerData {
    public boolean power;
    public int temperture;
    public int mode;
    public static final int modeAuto = 0;
    public static final int modeRefrigeration = 1;
    public static final int modeHumidification = 2;
    public static final int modeSimpleBlow = 3;
    public static final int modeHeat = 4;
    public int windStrength;
    public static final int windStrengthAuto = 0;
    public static final int windStrengthLowLevel = 1;
    public static final int windStrengthMidLevel = 2;
    public static final int windStrengthHighLevel = 3;
    public boolean horizontalScavenging;
    public boolean verticalScavenging;
    public int airConditionerType;


    public static final String kAirConditionerType = "air_conditioner_type";
    public static final String kPower = "power";
    public static final String kMode = "mode";
    public static final String kTemperture = "temperture";
    public static final String kWindStrength = "wind_strength";
    public static final String kHorizontalScavenging = "horizontal_scavenging";
    public static final String kVerticalScavenging = "vertical_scavenging";

    public int getAirConditionerType() {
        return airConditionerType;
    }

    public void setAirConditionerType(int airConditionerType) {
        this.airConditionerType = airConditionerType;
    }

    public boolean isPower() {
        return power;
    }

    public void setPower(boolean power) {
        this.power = power;
    }

    public int getTemperture() {
        return temperture;
    }

    public void setTemperture(int temperture) {
        this.temperture = temperture;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getWindStrength() {
        return windStrength;
    }

    public void setWindStrength(int windStrength) {
        this.windStrength = windStrength;
    }

    public boolean isHorizontalScavenging() {
        return horizontalScavenging;
    }

    public void setHorizontalScavenging(boolean horizontalScavenging) {
        this.horizontalScavenging = horizontalScavenging;
    }

    public boolean isVerticalScavenging() {
        return verticalScavenging;
    }

    public void setVerticalScavenging(boolean verticalScavenging) {
        this.verticalScavenging = verticalScavenging;
    }


}
