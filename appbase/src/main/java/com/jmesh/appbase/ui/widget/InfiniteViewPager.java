package com.jmesh.appbase.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import com.jmesh.appbase.utils.Logger;

/**
 * Created by Administrator on 2018/8/23.
 */

public class InfiniteViewPager extends ViewGroup {

    public InfiniteViewPager(Context context) {
        this(context, null, 0);
    }

    public InfiniteViewPager(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InfiniteViewPager(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
        addContainer();
        overScroller = new OverScroller(getContext());
        velocityTracker = VelocityTracker.obtain();
    }

    int mTouchSlop;
    OverScroller overScroller;
    VelocityTracker velocityTracker;

    @Override
    public void addView(View view) {
        if (getChildCount() >= 3) {
            return;
        }
        if (view instanceof Container) {
            super.addView(view);
        } else return;
    }

    @Override
    public void addView(View view, int index) {
        if (getChildCount() >= 3) {
            return;
        }
        if (view instanceof Container) {
            super.addView(view, index);
        } else return;
    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        if (getChildCount() >= 3) {
            return;
        }
        if (view instanceof Container) {
            super.addView(view, layoutParams);
        } else return;
    }

    @Override
    public void addView(View view, int width, int height) {
        if (getChildCount() >= 3) {
            return;
        }
        if (view instanceof Container) {
            super.addView(view, width, height);
        } else return;
    }

    @Override
    public void addView(View view, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() >= 3) {
            return;
        }
        if (view instanceof Container) {
            super.addView(view, index, params);
        } else return;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            view.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));//这里不支持Wrap_content
        }
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));//不支持Wrap_content
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int usedWidth = 0;
        for (int i = 0; i < childCount && i < 3; i++) {
            View view = getChildAt(i);
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
            view.layout(left + usedWidth, top, left + usedWidth + width, bottom + height);
            usedWidth += width;
        }
        firstPageX = getChildAt(0).getX();
        secondPageX = getChildAt(1).getX();
        thirdPageX = getChildAt(2).getX();
        scrollToPageImmediately(1);

    }

    private void bindItem(Container container, int itemIndex) {
        if (mAdapter == null) {
            return;
        }
        container.removeAllViews();
        View view = mAdapter.getItem(itemIndex);
        container.addView(view);
        container.setItemIndex(itemIndex);
    }

    private void bindItem() {
        if (mAdapter == null) {
            return;
        }

        for (int i = 0; i < getChildCount(); i++) {
            Container container = (Container) getChildAt(i);
            if (container != null) {
                int containerIndex = container.getItemIndex();
                if (containerIndex != (currentIndex + mAdapter.getItemCount() - 1 + i) % mAdapter.getItemCount()) {
                    bindItem(container, (currentIndex + mAdapter.getItemCount() - 1 + i) % mAdapter.getItemCount());
                }
            }
        }
    }

    private int currentIndex;

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }


    PagerAdapter mAdapter;

    public void setAdapter(PagerAdapter pagerAdapter) {
        this.mAdapter = pagerAdapter;
        addContainer();
        bindItem();
    }

    /**
     * 将3个FrameLayout添加进这个ViewGroup里面.
     **/


    private void addContainer() {
        removeAllViews();
        for (int i = 0; i < 3; i++) {
            Container container = new Container(getContext());
            if (i == 0) {
                container.setBackgroundColor(Color.parseColor("#112233"));
            } else if (i == 1) {
                container.setBackgroundColor(Color.parseColor("#223311"));
            } else if (i == 2) {
                container.setBackgroundColor(Color.parseColor("#331122"));
            }
            addView(container);
        }
    }

    class Container extends FrameLayout {

        public int itemIndex = -1;

        public int getItemIndex() {
            return itemIndex;
        }

        public void setItemIndex(int itemIndex) {
            this.itemIndex = itemIndex;
        }

        public Container(@NonNull Context context) {
            super(context);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            String s = "";
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    s = "down";
                    break;
                case MotionEvent.ACTION_UP:
                    s = "up";
                    break;
                case MotionEvent.ACTION_MOVE:
                    s = "move";
                    break;
                case MotionEvent.ACTION_CANCEL:
                    s = "cancel";
                    break;
            }
            Logger.e("container_onInterceptTouchEvent" + " " + s);
            return super.onInterceptTouchEvent(event);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            String s = "";
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    s = "down";
                    break;
                case MotionEvent.ACTION_UP:
                    s = "up";
                    break;
                case MotionEvent.ACTION_MOVE:
                    s = "move";
                    break;
                case MotionEvent.ACTION_CANCEL:
                    s = "cancel";
                    break;
            }
            Logger.e("container_onTouchEvent" + " " + s);
            return super.onTouchEvent(event);
        }

    }

    public static abstract class PagerAdapter {
        public abstract int getItemCount();

        public abstract View getItem(int position);
    }

    float lastPointX;
    float downPointX;

    private boolean isDraging;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (getChildCount() != 3) {
            return super.onInterceptTouchEvent(event);
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            Logger.e("intercept_action_down");
            lastPointX = event.getX();
            downPointX = event.getX();
        }
        if (action == MotionEvent.ACTION_MOVE) {
            Logger.e("intercept_action_move");
            float x = event.getX();
            if (Math.abs(lastPointX - x) > mTouchSlop) {
                isDraging = true;
            }
        }
        if (action == MotionEvent.ACTION_UP) {
            Logger.e("intercept_action_up");
            isDraging = false;
        }
        if (action == MotionEvent.ACTION_CANCEL) {
            Logger.e("intercept_action_cancel");

        }
        return isDraging;
    }

    float firstPageX;
    float secondPageX;
    float thirdPageX;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getChildCount() != 3) {
            return super.onTouchEvent(event);
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            Logger.e("touch_action_down");
        }
        if (action == MotionEvent.ACTION_MOVE) {
            int moveX = (int) event.getX() - (int) lastPointX;
            lastPointX = event.getX();
            scrollTo(getScrollX() - moveX, getScrollY());
            velocityTracker.addMovement(event);
            velocityTracker.computeCurrentVelocity(1000);
        }
        if (action == MotionEvent.ACTION_UP) {
            isDraging = false;
            float pxSpeed = velocityTracker.getXVelocity();
            if (Math.abs(pxSpeed) > 2000) {
                if (pxSpeed > 0) {//手指滑动方向向右
                    int currentPage = getCurrentPage();
                    scrollToPage(currentPage - 1);
                } else {//手指滑动方向向左
                    int currentPage = getCurrentPage();
                    scrollToPage(currentPage + 1);
                }
            } else {
                int currentPage = getCurrentPage();
                scrollToPage(currentPage);
            }
            Logger.e("touch_action_up");
        }
        if (action == MotionEvent.ACTION_CANCEL) {
            Logger.e("touch_action_cancel");
        }
        return true;
    }


    private int getCurrentPage() {
        int scrollX = getScrollX();
        if (scrollX < getMeasuredWidth() / 2) {
            return 0;
        } else if (scrollX > getMeasuredWidth() / 2 && scrollX < getMeasuredWidth() + getMeasuredWidth() / 2) {
            return 1;
        } else return 2;
    }


    int currentPage = 1;

    private void scrollToPage(int i) {
        if (getChildCount() != 3) {
            return;
        }
        isScrolling = true;
        if (i == 0) {
            overScroller.startScroll(getScrollX(), getScrollY(), (int) (firstPageX - getScrollX()), 0);
        } else if (i == 1) {
            overScroller.startScroll(getScrollX(), getScrollY(), (int) (secondPageX - getScrollX()), 0);
        } else if (i == 2) {
            overScroller.startScroll(getScrollX(), getScrollY(), (int) (thirdPageX - getScrollX()), 0);
        }
        invalidate();
    }

    private void scrollToPageImmediately(int i) {
        if (getChildCount() != 3) {
            return;
        }
        if (i == 0) {
            scrollTo((int) firstPageX, getScrollY());
        } else if (i == 1) {
            scrollTo((int) secondPageX, getScrollY());
        } else if (i == 2) {
            scrollTo((int) thirdPageX, getScrollY());
        }
    }

    @Override
    public InfiniteViewPager.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new InfiniteViewPager.LayoutParams(getContext(), attrs);
    }

    public class LayoutParams extends MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
    }

    public static final int firstPageIndex = 0;
    public static final int secondPageIndex = 1;
    public static final int thirdPageIndex = 2;

    private boolean isScrolling;

    @Override
    public void computeScroll() {
        if (getChildCount() != 3) {
            return;
        }
        if (overScroller.computeScrollOffset()) {//当overScroll还能滚动的时候
            Logger.e("computeScroll_offset");
            scrollTo(overScroller.getCurrX(), overScroller.getCurrY());
            invalidate();
        } else if (isScrolling) {//已经滚动完成，如果当前页是第一页，那么将第三页移动到第一页的前面，如果当前是第三页，那么将第一页移动到第三页后面，然后requestLayout
            Logger.e("computeScroll");
            isScrolling = false;
            int currentPage = getCurrentPage();
            if (currentPage == firstPageIndex) {
                View view = getChildAt(thirdPageIndex);
                removeView(view);
                addView(view, firstPageIndex);
                if (mAdapter != null) {
                    currentIndex = (currentIndex - 1) % mAdapter.getItemCount();
                }
            } else if (currentPage == thirdPageIndex) {
                View view = getChildAt(firstPageIndex);
                removeView(view);
                addView(view);
                if (mAdapter != null) {
                    currentIndex = (currentIndex + 1) % mAdapter.getItemCount();
                }
            }
            bindItem();
            requestLayout();
        }
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (getChildCount() != 3) {
            return;
        }
        if (x < 0) {
            super.scrollTo(0, y);
        } else if (x > getMeasuredWidth() * (getChildCount() - 1)) {
            super.scrollTo(getMeasuredWidth() * (getChildCount() - 1), y);
        } else super.scrollTo(x, y);
    }
}
