package com.jmesh.appbase.ui.widget;

import android.content.Context;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.jmesh.appbase.utils.Logger;

/**
 * Created by Administrator on 2018/7/23.
 */

public abstract class CustomView extends View {
    public static final String LogTag = CustomView.class.getSimpleName();

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    int width;
    int height;

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }


    private RectF centreMaxArc;

    public RectF getMaxRect() {
        if (centreMaxArc != null) {
            return centreMaxArc;
        }
        centreMaxArc = new RectF();
        if (width > height) {
            centreMaxArc.left = (int) (0.5f * width - 0.5f * height);
            centreMaxArc.top = 0;
            centreMaxArc.bottom = height;
            centreMaxArc.right = centreMaxArc.left + height;
        } else {
            centreMaxArc.left = 0;
            centreMaxArc.right = width;
            centreMaxArc.top = (int) (0.5f * height - 0.5 * width);
            centreMaxArc.bottom = centreMaxArc.top + width;
        }
        Logger.e(LogTag, centreMaxArc.left + "", centreMaxArc.top + "", centreMaxArc.right + "", centreMaxArc.bottom + "");
        return centreMaxArc;
    }

    public RectF getMaxRect(int reserveWidth) {
        RectF rectF = getMaxRect();
        float left = rectF.left +reserveWidth;
        left =left>(rectF.right-rectF.left)/2?( rectF.right-rectF.left)/2:left;
        float top = rectF.top+reserveWidth;
        top = top>(rectF.bottom-rectF.top)/2?(rectF.bottom-rectF.top)/2:top;
        float right = rectF.right -reserveWidth;
        right = right>left?right:left;
        float bootom = rectF.bottom - reserveWidth;
        return new RectF(left,top,right,bootom);
    }

    public abstract void stopAnimation();
    public abstract void removeHandler();

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
        removeHandler();
    }




}
