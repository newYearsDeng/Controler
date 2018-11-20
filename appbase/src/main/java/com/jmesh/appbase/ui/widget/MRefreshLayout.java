package com.jmesh.appbase.ui.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.jmesh.appbase.utils.Logger;

/**
 * Created by Administrator on 2018/8/10.
 */

public class MRefreshLayout extends SwipeRefreshLayout {
    public static final String LogTag = MRefreshLayout.class.getSimpleName();

    public MRefreshLayout(Context context) {
        this(context, null);
    }

    public MRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        boolean canScrollUp = super.canChildScrollUp();
        Logger.e(LogTag, "canChildScrollUp" + (canScrollUp ? "true" : "false"));
        return canScrollUp;
    }
}
