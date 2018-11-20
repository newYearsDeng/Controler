package com.jmesh.appbase.network;

import android.text.TextUtils;

import com.jmesh.appbase.BaseApplication;
import com.jmesh.appbase.R;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/6.
 */

public class NetResult {
    public static final int kCodeSucc = 0;

    public static final int kCodeBadResponse = -50;
    public static final int kCodeBreakPipleLine = -51;
    public static final int kCodeNetError = -52;
    public static final int kCodeErrorJson = -53;
    public static final int kCodeFileInvalid = -54;

    public static final int kCodeCopyFileFail = -100;
    public static final int kCodeReadFileFail = -101;

    public static final int kCodeNetUnConnect = -102;

    public static String kMsgCodeBadResponse;
    public static String kMsgCodeBreakPipleLine;
    public static String kMsgCodeNetError;
    public static String kMsgCodeErrorJson;
    public static String kMsgCodeFileInvalid;
    public static String kMsgCodeCopyFileFail;
    public static String kMsgCodeNetUnConnect;

    private String msg;
    private int code;
    private JSONObject data;
    public long sentRequestAtMillis;
    public long receivedResponseAtMillis;
    public Call call;

    public String msg() {
        return msg;
    }

    public int code() {
        return code;
    }

    public JSONObject data() {
        return data;
    }

    public NetResult(int errCode, String errMsg, JSONObject jsonData) {
        code = errCode;
        msg = errMsg;
        data = jsonData;
    }

    public boolean ok() {
        return code == kCodeSucc;
    }

    public NetResult(int errCode) {
        code = errCode;
        getMsgByCode(errCode);
    }

    public void getMsgByCode(int errorCode) {
        switch (errorCode) {
            case kCodeSucc:
                msg = "ok";
                break;
            case kCodeBadResponse:
                msg = kMsgCodeBadResponse = getDefString(kMsgCodeBadResponse, R.string.net_error_bad_response);
                break;
            case kCodeCopyFileFail:
                msg = kMsgCodeCopyFileFail = getDefString(kMsgCodeCopyFileFail, R.string.net_error_copy_file_fail);
                break;
            case kCodeBreakPipleLine:
                msg = kMsgCodeBreakPipleLine = getDefString(kMsgCodeBreakPipleLine, R.string.net_error_break_pipe_line);
                break;
            case kCodeNetError:
                msg = kMsgCodeNetError = getDefString(kMsgCodeNetError, R.string.net_error_net_error);
                break;
            case kCodeErrorJson:
                msg = kMsgCodeErrorJson = getDefString(kMsgCodeErrorJson, R.string.net_error_data_format_error);
                break;
            case kCodeNetUnConnect:
                msg = kMsgCodeNetUnConnect = getDefString(kMsgCodeNetUnConnect, R.string.net_error_net_un_connect);
                break;
            case kCodeFileInvalid:
                msg = kMsgCodeFileInvalid = getDefString(kMsgCodeFileInvalid, R.string.net_error_file_invalid);
                break;
            default:
                msg = "unknow error";
        }
    }

    private String getDefString(String str, int strId) {
        if (TextUtils.isEmpty(str)) {
            str = BaseApplication.context.getResources().getString(strId);
        }
        return str;
    }

    public NetResult(int statusCode, Headers headers) {
        code = statusCode;
    }

    public NetResult(int errCode, String errorMsg) {
        code = errCode;
        msg = errorMsg;
    }

    public NetResult(JSONObject jsonObject) {
        if (jsonObject.has("code")) {
            code = jsonObject.optInt("code", 0);
            msg = jsonObject.optString("msg", null);
        } else {
            code = 0;
            msg = null;
        }

        data = jsonObject;
    }

    public NetResult(Response response) {
        code = response.code();
        msg = response.message();
        try {
            data = new JSONObject(response.body().string());
        } catch (Throwable e) {
        }
    }

    public static NetResult netResultForRequest(IOException e) {
        String msg = e.getLocalizedMessage();
        if (msg == null) {
            msg = BaseApplication.context.getResources().getString(R.string.net_error_net_error);
        }
        return new NetResult(kCodeNetError, msg);
    }

    public static NetResult sussRes() {
        return new NetResult(kCodeSucc);
    }

    public static NetResult netUnConnected() {
        return new NetResult(kCodeNetUnConnect);
    }
}

