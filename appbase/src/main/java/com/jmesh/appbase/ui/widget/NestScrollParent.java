package com.jmesh.appbase.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.jmesh.appbase.utils.Density;
import com.jmesh.appbase.utils.Logger;

/**
 * Created by Administrator on 2018/8/27.
 */

public class NestScrollParent extends LinearLayout implements NestedScrollingParent {

    public NestScrollParent(Context context) {
        this(context, null);
    }

    public NestScrollParent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }

    NestedScrollingParentHelper nestedScrollingParentHelper;

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes) {
        Logger.e("parent", "onStartNestedScroll");
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
        Logger.e("parent", "onNestedScrollAccepted");
        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {
        Logger.e("parent", "onStopNestedScroll");
        nestedScrollingParentHelper.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Logger.e("parent", "onNestedScroll");
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        Logger.e("parent", "scrollY" + getScrollY());
        if (getScrollY() <= Density.dip2px(getContext(), 50) && dy < 0) {
            scrollBy(0, -dy);
            consumed[1] = dy;
        } else if (getScrollY() >= 0 && dy > 0) {
            scrollBy(0, -dy);
            consumed[1] = dy;
        }

        Logger.e("parent", "onNestedPreScroll");
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        Logger.e("parent", "onNestedFling");
        return false;
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        Logger.e("parent", "onNestedPreFling");
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        Logger.e("parent", "getNestedScrollAxes");
        return 0;
    }
}
