package com.saltwater.common.network

import com.google.gson.annotations.SerializedName

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/06/26
 * desc   : 返回实体类,用来对不同返回结果先处理
 * version: 1.0
</pre> *
 */
class BaseResponse<T> {
    @SerializedName(value = "data")
    private var mData: T? = null//返回数据

    @SerializedName(value = "status")
    private var mStatue: Int = 0//返回状态

    @SerializedName(value = "info", alternate = arrayOf("code", "xxx", "yyy", "zzz"))
    private var mInfo: String? = null//返回信息

    fun getData(): T? {
        return mData
    }

    fun setData(data: T) {
        mData = data
    }

    fun getStatue(): Int {
        return mStatue
    }

    fun setStatue(statue: Int) {
        mStatue = statue
    }

    fun getInfo(): String? {
        return mInfo
    }

    fun setInfo(info: String) {
        mInfo = info
    }
}
