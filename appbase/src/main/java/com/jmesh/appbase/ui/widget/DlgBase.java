package com.jmesh.appbase.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.jmesh.appbase.R;
import com.jmesh.appbase.base.HandlerUtil;

/**
 * Created by Administrator on 2018/7/16.
 */

public abstract class DlgBase extends FrameLayout implements View.OnClickListener {
    public DlgBase(@NonNull Context context) {
        this(context, null, 0);
    }

    public abstract View getContentView();

    public DlgBase(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DlgBase(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    View contentView;



    private void init() {
        setId(R.id.popup_window);
        setBackgroundColor(getResources().getColor(R.color.black_10));
        this.setOnClickListener(this);
        View view = getContentView();
        contentView = view;
        contentView.setOnClickListener(this);
        addView(view);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = getGravity();
        view.setLayoutParams(layoutParams);
        initContentView(contentView);
    }

    public int getGravity() {
        return Gravity.BOTTOM;
    }

    abstract protected void initContentView(View contentView);


    public void show() {
        if (hasShowed) {
            return;
        }
        hasShowed = true;
        if (!(getContext() instanceof Activity)) {
            return;
        }
        Activity activity = (Activity) getContext();
        FrameLayout root = activity.findViewById(android.R.id.content);
        root.addView(this);
        bringToFront();
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha_show);
        this.startAnimation(animation);
    }

    private boolean hasShowed = false;

    public void dimiss() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha_dimiss);
        this.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                HandlerUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hasShowed = false;
                        if (!(getContext() instanceof Activity)) {
                            return;
                        }
                        Activity activity = (Activity) getContext();
                        FrameLayout root = activity.findViewById(android.R.id.content);
                        if (root != null) {
                            root.removeView(DlgBase.this);
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private boolean backgroundClickable = true;

    public void setBackgroundClickable(boolean clickable) {
        this.backgroundClickable = clickable;
    }

    @Override
    public void onClick(View v) {
        if (v != this) {
            return;
        }
        if (backgroundClickable) {
            dimiss();
        }
    }

    public void onBackPressed() {
        dimiss();
    }
}
