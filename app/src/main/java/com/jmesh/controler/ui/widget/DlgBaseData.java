package com.jmesh.controler.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmesh.appbase.ui.widget.DlgBase;
import com.jmesh.appbase.utils.Density;
import com.jmesh.controler.R;

import java.util.List;

/**
 * Created by Administrator on 2018/11/9.
 */

public class DlgBaseData extends DlgBase {
    public DlgBaseData(@NonNull Context context) {
        super(context);
    }

    @Override
    public View getContentView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dlg_base_data, null);
        return view;
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected void initContentView(View contentView) {
        assignViews(contentView);
    }

    private LinearLayout dataList;
    private View dataClose;

    private void assignViews(View view) {
        dataList = view.findViewById(R.id.data_list);
        dataClose = view.findViewById(R.id.data_close);
        dataClose.setOnClickListener(this);
    }

    List<String> datas;

    public void setData(List<String> datas) {
        this.datas = datas;
    }

    @Override
    public void show() {
        super.show();
        if (datas == null) {
            return;
        }
        dataList.removeAllViews();
        for (int i = 0; i < datas.size(); i++) {
            TextView singleData = new TextView(getContext());
            singleData.setText(datas.get(i));
            singleData.setTextSize(19);
            singleData.setTextColor(Color.parseColor("#747474"));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i >= 1) {
                layoutParams.topMargin = Density.dip2px(getContext(), 28);
            }
            singleData.setLayoutParams(layoutParams);
            dataList.addView(singleData);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.data_close) {
            dimiss();
        }
    }
}
