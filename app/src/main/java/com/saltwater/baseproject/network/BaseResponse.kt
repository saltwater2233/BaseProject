package com.saltwater.baseproject.network.entity

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
    var data: T? = null//返回数据
    var statue: Int = 0//返回状态

    @SerializedName(value = "info", alternate = arrayOf("code", "xxx", "yyy", "zzz"))
    var info: String? = null//返回信息
}
