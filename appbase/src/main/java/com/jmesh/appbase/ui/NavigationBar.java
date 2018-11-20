package com.jmesh.appbase.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmesh.appbase.R;


/**
 * Created by Administrator on 2018/6/30.
 */

public class NavigationBar extends FrameLayout implements View.OnClickListener {

    public NavigationBar(Context context) {
        super(context);
        initView(context);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private FrameLayout leftBn;
    private FrameLayout rightBn;
    private View leftBnContent;
    private View rightBnContent;
    private TextView labelTitle;
    private View splitLine;
    private NavBnClickedListener l;
    private LinearLayout rightBnContainer;

    public void setTitle(CharSequence title) {
        labelTitle.setText(title);
    }

    public void setTitle(int titleRes) {
        labelTitle.setText(titleRes);
    }

    private void initView(Context context) {
        labelTitle = new TextView(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        int margin = getResources().getDimensionPixelSize(R.dimen.nav_title_margin_left_right);
        params.leftMargin = margin;
        params.rightMargin = margin;
        labelTitle.setGravity(Gravity.CENTER);
        labelTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.nav_title_text_size));
        labelTitle.setId(R.id.label_nva_title);
        labelTitle.setTextColor(Color.WHITE);
        labelTitle.setSingleLine(true);
        labelTitle.setEllipsize(TextUtils.TruncateAt.END);
        addView(labelTitle, params);
        rightBnContainer = new LinearLayout(context);
        rightBnContainer.setOrientation(LinearLayout.HORIZONTAL);
        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.RIGHT;
        addView(rightBnContainer, params);
        labelTitle.setOnClickListener(this);
        splitLine = new View(context);
        splitLine.setBackgroundColor(getResources().getColor(R.color.black_10));
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        params.gravity = Gravity.BOTTOM;
        addView(splitLine, params);
    }

    public void setNavTitleView(View titleView) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int margin = getResources().getDimensionPixelSize(R.dimen.nav_title_margin_left_right);
        params.gravity = Gravity.CENTER;
        params.leftMargin = margin;
        params.rightMargin = margin;
        addView(titleView, params);
        labelTitle.setVisibility(GONE);
    }

    public TextView getLabelTitle() {
        return labelTitle;
    }

    public View getLeftBnContent() {
        return leftBnContent;
    }

    public View getRightBnContent() {
        return rightBnContent;
    }

    public View getLeftBn() {
        return leftBn;
    }

    public View getRightBn() {
        return rightBn;
    }

    public View getSplitLine() {
        return splitLine;
    }

    public void invisibleSplitLine() {
        splitLine.setVisibility(GONE);
    }

    public void setSplitLineColor(int color) {
        splitLine.setBackgroundColor(color);
    }

    private void buildLeftBn() {
        leftBn = createBn();
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        leftBn.setId(R.id.bn_nav_left);
        addView(leftBn, params);
    }

    private void buildRightBn() {
        rightBn = createBn();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        rightBn.setId(R.id.bn_nav_right);
        rightBnContainer.addView(rightBn, params);
    }

    private FrameLayout createBn() {
        FrameLayout bn = new FrameLayout(getContext());
        bn.setMinimumWidth(getResources().getDimensionPixelSize(R.dimen.nav_bn_min_width));
        int padding = getResources().getDimensionPixelSize(R.dimen.nav_bn_padding_left_right);
        bn.setPadding(padding, 0, padding, 0);
        return bn;
    }

    public void setLeftBnContent(View leftBnContent) {
        if (null == leftBn) {
            buildLeftBn();
        }
        if (null != this.leftBnContent) {
            leftBn.removeView(this.leftBnContent);
        }
        addBnContentToBn(leftBnContent, leftBn);
        leftBn.setOnClickListener(this);
        this.leftBnContent = leftBnContent;
    }

    public void setLeftRightBnVisible(boolean leftVisible, boolean rightVisible) {
        if (leftVisible) {
            leftBn.setVisibility(View.VISIBLE);
        } else {
            leftBn.setVisibility(View.GONE);
        }

        if (rightVisible) {
            rightBn.setVisibility(VISIBLE);
        } else {
            rightBn.setVisibility(GONE);
        }
    }


    public void setRightBnContent(View rightBnContent) {
        if (null == rightBn) {
            buildRightBn();
        }
        if (null != this.rightBnContent) {
            rightBn.removeView(this.rightBnContent);
        }
        addBnContentToBn(rightBnContent, rightBn);
        rightBn.setOnClickListener(this);
        this.rightBnContent = rightBnContent;
    }

    private void addBnContentToBn(View bnContent, FrameLayout bn) {
        if (bnContent.getLayoutParams() == null) {
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            bn.addView(bnContent, params);
        } else {
            bn.addView(bnContent);
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.label_nva_title) {
            if (l != null) {
                l.onNavTitleClicked();
            }
        } else if (id == R.id.bn_nav_left) {
            if (l != null)
                l.onNavLeftBnClicked();
        } else if (id == R.id.bn_nav_right) {
            if (l != null)
                l.onNavRightBnClicked();
        } else {
            if (navExtraBnClickedListener != null) {
                navExtraBnClickedListener.onNavExtraBnClicked((Integer) view.getTag());
            }
        }
    }

    public interface NavBnClickedListener {
        void onNavLeftBnClicked();

        void onNavTitleClicked();

        void onNavRightBnClicked();
    }

    public void addRightBn(View bnContent, int tag) {
        FrameLayout bn = createBn();
        addBnContentToBn(bnContent, bn);
        bn.setTag(Integer.valueOf(tag));
        bn.setOnClickListener(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        if (null == rightBn) {
            rightBnContainer.addView(bn, params);
        } else {
            rightBnContainer.addView(bn, rightBnContainer.getChildCount() - 1, params);
        }
    }

    public interface NavExtraBnClickedListener {
        void onNavExtraBnClicked(int tag);
    }

    private NavExtraBnClickedListener navExtraBnClickedListener;

    public void setNavExtraBnClickedListener(NavExtraBnClickedListener navExtraBnClickedListener) {
        this.navExtraBnClickedListener = navExtraBnClickedListener;
    }

    public void setNavBnClickedListener(NavBnClickedListener l) {
        this.l = l;
    }

    public static View iconBn(Context context, int resId) {
        ImageView view = new ImageView(context);
        view.setImageResource(resId);
        return view;
    }


    public static View backGreyBn(Context context) {
        return iconBn(context, R.mipmap.navigation_back);
    }

    public static View textBn(Context context, int textRes) {
        return rightTextBn(context, textRes, context.getResources().getColorStateList(R.color.white));
    }

    public static View rightTextBn(Context context, int textRes, ColorStateList textColor) {
        TextView textView = new TextView(context);
        textView.setText(textRes);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                context.getResources().getDimensionPixelSize(R.dimen.nav_text_bn_text_size));
        textView.setTextColor(textColor);
        return textView;
    }
}
