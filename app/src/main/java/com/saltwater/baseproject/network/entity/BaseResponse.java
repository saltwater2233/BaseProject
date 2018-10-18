package com.saltwater.baseproject.network.entity;

import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/06/26
 *     desc   : 返回实体类,用来对不同返回结果先处理
 *     version: 1.0
 * </pre>
 */
public class BaseResponse<T> {
    private T mData;//返回数据
    private int mStatue;//返回状态

    @SerializedName(value = "info", alternate = {"code", "xxx", "yyy", "zzz"})
    private String mInfo;//返回信息

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        mData = data;
    }

    public int getStatue() {
        return mStatue;
    }

    public void setStatue(int statue) {
        mStatue = statue;
    }

    public String getInfo() {
        return mInfo;
    }

    public void setInfo(String info) {
        mInfo = info;
    }
}
