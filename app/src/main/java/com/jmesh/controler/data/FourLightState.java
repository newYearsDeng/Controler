package com.jmesh.controler.data;

/**
 * Created by Administrator on 2018/12/12.
 */

public class FourLightState {
    private boolean leftLightState;
    private boolean topLightState;
    private boolean rightLightState;
    private boolean bottomLightState;

    public boolean isLeftLightState() {
        return leftLightState;
    }

    public void setLeftLightState(boolean leftLightState) {
        this.leftLightState = leftLightState;
    }

    public boolean isTopLightState() {
        return topLightState;
    }

    public void setTopLightState(boolean topLightState) {
        this.topLightState = topLightState;
    }

    public boolean isRightLightState() {
        return rightLightState;
    }

    public void setRightLightState(boolean rightLightState) {
        this.rightLightState = rightLightState;
    }

    public boolean isBottomLightState() {
        return bottomLightState;
    }

    public void setBottomLightState(boolean bottomLightState) {
        this.bottomLightState = bottomLightState;
    }
}
