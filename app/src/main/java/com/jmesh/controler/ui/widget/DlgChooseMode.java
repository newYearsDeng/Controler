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

public class DlgChooseMode extends DlgBase {
    public DlgChooseMode(@NonNull Context context) {
        super(context);
    }

    @Override
    public View getContentView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dlg_air_conditioner_choose_mode, null);
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

    private void initEvent() {
        dlgModeRefrigerationIcon.setOnClickListener(this);
        dlgModeRefrigerationText.setOnClickListener(this);
        dlgModeHeatIcon.setOnClickListener(this);
        dlgModeHeatText.setOnClickListener(this);
        dlgModeDehumidificationIcon.setOnClickListener(this);
        dlgModeDehumidificationText.setOnClickListener(this);
        dlgModeSimpleBlowIcon.setOnClickListener(this);
        dlgModeSimpleBlowText.setOnClickListener(this);
        dlgModeSimpleAutoIcon.setOnClickListener(this);
        dlgModeSimpleAutoText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (chooseCallback == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.dlg_mode_refrigeration_icon:
            case R.id.dlg_mode_refrigeration_text:
                chooseCallback.onChoose(AirConditionerData.modeRefrigeration);
                dimiss();
                break;
            case R.id.dlg_mode_heat_icon:
            case R.id.dlg_mode_heat_text:
                chooseCallback.onChoose(AirConditionerData.modeHeat);
                dimiss();
                break;
            case R.id.dlg_mode_dehumidification_icon:
            case R.id.dlg_mode_dehumidification_text:
                chooseCallback.onChoose(AirConditionerData.modeHumidification);
                dimiss();
                break;
            case R.id.dlg_mode_simple_blow_icon:
            case R.id.dlg_mode_simple_blow_text:
                chooseCallback.onChoose(AirConditionerData.modeSimpleBlow);
                dimiss();
                break;
            case R.id.dlg_mode_simple_auto_icon:
            case R.id.dlg_mode_simple_auto_text:
                chooseCallback.onChoose(AirConditionerData.modeAuto);
                dimiss();
                break;

        }
    }

    public void disableAuto() {
        dlgModeSimpleAutoIcon.setVisibility(GONE);
        dlgModeSimpleAutoText.setVisibility(GONE);
    }

    public void disableHumidification() {
        dlgModeDehumidificationIcon.setVisibility(GONE);
        dlgModeDehumidificationText.setVisibility(GONE);
    }

    public void disableBlow() {
        dlgModeSimpleBlowIcon.setVisibility(GONE);
        dlgModeSimpleBlowText.setVisibility(GONE);
    }

    private JmeshDraweeView dlgModeRefrigerationIcon;
    private TextView dlgModeRefrigerationText;
    private JmeshDraweeView dlgModeHeatIcon;
    private TextView dlgModeHeatText;
    private JmeshDraweeView dlgModeDehumidificationIcon;
    private TextView dlgModeDehumidificationText;
    private JmeshDraweeView dlgModeSimpleBlowIcon;
    private TextView dlgModeSimpleBlowText;
    private JmeshDraweeView dlgModeSimpleAutoIcon;
    private TextView dlgModeSimpleAutoText;

    private void assignViews(View view) {
        dlgModeRefrigerationIcon = (JmeshDraweeView) view.findViewById(R.id.dlg_mode_refrigeration_icon);
        dlgModeRefrigerationText = (TextView) view.findViewById(R.id.dlg_mode_refrigeration_text);
        dlgModeHeatIcon = (JmeshDraweeView) view.findViewById(R.id.dlg_mode_heat_icon);
        dlgModeHeatText = (TextView) view.findViewById(R.id.dlg_mode_heat_text);
        dlgModeDehumidificationIcon = (JmeshDraweeView) view.findViewById(R.id.dlg_mode_dehumidification_icon);
        dlgModeDehumidificationText = (TextView) view.findViewById(R.id.dlg_mode_dehumidification_text);
        dlgModeSimpleBlowIcon = (JmeshDraweeView) view.findViewById(R.id.dlg_mode_simple_blow_icon);
        dlgModeSimpleBlowText = (TextView) view.findViewById(R.id.dlg_mode_simple_blow_text);
        dlgModeSimpleAutoIcon = (JmeshDraweeView) view.findViewById(R.id.dlg_mode_simple_auto_icon);
        dlgModeSimpleAutoText = (TextView) view.findViewById(R.id.dlg_mode_simple_auto_text);
    }

    public interface ModeChooseCallback {
        void onChoose(int mode);
    }

    ModeChooseCallback chooseCallback;

    public void setChooseCallback(ModeChooseCallback chooseCallback) {
        this.chooseCallback = chooseCallback;
    }
}
