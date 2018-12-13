package com.jmesh.controler.ui;

import android.app.Activity;

/**
 * Created by Administrator on 2018/12/10.
 */


import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.jmesh.appbase.base.PermissionUtils;
import com.jmesh.controler.R;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * Created by Administrator on 2018/8/21.
 */

public class ActivityQRCodeScan extends ControlerBaseActivity implements QRCodeView.Delegate {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_qr_code_scan);
        zxingview = findViewById(R.id.zxingview);
        checkPermission();
        initView();
    }

    public boolean hasNavigationBar() {
        return false;
    }

    ZXingView zxingview;


    private void checkPermission() {
        if (PermissionUtils.hasPermission(this, PermissionUtils.Camera, CAMERA_OK)) {
            zxingview.startCamera();
            //开启扫描框
            zxingview.showScanRect();
            zxingview.startSpot();
        }
    }

    public static final int CAMERA_OK = 100;

    @Override
    protected void onStart() {
        super.onStart();
        //打开后置摄像头预览,但并没有开始扫描
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        zxingview.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zxingview.onDestroy();
        super.onDestroy();
    }

    private void initView() {
        //设置二维码的代理
        zxingview.setDelegate(this);
    }

    //扫描成功解析二维码成功后调用,可做对应的操作
    @Override
    public void onScanQRCodeSuccess(String result) {
        //扫描成功后调用震动器
        vibrator();
        //显示扫描结果
        Intent intent = new Intent();
        intent.putExtra(kScanData, result);
        setResult(RESULT_OK, intent);
        finish();
        //再次延时1.5秒后启动
//        zxingview.startSpot();
    }

    public static final String kScanData = "scan_data";

    public static String getScanData(Intent intent) {
        if (intent == null) {
            return null;
        }
        return intent.getStringExtra(kScanData);
    }

    private void vibrator() {
        //获取系统震动服务
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    //扫描失败后调用的方法
    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(this, "相机打开失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_OK) {
            if (requestCode == RESULT_OK) {
                zxingview.startCamera();
                //开启扫描框
                zxingview.showScanRect();
                zxingview.startSpot();
            } else {
                finish();
            }
        }
        zxingview.showScanRect();
    }

    public void onClick(View view) {

    }

    public static void getDeviceInfo(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, ActivityQRCodeScan.class);
        activity.startActivityForResult(intent, requestCode);
    }
}

