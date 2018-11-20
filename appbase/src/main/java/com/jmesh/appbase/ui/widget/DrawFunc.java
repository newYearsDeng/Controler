package com.jmesh.appbase.ui.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Administrator on 2018/11/20.
 */

public class DrawFunc {
    public static void drawTextInCenter(Canvas canvas, String text, int x, int y, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        int offset = rect.height() / 2 - Math.abs(rect.bottom);
        canvas.drawText(text, x - (rect.width() / 2) - rect.left, y + offset, paint);
    }
}
