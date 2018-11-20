package com.jmesh.controler.ui.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.jmesh.appbase.ui.widget.JmeshDraweeView;
import com.jmesh.appbase.utils.Density;
import com.jmesh.controler.R;
import com.jmesh.controler.ui.ControlerBaseActivity;
import com.jmesh.controler.ui.devicescan.ActivityDeviceScanBle;

/**
 * Created by Administrator on 2018/11/2.
 */

public class ActivityHome extends ControlerBaseActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home);
        assignViews();
        initNavRightContent();
        setTitle(getString(R.string.device_list));
    }

    private void initNavRightContent() {
        JmeshDraweeView jmeshDraweeView = new JmeshDraweeView(this);
        jmeshDraweeView.setNativeDrawable(R.mipmap.icon_add_white);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(Density.dip2px(this, 24), Density.dip2px(this, 24));
        layoutParams.gravity = Gravity.CENTER;
        jmeshDraweeView.setLayoutParams(layoutParams);
        setRightImageContent(jmeshDraweeView);
    }

    @Override
    public void onNavRightBnClicked() {
        addDevice();
    }

    @Override
    public boolean hasNavBackBn() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapterHomeList.refreshDevice();
        adapterHomeList.notifyDataSetChanged();
    }


    private RecyclerView homeDeviceList;
    private AdapterHomeList adapterHomeList;


    private void assignViews() {
        homeDeviceList = (RecyclerView) findViewById(R.id.home_device_list);
        adapterHomeList = new AdapterHomeList(this);
        homeDeviceList.setLayoutManager(new GridLayoutManager(this, 3));
        homeDeviceList.setAdapter(adapterHomeList);
    }

    private void addDevice() {
        ActivityDeviceScanBle.openActivity(this);
    }

    public boolean leftBnVisible() {
        return false;
    }


}
