package com.jmesh.controler.ui;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jmesh.appbase.ui.BaseActivity;
import com.jmesh.appbase.ui.widget.JmeshDraweeView;
import com.jmesh.appbase.utils.Density;
import com.jmesh.appbase.utils.StatusBarUtil;
import com.jmesh.blebase.utils.JMeshLog;
import com.jmesh.controler.R;

/**
 * Created by Administrator on 2018/11/8.
 */

public class ControlerBaseActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FrameLayout frameLayout = findViewById(R.id.root_view);
        if (frameLayout == null) {
            return;
        }
    }


    @Override
    protected boolean isReserveStatuBarHeight() {//是否保留状态栏高度
        return true;
    }

    @Override
    protected boolean isStatusBarTransparent() {
        return true;
    }

    @Override
    protected ImageView getBackgroundImage() {
        JmeshDraweeView jmeshDraweeView = new JmeshDraweeView(this);
        jmeshDraweeView.setNativeDrawable(R.mipmap.background);
        return jmeshDraweeView;
    }
}
