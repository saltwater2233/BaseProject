package com.saltwater.baseproject

import com.saltwater.common.BaseApplication
import com.saltwater.common.utils.LogUtil

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2019/05/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class MainApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        LogUtil.d("onCreate")
    }
}