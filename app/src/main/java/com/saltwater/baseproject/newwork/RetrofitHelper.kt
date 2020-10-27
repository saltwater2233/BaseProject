package com.saltwater.baseproject.newwork

import com.saltwater.common.network.RetrofitConfig.createApi

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/02/24
 * desc   : Retrofit辅助类
 * version: 1.0
</pre> *
 */
object RetrofitHelper {
    var BASE_URL = "https://www.wanandroid.com/" //测试地址



    val api: ApiService
        get() = createApi(ApiService::class.java, BASE_URL)
}