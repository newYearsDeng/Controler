package com.jmesh.appbase.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.jmesh.appbase.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/1.
 * finished
 */

public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * flowLayout 不允许高度上matchParent,只能是warp_content或者是精确数值
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int rowWidth = 0;
        int rowHeight = 0;
        int maxWidth = 0;
        int maxHeight = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
            int childHeight = childView.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
            if (rowWidth + childWidth > widthSize) {
                //当子View的宽度大于父View的这个，这次测量丢掉，排在下面,重新设置rowWidth和rowHeight
                maxHeight += rowHeight;
                rowWidth = 0;
                rowHeight = 0;
                --i;
                continue;
            } else {
                rowWidth += childWidth;
                maxWidth = Math.max(maxWidth, rowWidth);
                rowHeight = Math.max(rowHeight, childHeight);
            }
        }
        setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec), resolveSize(maxHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int currentLeft = 0;
        int currentTop = 0;
        int maxBottom = 0;
        List<ReadyLayoutView> readyLayoutViewList = new ArrayList<>();
        int rowHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            MarginLayoutParams param = (MarginLayoutParams) child.getLayoutParams();

            int childMarginLeft = param.leftMargin;
            int childMarginRifht = param.rightMargin;
            int childMarginTop = param.topMargin;
            int childMarginBottom = param.bottomMargin;

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            int childLeft = currentLeft + childMarginLeft;
            int childRight = childLeft + childWidth;
            int childTop = currentTop + childMarginTop;
            int childBottom = childTop + childHeight;
            if (childRight > right) {
                doLayout(readyLayoutViewList, rowHeight);
                rowHeight = 0;
                --i;
                currentTop = maxBottom;
                currentLeft = 0;
                continue;
            } else {
                rowHeight = Math.max(rowHeight, childHeight + childMarginTop + childMarginBottom);
                maxBottom = Math.max(maxBottom, childBottom + childMarginBottom);
                currentLeft += childMarginLeft + childMarginRifht + childWidth;
                readyLayoutViewList.add(new ReadyLayoutView(child, childLeft, childTop, childRight, childBottom));
                if (i == childCount - 1) {
                    doLayout(readyLayoutViewList, rowHeight);
                }
            }

        }
    }

    private void doLayout(List<ReadyLayoutView> readyLayoutViews, int rowHight) {
        for (int i = 0; i < readyLayoutViews.size(); i++) {
            ReadyLayoutView readyLayoutView = readyLayoutViews.get(i);
            LayoutParams layoutParams = (LayoutParams) readyLayoutView.view.getLayoutParams();
            if (layoutParams.topMargin != 0 || layoutParams.bottomMargin != 0) {
                readyLayoutView.view.layout(readyLayoutView.left, readyLayoutView.top, readyLayoutView.right, readyLayoutView.bottom);
                continue;
            }
            if (layoutParams.gravity == LayoutParams.CENTER_VERTICAL) {
                int top = (rowHight - readyLayoutView.view.getMeasuredHeight()) / 2 + readyLayoutView.top;
                int bottom = top + readyLayoutView.view.getMeasuredHeight();
                readyLayoutView.view.layout(readyLayoutView.left, top, readyLayoutView.right, bottom);
            } else if (layoutParams.gravity == LayoutParams.BOTTOM) {
                int top = rowHight - readyLayoutView.view.getMeasuredHeight() + readyLayoutView.top;
                int bottom = top + readyLayoutView.view.getMeasuredHeight();
                readyLayoutView.view.layout(readyLayoutView.left, top, readyLayoutView.right, bottom);
            } else {
                readyLayoutView.view.layout(readyLayoutView.left, readyLayoutView.top, readyLayoutView.right, readyLayoutView.bottom);
            }
        }
        readyLayoutViews.clear();
    }

    class ReadyLayoutView {
        private View view;
        private int left;
        private int top;
        private int right;
        private int bottom;

        public ReadyLayoutView(View view, int left, int top, int right, int bottom) {
            this.view = view;
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FlowLayout.LayoutParams(getContext(), attrs);
    }

    public class LayoutParams extends MarginLayoutParams {
        int gravity;
        public static final int TOP = 101;
        public static final int CENTER_VERTICAL = 100;
        public static final int BOTTOM = 102;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.FlowLayout_Layout);
            gravity = typedArray.getInt(R.styleable.FlowLayout_Layout_layout_gravity, 0);
            typedArray.recycle();
        }
    }
}
