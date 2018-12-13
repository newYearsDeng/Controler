package com.jmesh.controler.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jmesh.blebase.utils.JMeshLog;

/**
 * Created by Administrator on 2018/11/20.
 */

public class DrawText extends View {
    String text = "";
    private Paint mPaint;
    private Rect bounds;
    private int drawX = 100;
    private int drawY = 300;
    private Paint.FontMetrics mFontMetrics;

    public DrawText(Context context) {
        super(context);
        init(context);
    }

    public DrawText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DrawText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(200);
        mPaint.setTextAlign(Paint.Align.LEFT);
        bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        mFontMetrics = mPaint.getFontMetrics();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(100);
        drawTextInCenter(canvas, "中间", getMeasuredWidth() / 2, getMeasuredHeight() / 2, paint);

//        //绘制文本
//        canvas.drawText(text, drawX, drawY, mPaint);
//        //top相对位置
//        mPaint.setColor(Color.RED);
//        canvas.drawLine(drawX, drawY + mFontMetrics.top, drawX + bounds.right, drawY + mFontMetrics.top, mPaint);
//        //ascent相对位置
//        mPaint.setColor(Color.BLUE);
//        canvas.drawLine(drawX, drawY + mFontMetrics.ascent, drawX + bounds.right, drawY + mFontMetrics.ascent, mPaint);
//        //descent相对位置
//        mPaint.setColor(Color.GRAY);
//        canvas.drawLine(drawX, drawY + mFontMetrics.descent, drawX + bounds.right, drawY + mFontMetrics.descent, mPaint);
//        //bottom相对位置
//        mPaint.setColor(Color.MAGENTA);
//        canvas.drawLine(drawX, drawY + mFontMetrics.bottom, drawX + bounds.right, drawY + mFontMetrics.bottom, mPaint);
//        //bounds相对位置
//        mPaint.setColor(Color.BLACK);
//        mPaint.setStyle(Paint.Style.STROKE);
//        canvas.drawRect(drawX + bounds.left, drawY + bounds.top, drawX + bounds.right, drawY + bounds.bottom, mPaint);
//        //基准点位置
//        mPaint.setColor(Color.RED);
//        mPaint.setStyle(Paint.Style.FILL);
//        canvas.drawCircle(drawX, drawY, 2, mPaint);
//        //基准线
//        mPaint.setColor(Color.GRAY);
//        canvas.drawLine(drawX, drawY, drawX + bounds.right, drawY, mPaint);
        mPaint.setColor(Color.CYAN);
        mPaint.setStrokeWidth(2);
        canvas.drawPoint(x, y, mPaint);
    }


    float x;
    float y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        JMeshLog.e("x" + event.getX(), "y" + event.getY());
        x = event.getX();
        y = event.getY();
        postInvalidate();
        return super.onTouchEvent(event);
    }

    //drawText 的Y数值是基准线为准的，那么可以取Bounds的一半减去bottom，然后向下唯一这个数值
    private void drawTextInCenter(Canvas canvas, String text, int x, int y, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        int offset = rect.height()/2 - Math.abs(rect.bottom);
        canvas.drawText(text, x - (rect.width() / 2) - rect.left, y + offset, paint);
    }

}