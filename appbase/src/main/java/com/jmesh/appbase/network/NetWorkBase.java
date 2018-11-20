package com.jmesh.appbase.network;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.jmesh.appbase.utils.ReleaseAble;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class NetWorkBase {

    protected OkHttpClient httpClient;
    protected static Handler handler = new Handler(Looper.getMainLooper());

    protected NetWorkBase() {
        try {
            httpClient = new OkHttpClient();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    static NetWorkBase sNetWork;

    public static void setNetWork(NetWorkBase netWork) {
        sNetWork = netWork;
    }

    public static NetWorkBase netWork() {
        return sNetWork;
    }

    public Call asyncPostInternal(String url, HttpParams params, JMNetCallBack callBack) {
        return asyncDo(HttpMethod.kHttpPost, url, params, true, callBack);
    }

    public Call asyncPost(String url, HttpParams params, JMNetCallBack callBack) {
        return asyncDo(HttpMethod.kHttpPost, url, params, false, callBack);
    }

    public Call asyncPutInternal(String url, HttpParams params, JMNetCallBack callBack) {
        return asyncDo(HttpMethod.kHttpPut, url, params, true, callBack);
    }

    public Call asyncPut(String url, HttpParams params, JMNetCallBack callBack) {
        return asyncDo(HttpMethod.kHttpPut, url, params, false, callBack);
    }

    public Call asyncPatchInternal(String url, HttpParams params, JMNetCallBack callBack) {
        return asyncDo(HttpMethod.kHttpPatch, url, params, true, callBack);
    }

    public Call asyncPatch(String url, HttpParams params, JMNetCallBack callBack) {
        return asyncDo(HttpMethod.kHttpPatch, url, params, false, callBack);
    }

    public Call asyncDeleteInternal(String url, HttpParams params, JMNetCallBack callBack) {
        return asyncDo(HttpMethod.kHttpDelete, url, params, true, callBack);
    }

    public Call asyncDelete(String url, HttpParams params, JMNetCallBack callBack) {
        return asyncDo(HttpMethod.kHttpDelete, url, params, false, callBack);
    }

    public Call asyncGetInternal(String url, HttpParams params, JMNetCallBack callBack) {
        return asyncDo(HttpMethod.kHttpGet, url, params, true, callBack);
    }

    public Call asyncGet(String url, HttpParams params, JMNetCallBack callBack) {
        return asyncDo(HttpMethod.kHttpGet, url, params, false, callBack);
    }

    public Handler getHandler() {
        return handler;
    }

    private boolean checkNetState(final JMNetCallBack callBack) {

//        if (!isNetConnected()) {
//            if (callBack != null) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        callBack.onNetFinished(NetResult.netUnConnected());
//                    }
//                });
//
//            }
//            return false;
//        }
        return true;
    }

    @Nullable
    public Call asyncPost(String url, RequestBody requestBody, JMNetCallBack callBack) {
        try {
            if (!checkNetState(callBack)) {
                return null;
            }
            url = progressUrl(url);
            Request.Builder builder = new Request.Builder();
            progressRequestBuilder(builder);
            builder.post(requestBody);
            builder.url(url);
            Request request = builder.build();

            Call call = httpClient.newCall(request);
            call.enqueue(new HttpCallBack(callBack, url));
            return call;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public Call asyncPostFile(String url, JSONObject formData, String contentType, File file, String name, JMNetCallBack callBack) {
        try {
            if (!checkNetState(callBack)) {
                return null;
            }
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            Iterator<String> iterator = formData.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                builder.addFormDataPart(key, formData.optString(key));
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), file);
            builder.addFormDataPart("file", name, requestBody);
            return asyncPost(url, builder.build(), callBack);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public Call asyncPostFile(String url, HttpParams params, String contentType, File file, String filePartName, String name, JMNetCallBack callBack) {
        try {
            if (!checkNetState(callBack)) {
                return null;
            }
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), file);
            builder.addFormDataPart(filePartName, name, requestBody);
            return asyncPost(url, builder.build(), callBack);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public Call asyncPostFile(String url, Headers headers, File file, JMNetCallBack callBack) {
        try {
            if (!checkNetState(callBack)) {
                return null;
            }
            Request.Builder builder = new Request.Builder();
            progressRequestBuilder(builder);

            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();

            bodyBuilder.addPart(headers, RequestBody.create(MediaType.parse("image/jpeg"), file));
            builder.post(bodyBuilder.build());
            builder.url(url);
            Request request = builder.build();

            Call call = httpClient.newCall(request);
            call.enqueue(new HttpCallBack(callBack, url));
            return call;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public enum HttpMethod {
        kHttpPost,
        kHttpGet,
        kHttpPut,
        kHttpPatch,
        kHttpDelete
    }

    @Nullable
    public Call asyncDo(HttpMethod method, String url, HttpParams params, boolean internalServer, JMNetCallBack callBack) {
        if (!checkNetState(callBack)) {
            return null;
        }
        return asyncDo(method, url, params, internalServer, null, new HttpCallBack(callBack, url, params));
    }

    @Nullable
    public Call asyncDo(HttpMethod method, String url, HttpParams params, boolean internalServer, Map<String, String> headers, Callback callback) {
        try {
            Request.Builder builder = new Request.Builder();
            url = progressUrl(url);
            if (internalServer) {
                if (params == null) {
                    params = HttpParams.genValidParams();
                }
                progressRequestParams(url, params);
            }
            progressRequestBuilder(builder);

            if (method == HttpMethod.kHttpGet) {
                url = buildUrlForGet(url, params);
                builder.get();
            } else {
                configBuilderForMethod(url, builder, params, method);
            }
            if (isIllegalUrl(url)) {
                android.util.Log.e("NetWorkBase", "syncDo error url : " + url);
                return null;
            }
            builder.url(url);
            if (null != headers) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    builder.header(header.getKey(), header.getValue());
                }
            }
            Request request = builder.build();
            Call call = httpClient.newCall(request);
            call.enqueue(callback);
            return call;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public NetResult syncPostInternal(String url, HttpParams params) {
        return syncDo(HttpMethod.kHttpPost, url, params, true, null);
    }

    public NetResult syncDo(HttpMethod method, String url, HttpParams params, boolean internalServer, Map<String, String> headers) {
        try {
            Request.Builder builder = new Request.Builder();
            url = progressUrl(url);
            if (internalServer) {
                if (params == null) {
                    params = HttpParams.genValidParams();
                }
                progressRequestParams(url, params);
            }
            progressRequestBuilder(builder);

            if (method == HttpMethod.kHttpGet) {
                url = buildUrlForGet(url, params);
                builder.get();
            } else {
                configBuilderForMethod(url, builder, params, method);
            }
            if (isIllegalUrl(url)) {
                return null;
            }
            builder.url(url);

            if (null != headers) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    builder.header(header.getKey(), header.getValue());
                }
            }

            Request request = builder.build();

            Call call = httpClient.newCall(request);
            try {
                Response response = call.execute();
                NetResult result = new NetResult(response);
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                onResponsePrint("execute error : " + e.getMessage(), url);
                return NetResult.netResultForRequest(e);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            onResponsePrint("execute error : " + e.getMessage(), url);
            return null;
        }
    }

    private static class HttpCallBack implements Callback, ReleaseAble {

        private JMNetCallBack callBack;
        private String mStrUrl;

        HttpCallBack(JMNetCallBack callBack, String url) {
            this.callBack = callBack;
            mStrUrl = url;
        }

        private HttpParams params;

        HttpCallBack(JMNetCallBack callBack, String url, HttpParams params) {
            this.callBack = callBack;
            mStrUrl = url;
            this.params = params;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            if (callBack == null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        release();
                    }
                });
                return;
            }
            final NetResult result = NetResult.netResultForRequest(e);
            result.call = call;
            sNetWork.onResponsePrint(result.msg(), mStrUrl);
            result.getMsgByCode(result.code());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callBack.onNetFinished(result);
                    release();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (callBack == null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        release();
                    }
                });
                return;
            }
            if (response.isSuccessful()) {
                try {
                    if (TextUtils.isEmpty(response.body().toString())) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onNetFinished(new NetResult(NetResult.kCodeErrorJson));
                                release();
                            }
                        });
                        return;
                    }
                    JSONObject resJson = new JSONObject(response.body().string());
                    final NetResult result = new NetResult(resJson);
                    result.call = call;
                    result.sentRequestAtMillis = response.sentRequestAtMillis();
                    result.receivedResponseAtMillis = response.receivedResponseAtMillis();

                    if (isPrintLog()) {
                        sNetWork.onResponsePrint(resJson.toString(), mStrUrl);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            sNetWork.progressResponse(result);
                            callBack.onNetFinished(result);
                            release();
                        }
                    });
                } catch (Throwable e) {
                    e.printStackTrace();
                    sNetWork.onResponsePrint("json error :" + e.getMessage(), mStrUrl);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onNetFinished(new NetResult(NetResult.kCodeErrorJson));
                            release();
                        }
                    });
                } finally {
                    try {
                        response.body().close();
                    } catch (Throwable e) {
                    }
                }
            } else {
                final NetResult result = new NetResult(response);
                result.call = call;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onNetFinished(result);
                        release();
                    }
                });
                String ip = "";
                try {
                    ip = InetAddress.getByName(call.request().url().host()).getHostAddress();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                sNetWork.onResponsePrint("response is fail code : " + result.code() + ", ip : " + ip, mStrUrl);
            }
        }

        @Override
        public void release() {
            callBack = null;
            if (params != null) {
                params.addToReuse(params);
                params = null;
            }
        }

        private boolean isPrintLog() {
           return false;
        }
    }

    private String buildUrlForGet(String basUrl, HttpParams params) {
        if (null == params || params.isEmpty()) {
            return basUrl;
        }
        StringBuilder stringBuilder = new StringBuilder(basUrl);
        stringBuilder.append('?');
        for (Map.Entry<String, String> param : params.entrySet()) {
            stringBuilder.append(param.getKey());
            stringBuilder.append('=');
            stringBuilder.append(param.getValue());
            stringBuilder.append('&');
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    private void configBuilderForMethod(String url, Request.Builder builder, HttpParams params, HttpMethod method) {
        if (null == params || params.isEmpty()) {
            return;
        }
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (null == param.getValue()) {
                onError("null value url:" + url + " key:" + param.getKey());
                continue;
            }
            bodyBuilder.add(param.getKey(), param.getValue());
        }
        switch (method) {
            case kHttpPost:
                builder.post(bodyBuilder.build());
                break;
            case kHttpDelete:
                builder.delete(bodyBuilder.build());
                break;
            case kHttpPatch:
                builder.patch(bodyBuilder.build());
                break;
            case kHttpPut:
                builder.put(bodyBuilder.build());
                break;
            case kHttpGet:
                break;
        }
    }

    protected abstract void progressRequestBuilder(Request.Builder builder);

    protected abstract void progressRequestParams(String url, HttpParams params);

    protected abstract void progressResponse(NetResult result);

    protected String progressUrl(String url) {
        return url;
    }

    protected void onResponsePrint(String strMsg, String url) {

    }

    public abstract boolean isNetConnected();

    public boolean isIllegalUrl(String url) {
        return false;
    }

    protected void onError(String msg) {
    }

    public interface JMNetCallBack {
        void onNetFinished(NetResult result);
    }
}
