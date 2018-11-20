package com.jmesh.appbase.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import com.jmesh.appbase.utils.Logger;

/**
 * Created by Administrator on 2018/8/27.
 */

public class NestScrollChild extends LinearLayout implements NestedScrollingChild {

    public NestScrollChild(Context context) {
        this(context, null);
    }

    NestedScrollingChildHelper nestedScrollingChildHelper;

    public NestScrollChild(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        nestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        nestedScrollingChildHelper.setNestedScrollingEnabled(true);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        nestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
        Logger.e("child", "setNestedScrollingEnabled");
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        nestedScrollingChildHelper.isNestedScrollingEnabled();
        Logger.e("child", "isNestedScrollingEnabled");
        return false;
    }

    @Override
    public boolean startNestedScroll(int axes) {
        nestedScrollingChildHelper.startNestedScroll(axes);
        Logger.e("child", "startNestedScroll");
        return false;
    }

    @Override
    public void stopNestedScroll() {
        nestedScrollingChildHelper.stopNestedScroll();
        Logger.e("child", "stopNestedScroll");

    }

    @Override
    public boolean hasNestedScrollingParent() {
        Logger.e("child", "hasNestedScrollingParent");
        return nestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
        Logger.e("child", "dispatchNestedScroll");
        return nestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow) {
        Logger.e("child", "dispatchNestedPreScroll");
        return nestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, comsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        Logger.e("child", "dispatchNestedFling");
        return nestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        Logger.e("child", "dispatchNestedPreFling");
        return nestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    public float lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            lastY = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float dy = event.getY() - lastY;
            lastY = event.getY();
            if (nestedScrollingChildHelper.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL) && dispatchNestedPreScroll(0, (int) dy, comsumed, offset)) {
                int remain = (int) dy - comsumed[1];
                if (remain != 0) {
                    scrollBy(0, -remain);
                }
            } else {
                scrollBy(getScrollX(), (int) -dy);
            }

        }
        return true;
    }

    private int[] offset = new int[2];
    private int[] comsumed = new int[2];
}
