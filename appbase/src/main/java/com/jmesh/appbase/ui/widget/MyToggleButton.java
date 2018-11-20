package com.jmesh.appbase.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jmesh.appbase.R;
import com.jmesh.appbase.utils.ColorUtil;

import java.util.Timer;
import java.util.TimerTask;

public class MyToggleButton extends View implements View.OnClickListener {

    public MyToggleButton(Context context) {
        super(context);
        init();
    }

    public MyToggleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyToggleButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawbackground(canvas);
        drawSwitchText(canvas);
        drawCircle(canvas);
        drawStroke(canvas);
    }


    private void drawSwitchText(Canvas canvas) {
        //先计算出中心点，在利用DrawFunc的公共方法来绘制。
        int centerY = getMeasuredHeight() / 2;
        if (switchState) {
            int centerX = (getMeasuredWidth() - getMeasuredHeight()) / 2;
            textPaint.setColor(switchOnTextColor);
            DrawFunc.drawTextInCenter(canvas, swithcOnText, centerX, centerY, textPaint);
        } else {
            int centerX = getMeasuredHeight() + (getMeasuredWidth() - getMeasuredHeight()) / 2;
            textPaint.setColor(switchOffTextColor);
            DrawFunc.drawTextInCenter(canvas, switchOffText, centerX, centerY, textPaint);
        }

    }


    RectF frame;

    private RectF getFrame() {
        if (frame == null) {
            frame = new RectF(0, 0, width, height);
        }
        return frame;
    }

    private RectF getFrameWithPadding(int padding) {
        RectF rectF = new RectF(padding, padding, width - padding, height - padding);
        return rectF;
    }

    private void drawStroke(Canvas canvas) {
        if (strokeWide == 0 || strokeColor == TRANSPARENT) {
            return;
        }
        canvas.drawRoundRect(getFrameWithPadding((int) strokePaint.getStrokeWidth()), height / 2.0f, height / 2.0f, strokePaint);
    }

    private void drawCircle(Canvas canvas) {
        float y = height / 2.0f;
        float radius = height / 2.0f;
        float fraction = currentValue / (MAX_VALUE * 1.0f - MIN_VALUE);
        float x = radius + fraction * (width - radius * 2);
        canvas.drawCircle(x, y, radius, silderRoundRectPaint);
    }

    private void drawbackground(Canvas canvas) {
        backgroundRoundRectPaint.setColor(ColorUtil.evaluate(currentValue / (1.0f * MAX_VALUE - MIN_VALUE), switchOffColor, switchOnColor));
        RectF rect = new RectF(0, 0, width, height);
        canvas.drawRoundRect(rect, height / 2.0f, height / 2.0f, backgroundRoundRectPaint);
    }

    int height;
    int width;

    private Paint backgroundRoundRectPaint;
    private Paint silderRoundRectPaint;
    private Paint strokePaint;
    private Paint textPaint;
    private Paint.FontMetrics mFontMetrics;

    private void initPaint() {
        backgroundRoundRectPaint = new Paint();
        backgroundRoundRectPaint.setAntiAlias(true);
        backgroundRoundRectPaint.setStyle(Paint.Style.FILL);
        silderRoundRectPaint = new Paint();
        silderRoundRectPaint.setAntiAlias(true);
        silderRoundRectPaint.setStyle(Paint.Style.FILL);
        silderRoundRectPaint.setColor(slideColor);
        strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(strokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeWide);

        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(switchTextSize);
        mFontMetrics = textPaint.getFontMetrics();
    }

    private int BTN_WIDTH = 40;// 宽度
    private int BTN_HEIGHT = 30;// 高度

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getMeasuredHeight();
        width = getMeasuredWidth();
    }

    public static final int defaultDuration = 500;
    public static final int ANIMATION_TIME = 50;

    private void init() {
        setOnClickListener(this);
        switchOnColor = BLUE;
        switchOffColor = GREY;
        slideColor = WHITE;
        switchDuration = defaultDuration;
        initPaint();
    }

    public static final int GREY = Color.parseColor("#ff999999");
    public static final int BLUE = Color.parseColor("#ff438EE0");
    public static final int WHITE = Color.parseColor("#ffffffff");
    public static final int TRANSPARENT = Color.parseColor("#00000000");


    private int switchOnColor;
    private int switchOffColor;
    private int slideColor;
    private int switchDuration;
    private int strokeWide;
    private int strokeColor;

    private int switchOnTextColor;
    private int switchOffTextColor;
    private String swithcOnText = "";
    private String switchOffText = "";
    private int switchTextSize;

    public int getStrokeWide() {
        return strokeWide;
    }

    public void setStrokeWide(int strokeWide) {
        this.strokeWide = strokeWide;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public void setSwitchOnColor(int switchOnColor) {
        this.switchOnColor = switchOnColor;
    }

    public void setSwitchOffColor(int switchOffColor) {
        this.switchOffColor = switchOffColor;
    }

    public void setSlideColor(int slideColor) {
        this.slideColor = slideColor;
    }

    public void setSwitchDuration(int switchDuration) {
        this.switchDuration = switchDuration;
    }

    private void init(AttributeSet attributeSet) {
        setOnClickListener(this);
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.MyToggleButton);
        switchOffColor = typedArray.getColor(R.styleable.MyToggleButton_switch_off_color, GREY);
        switchOnColor = typedArray.getColor(R.styleable.MyToggleButton_switch_on_color, BLUE);
        slideColor = typedArray.getColor(R.styleable.MyToggleButton_slide_color, WHITE);
        strokeColor = typedArray.getColor(R.styleable.MyToggleButton_stroke_color, TRANSPARENT);
        strokeWide = typedArray.getDimensionPixelSize(R.styleable.MyToggleButton_stroke_wide, 0);
        switchOnTextColor = typedArray.getColor(R.styleable.MyToggleButton_switch_on_text_color, getContext().getResources().getColor(R.color.black));
        switchOffTextColor = typedArray.getColor(R.styleable.MyToggleButton_switch_on_text_color, getContext().getResources().getColor(R.color.black));
        swithcOnText = typedArray.getString(R.styleable.MyToggleButton_switch_on_text) == null ? "" : typedArray.getString(R.styleable.MyToggleButton_switch_on_text);
        switchOffText = typedArray.getString(R.styleable.MyToggleButton_switch_off_text) == null ? "" : typedArray.getString(R.styleable.MyToggleButton_switch_off_text);
        switchTextSize = typedArray.getDimensionPixelSize(R.styleable.MyToggleButton_switch_text_size, 0);
        typedArray.recycle();
        initPaint();
    }

    public int getSwitchTextSize() {
        return switchTextSize;
    }

    public void setSwitchTextSize(int switchTextSize) {
        this.switchTextSize = switchTextSize;
    }

    public void setFrame(RectF frame) {
        this.frame = frame;
    }

    public int getSwitchOnTextColor() {
        return switchOnTextColor;
    }

    public void setSwitchOnTextColor(int switchOnTextColor) {
        this.switchOnTextColor = switchOnTextColor;
    }

    public int getSwitchOffTextColor() {
        return switchOffTextColor;
    }

    public void setSwitchOffTextColor(int switchOffTextColor) {
        this.switchOffTextColor = switchOffTextColor;
    }

    public String getSwithcOnText() {
        return swithcOnText;
    }

    public void setSwithcOnText(String swithcOnText) {
        this.swithcOnText = swithcOnText;
    }

    public String getSwitchOffText() {
        return switchOffText;
    }

    public void setSwitchOffText(String switchOffText) {
        this.switchOffText = switchOffText;
    }

    @Override
    public void onClick(View v) {
        if (v != this) {
            return;
        }
        initHandler();
        setSwitch(!switchState);
    }

    public interface SwitchListener {
        void onSwitchStateChange(View view, boolean state);
    }

    private SwitchListener switchListener;

    public void setOnSwitchListener(SwitchListener switchListener) {
        this.switchListener = switchListener;
    }

    private int animateState;//标志动画状态
    private static final int CLOSING = 0;
    private static final int OPENING = 1;


    private boolean switchState;

    public boolean getSwitchState() {
        return this.switchState;
    }

    public void setInitState(boolean switchState) {
        this.switchState = switchState;
        if (switchState) {
            currentValue = MAX_VALUE;
        } else {
            currentValue = MIN_VALUE;
        }
        postInvalidate();
    }

    public void setSwitch(boolean switched) {
        this.switchState = switched;
        if (timer == null) {
            timer = new Timer();
        }
        if (switched) {
            animateState = OPENING;
            currentValue = MIN_VALUE;
        } else {
            animateState = CLOSING;
            currentValue = MAX_VALUE;
        }
        timer = new Timer();
        task = new MyAnimateTask();
        timer.schedule(task, 0, 2);
    }

    private int currentValue = 0;
    private static final int MAX_VALUE = 100;
    private static final int MIN_VALUE = 0;
    Timer timer;
    MyAnimateTask task;

    class MyAnimateTask extends TimerTask {
        @Override
        public void run() {
            if (animateState == CLOSING) {
                currentValue -= (MAX_VALUE - MIN_VALUE) / ANIMATION_TIME;
                currentValue = currentValue < MIN_VALUE ? MIN_VALUE : currentValue;
                if (currentValue == MIN_VALUE && switchListener != null && handler != null) {
                    handler.sendEmptyMessage(UNSWITCHED);
                }
            } else if (animateState == OPENING) {
                currentValue += (MAX_VALUE - MIN_VALUE) / ANIMATION_TIME;
                currentValue = currentValue > MAX_VALUE ? MAX_VALUE : currentValue;
                if (currentValue == MAX_VALUE && switchListener != null && handler != null) {
                    handler.sendEmptyMessage(SWITCHED);
                }
            }
            handler.sendEmptyMessage(MESSAGE_DRAW);
            if (currentValue <= 0 || currentValue >= 100) {
                cancel();
            }
        }
    }

    public static final int MESSAGE_DRAW = 100;
    public static final int SWITCHED = 201;
    public static final int UNSWITCHED = 200;

    Handler handler;

    private void initHandler() {
        if (handler == null) {
            handler = new Handler() {
                @Override
                public void handleMessage(Message message) {
                    switch (message.what) {
                        case MESSAGE_DRAW:
                            invalidate();
                            break;
                        case SWITCHED:
                            if (switchListener != null) {
                                switchListener.onSwitchStateChange(MyToggleButton.this, true);
                            }
                            break;
                        case UNSWITCHED:
                            if (switchListener != null) {
                                switchListener.onSwitchStateChange(MyToggleButton.this, false);
                            }
                    }
                }
            };
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        initHandler();
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnim();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    private void stopAnim() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

}