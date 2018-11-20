package com.jmesh.controler.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jmesh.appbase.ui.widget.DlgBase;
import com.jmesh.appbase.ui.widget.JmeshDraweeView;
import com.jmesh.controler.R;
import com.jmesh.controler.data.AirConditionerData;

/**
 * Created by Administrator on 2018/11/13.
 */

public class DlgChooseWindStrength extends DlgBase {
    public DlgChooseWindStrength(@NonNull Context context) {
        super(context);
    }

    @Override
    public View getContentView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dlg_choose_wind_strength, null);
        return view;
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected void initContentView(View contentView) {
        assignViews(contentView);
        initEvent();
    }

    private JmeshDraweeView dlgWindStrengthLowIcon;
    private TextView dlgWindStrengthLowText;
    private JmeshDraweeView dlgWindStrengthMidIcon;
    private TextView dlgWindStrengthMindText;
    private JmeshDraweeView dlgWindStrengthHighIcon;
    private TextView dlgWindStrengthHighText;
    private JmeshDraweeView dlgWindStrengthAutoIcon;
    private TextView dlgWindStrengthAutoText;

    private void initEvent() {
        dlgWindStrengthLowIcon.setOnClickListener(this);
        dlgWindStrengthLowText.setOnClickListener(this);
        dlgWindStrengthMidIcon.setOnClickListener(this);
        dlgWindStrengthMindText.setOnClickListener(this);
        dlgWindStrengthHighIcon.setOnClickListener(this);
        dlgWindStrengthHighText.setOnClickListener(this);
        dlgWindStrengthAutoIcon.setOnClickListener(this);
        dlgWindStrengthAutoText.setOnClickListener(this);
    }

    private void assignViews(View view) {
        dlgWindStrengthLowIcon = (JmeshDraweeView) view.findViewById(R.id.dlg_wind_strength_low_icon);
        dlgWindStrengthLowText = (TextView) view.findViewById(R.id.dlg_wind_strength_low_text);
        dlgWindStrengthMidIcon = (JmeshDraweeView) view.findViewById(R.id.dlg_wind_strength_mid_icon);
        dlgWindStrengthMindText = (TextView) view.findViewById(R.id.dlg_wind_strength_mid_text);
        dlgWindStrengthHighIcon = (JmeshDraweeView) view.findViewById(R.id.dlg_wind_strength_high_icon);
        dlgWindStrengthHighText = (TextView) view.findViewById(R.id.dlg_wind_strength_high_text);
        dlgWindStrengthAutoIcon = (JmeshDraweeView) view.findViewById(R.id.dlg_wind_strength_auto_icon);
        dlgWindStrengthAutoText = (TextView) view.findViewById(R.id.dlg_wind_strength_auto_text);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (chooseCallback == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.dlg_wind_strength_low_icon:
            case R.id.dlg_wind_strength_low_text:
                chooseCallback.onChoose(AirConditionerData.windStrengthLowLevel);
                dimiss();
                break;
            case R.id.dlg_wind_strength_mid_icon:
            case R.id.dlg_wind_strength_mid_text:
                chooseCallback.onChoose(AirConditionerData.windStrengthMidLevel);
                dimiss();
                break;
            case R.id.dlg_wind_strength_high_icon:
            case R.id.dlg_wind_strength_high_text:
                chooseCallback.onChoose(AirConditionerData.windStrengthHighLevel);
                dimiss();
                break;
            case R.id.dlg_wind_strength_auto_icon:
            case R.id.dlg_wind_strength_auto_text:
                chooseCallback.onChoose(AirConditionerData.windStrengthAuto);
                dimiss();
                break;
        }
    }


    public interface WindStrengthChooseCallback {
        void onChoose(int windStrength);
    }

    WindStrengthChooseCallback chooseCallback;

    public void setChooseCallback(WindStrengthChooseCallback chooseCallback) {
        this.chooseCallback = chooseCallback;
    }
}
