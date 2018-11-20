package com.jmesh.appbase.ui.widget;

/**
 * Created by Administrator on 2018/7/9.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by virl on 15/7/16.
 */
public class Circle extends View {
    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public Circle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public Circle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private Paint paint;
    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        if(attrs==null) {
            return;
        }
        int color = Color.parseColor("#ffff0000");
        setColor(color);
    }

    public void setColor(int color) {
        paint.setColor(color);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getMeasuredWidth();
        float radius = w / 2.0f;
        float x = radius;
        float y = x;
        canvas.drawCircle(x, y, radius, paint);
    }
}
