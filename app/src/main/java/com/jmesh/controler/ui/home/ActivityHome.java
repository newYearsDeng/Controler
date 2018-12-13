package com.jmesh.controler.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jmesh.appbase.base.ToastUtils;
import com.jmesh.appbase.ui.widget.JmeshDraweeView;
import com.jmesh.appbase.utils.Density;
import com.jmesh.blebase.utils.JMeshLog;
import com.jmesh.controler.R;
import com.jmesh.controler.ui.ActivityQRCodeScan;
import com.jmesh.controler.ui.ControlerBaseActivity;
import com.jmesh.controler.ui.devicecontrol.ActivityControlMain;
import com.jmesh.controler.ui.devicescan.ActivityDeviceScanBle;
import com.jmesh.controler.ui.widget.DlgSelect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        DlgSelect dlgSelect = new DlgSelect(this);
        dlgSelect.addItem("获取附近所有设备", new DlgSelect.OnItemClickListener() {
            @Override
            public void onItemClick() {
                ActivityDeviceScanBle.openActivity(ActivityHome.this);
            }
        });
        dlgSelect.addItem("扫描二维码", new DlgSelect.OnItemClickListener() {
            @Override
            public void onItemClick() {
                ActivityQRCodeScan.getDeviceInfo(ActivityHome.this, kGetCodeInfo);
            }
        });
        dlgSelect.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == kGetCodeInfo && resultCode == RESULT_OK) {
            String info = ActivityQRCodeScan.getScanData(data);
            if (TextUtils.isEmpty(info)) {
                return;
            }
            info = info.toLowerCase();
            String regex = "bd[A-Za-z0-9]+\\:[A-Fa-f0-9]{12}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(info);
            while (matcher.find()) {
                String match = matcher.group(0);
                String meterCode = match.split(":")[1];
                int dIndex = match.indexOf('d');
                int colonIndex = match.indexOf(':');
                String type = (String) match.subSequence(dIndex + 1, colonIndex);
                int typeInt = -1;
                try {
                    typeInt = (Integer.parseInt(type));
                } catch (Throwable t) {
                }
                if (typeInt == -1 || TextUtils.isEmpty(meterCode)) {
                    return;
                }
                JMeshLog.e(meterCode);
                ActivityControlMain.openActivity(this, typeInt, meterCode);
            }
        }
    }

    public static final int kGetCodeInfo = 10001;


    public boolean leftBnVisible() {

        return false;
    }

    long lastPresstime;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - lastPresstime) < 3 * 1000) {
            super.onBackPressed();
            return;
        }
        lastPresstime = System.currentTimeMillis();
        ToastUtils.showToast("再按一次返回键退出");
    }


}
