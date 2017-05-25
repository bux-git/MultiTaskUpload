package com.dqr.www.multitaskupload.base;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class UploadBaseCallBack<T> implements Callback {
    private static final String TAG = "UploadBaseCallBack";


    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String responseStr = response.body().source().readString(Charset.forName("UTF-8"));
        Log.e(TAG, "onResponse: " + response + "      result:" + responseStr);
        int code = response.code();
        String msg = "";
        if (code == 200) {
            BaseModel<T> baseModel;
            try {
                baseModel = (BaseModel<T>) JSON.parseObject(responseStr, new TypeReference<BaseModel<T>>() {
                });
            } catch (Exception e) {
                baseModel = new BaseModel<T>();
                baseModel.setCode("000021");
            }

            T t = baseModel.getResult();
            //这里只实现了成功和失败的回调，根据接口返回的状态信息实现相应的回调
            switch (baseModel.getCode()) {
                case "000000"://成功

                    onSuccess(t);
                    break;
                default:
                    onFail(baseModel.getMessage());
                    break;
            }

            onAfter();
        } else {
            onFail(String.valueOf(code));
        }
        onAfter();
        response.body().close();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.d(TAG, "onFailure: " + e.getMessage());
        onFail(e.getMessage());
        onAfter();
    }

    /**
     * 请求成功的回调
     */
    protected abstract void onSuccess(T result);

    /**
     * 请求失败的回调
     */
    protected abstract void onFail(String msg);

    /**
     * 请求完的回调，可以在里面停止刷新控件，可以不实现
     */
    protected void onAfter() {
    }

    /**
     * 没有数据的回调，可以不实现
     */
    protected void onNoData(String msg) {

    }


}