package com.jmesh.appbase.ui;

import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jmesh.appbase.R;
import com.jmesh.appbase.ui.widget.DlgBase;
import com.jmesh.appbase.ui.widget.DlgJmeshBase;
import com.jmesh.appbase.ui.widget.DlgLoading;
import com.jmesh.appbase.ui.widget.JmeshDraweeView;
import com.jmesh.appbase.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/10/31.
 */

public class BaseActivity extends FragmentActivity implements NavigationBar.NavBnClickedListener {
    private NavigationBar navigationBar;

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    public static final int RequestCodePermission = 101;

    public boolean hasNavigationBar() {
        return true;
    }

    protected boolean hasNavBackBn() {
        return true;
    }

    @Override
    protected void onCreate(Bundle arg) {
        super.onCreate(arg);
        super.setContentView(R.layout.activity_base);
        if (isStatusBarTransparent()) {
            StatusBarUtil.setTranslucentStatus(this);
        }
    }

    @Override
    public void setContentView(int layoutId) {
        LayoutInflater lf = LayoutInflater.from(this);
        View contentView = lf.inflate(layoutId, null);
        setContentView(contentView);
    }

    @Override
    public void setContentView(View view) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(view, params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkBluetooth();
    }

    public void checkBluetooth() {
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        FrameLayout rootView = (FrameLayout) findViewById(R.id.root_view);
        view.setId(R.id.content_view);
        ImageView imageView = getBackgroundImage();
        if (rootView != null && imageView != null) {
            imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            rootView.addView(imageView);
        }
        if (hasNavigationBar()) {
            navigationBar = new NavigationBar(this);
            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    getResources().getDimensionPixelSize(R.dimen.oh_sdk_nav_height));
            navigationBar.setTitle(getTitle());
            if (hasNavBackBn()) {
                navigationBar.setLeftBnContent(NavigationBar.backGreyBn(this));
            }
            navigationBar.setNavBnClickedListener(this);
            int topMargin = 0;
            if (!isTransparentNavigation()) {
                topMargin += getResources().getDimensionPixelSize(R.dimen.oh_sdk_nav_height);
            } else {
                navigationBar.setBackgroundColor(Color.TRANSPARENT);
            }
            if (isReserveStatuBarHeight()) {
                params1.topMargin += StatusBarUtil.getStatusBarHeight(this);
                topMargin += StatusBarUtil.getStatusBarHeight(this);
            }
            ((FrameLayout.LayoutParams) params).topMargin = topMargin;
            initNavBar(navigationBar);
            rootView.addView(view, params);
            rootView.addView(navigationBar, params1);

        } else {
            rootView.addView(view, params);
        }
    }


    public boolean leftBnVisible() {
        return true;
    }


    public boolean rightBnVisible() {
        return true;
    }

    public void setTitle(String title) {
        navigationBar.setTitle(title);
    }

    protected void initNavBar(NavigationBar navBar) {

    }

    protected boolean isTransparentNavigation() {
        return false;
    }

    int statusBarHeight;

    protected boolean isReserveStatuBarHeight() {//是否保留状态栏高度
        return false;
    }

    protected boolean isStatusBarTransparent() {
        return false;
    }

    protected ImageView getBackgroundImage() {
        return null;
    }


    @Override
    public void onNavLeftBnClicked() {
        finish();
    }

    @Override
    public void onNavTitleClicked() {

    }

    @Override
    public void onNavRightBnClicked() {

    }

    @Override
    public void onBackPressed() {
        View view = findViewById(R.id.popup_window);
        if (view != null && view instanceof DlgBase) {
            DlgBase dlggBase = (DlgBase) view;
            dlggBase.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void setRightTitle(int rightTitleRes) {
        navigationBar.setRightBnContent(NavigationBar.textBn(this, rightTitleRes));
    }

    public void setRightImageContent(View view) {
        navigationBar.setRightBnContent(view);
    }

    DlgLoading dlgLoading;

    public void showProgress() {
        this.showProgress("");
    }

    public void showProgress(String message) {
        dlgLoading = new DlgLoading(this);
        dlgLoading.setMessage(message);
        dlgLoading.show();
    }

    public void dismissProgress() {
        dlgLoading.dimiss();
    }

    public void setProgressMessage(String s) {
        dlgLoading.setMessage(s);
    }

    public void invisibleSplitLine() {
        if (navigationBar == null) {
            return;
        }
        View splitLine = navigationBar.getSplitLine();
        if (splitLine == null) {
            return;
        }
        splitLine.setVisibility(View.GONE);
    }

    public void setSplitLineColor(int color) {
        if (navigationBar == null) {
            return;
        }
        View splitLine = navigationBar.getSplitLine();
        if (splitLine == null) {
            return;
        }
        splitLine.setBackgroundColor(color);
    }

}