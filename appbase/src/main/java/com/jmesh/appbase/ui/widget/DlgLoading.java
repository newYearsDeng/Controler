package com.jmesh.appbase.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jmesh.appbase.R;

/**
 * Created by Administrator on 2018/8/28.
 */

public class DlgLoading extends DlgBase {
    public DlgLoading(@NonNull Context context) {
        super(context);
    }


    @Override
    public View getContentView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dlg_loading, null);
        return view;
    }

    private MyLoadingView loadingView;
    private TextView message;

    private void assignViews(View view) {
        loadingView = view.findViewById(R.id.loading_view);
        message = view.findViewById(R.id.message);
    }


    @Override
    protected void initContentView(View contentView) {
        assignViews(contentView);
    }

    String messageStr = "";

    public void setMessage(String messageStr) {
        this.messageStr = messageStr;
        if (message != null) {
            message.setText(messageStr);
        }
    }

    @Override
    public void show() {
        super.show();
        loadingView.startLoading();
        message.setText(messageStr);
    }

    @Override
    public void dimiss() {
        loadingView.stopLoading();
        super.dimiss();
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

}
