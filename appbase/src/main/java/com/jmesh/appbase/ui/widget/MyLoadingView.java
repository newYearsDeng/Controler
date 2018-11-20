package com.jmesh.appbase.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.jmesh.appbase.R;
import com.jmesh.appbase.utils.Density;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/7/23.
 */

public class MyLoadingView extends CustomView {
    public static final String LogTag = MyLoadingView.class.getSimpleName();

    public MyLoadingView(Context context) {
        super(context, null, 0);
        init();
    }

    public MyLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public MyLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initPaint();

    }

    public void setLoadingColor(int color) {
        loadingPaint.setColor(color);
    }

    Paint loadingPaint;
    Paint transparentPaint;

    int loadingColor;
    int loadingStrokeWidth;

    private void initPaint() {
        loadingPaint = new Paint();
        transparentPaint = new Paint();
        loadingColor = getResources().getColor(R.color.app_color);
        loadingPaint.setColor(loadingColor);
        transparentPaint.setColor(getResources().getColor(R.color.transparent));
        loadingStrokeWidth = Density.dip2px(getContext(), 10);
        loadingPaint.setStyle(Paint.Style.STROKE);
        loadingPaint.setStrokeWidth(loadingStrokeWidth);
        loadingPaint.setAntiAlias(true);
        transparentPaint.setStyle(Paint.Style.STROKE);
        transparentPaint.setStrokeWidth(loadingStrokeWidth);
        transparentPaint.setAntiAlias(true);
    }

    RectF maxRect;

    @Override
    public void onDraw(Canvas canvas) {
        if (maxRect == null) {
            maxRect = getMaxRect(loadingStrokeWidth / 2);
        }
        canvas.drawArc(maxRect, transparentAngle % 360, loadingAngle - transparentAngle, false, loadingPaint);
        canvas.drawArc(maxRect, loadingAngle % 360, 360 - (loadingAngle - transparentAngle), false, transparentPaint);
    }

    public static final int kMessDraw = 100;


    float loadingAngle = 10;
    float transparentAngle = 0;
    int loadingAngleSpeed = 4;
    int transparentAngleSpeed = 1;

    private void updateStatus() {
        if (Math.abs(transparentAngle % 360 - loadingAngle % 360) < 3) {
            int tmp = loadingAngleSpeed;
            loadingAngleSpeed = transparentAngleSpeed;
            transparentAngleSpeed = tmp;
        }
        transparentAngle = (transparentAngle + transparentAngleSpeed);
        loadingAngle = (loadingAngle + loadingAngleSpeed);
    }

    Timer timer;
    TimerTask timerTask;

    public void startLoading() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                postInvalidate();
                updateStatus();
            }
        };
        timer.schedule(timerTask, 0, 10);
    }

    public void stopLoading() {
        timer.cancel();
    }

    @Override
    public void stopAnimation() {
        if (timer != null) {
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    @Override
    public void removeHandler() {

    }

}
