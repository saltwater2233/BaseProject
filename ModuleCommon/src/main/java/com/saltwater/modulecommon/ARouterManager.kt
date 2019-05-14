package com.saltwater.modulecommon

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import java.util.*

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2019/05/09
 * desc   : 阿里路由管理
 * version: 1.0
</pre> *
 */
object ARouterManager {
    /************** 路由地址 ****************/
    const val AModuleListActivity = "/amodule/ListActivity"
    const val BModuleDetailActivity = "/bmodule/DetailActivity"


    /************** 跳转方法 ****************/

    fun navigation(path: String) {
        ARouter.getInstance()
            .build(path)
            .navigation()
    }


    fun navigation(path: String, key: String, value: String) {
        ARouter.getInstance()
            .build(path)
            .withString(key, value)
            .navigation()
    }

    fun navigation(path: String, key: String, value: Object) {
        ARouter.getInstance()
            .build(path)
            .withObject(key, value)
            .navigation()
    }

    fun navigation(path: String, bundle: Bundle) {
        ARouter.getInstance()
            .build(path)
            .with(bundle)
            .navigation()
    }
}
