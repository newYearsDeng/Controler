package com.jmesh.appbase.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jmesh.appbase.R;


/**
 * Created by Administrator on 2018/7/23.
 */

public class DlgJmeshBase extends DlgBase implements View.OnClickListener{

    public DlgJmeshBase(@NonNull Context context) {
        super(context, null, 0);
    }

    @Override
    public View getContentView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dlg_jmesh_base, null, false);
        return view;
    }

    public int getGravity(){
        return Gravity.CENTER;
    }

    @Override
    public void onClick(View view){
        super.onClick(view);
        int i = view.getId();
        if (i == R.id.dlg_jmesh_confirm) {
            if (onDlgClickListener != null) {
                onDlgClickListener.onConfirm();
            }
            dimiss();

        } else if (i == R.id.dlg_jmesh_cancel) {
            if (onDlgClickListener != null) {
                onDlgClickListener.onCancel();
            }
            dimiss();

        }
    }


    @Override
    protected void initContentView(View contentView) {
        assignViews();
        initEvent();
        setClickable(true);
    }

    private void initEvent(){
        dlgJmeshConfirm.setOnClickListener(this);
        dlgJmeshCancel.setOnClickListener(this);
    }

    public void setTitle(String title){
        dlgJmeshBaseTitle.setText(title);
    }

    public void setMessage(String message){
        dlgJmeshMessage.setText(message);
    }

    public void setConfirmButtonText(String confirmText){
        dlgJmeshConfirm.setText(confirmText);
    }

    public void setCancelButtonText(String cancelText){
        dlgJmeshCancel.setText(cancelText);
    }

    public void setOnDlgClickListener(OnDlgClick onDlgClick){
        this.onDlgClickListener = onDlgClick;
    }

    public interface OnDlgClick{
        void onConfirm();
        void onCancel();
    }
    private OnDlgClick onDlgClickListener;

    private TextView dlgJmeshBaseTitle;
    private TextView dlgJmeshMessage;
    private TextView dlgJmeshConfirm;
    private TextView dlgJmeshCancel;

    private void assignViews() {
        dlgJmeshBaseTitle = (TextView) findViewById(R.id.dlg_jmesh_base_title);
        dlgJmeshMessage = (TextView) findViewById(R.id.dlg_jmesh_message);
        dlgJmeshConfirm = (TextView) findViewById(R.id.dlg_jmesh_confirm);
        dlgJmeshCancel = (TextView) findViewById(R.id.dlg_jmesh_cancel);
    }
    @Override
    public void dimiss(){
        super.dimiss();
        onDlgClickListener =null;
    }
}
