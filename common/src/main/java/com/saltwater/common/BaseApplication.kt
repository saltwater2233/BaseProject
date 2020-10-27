package com.saltwater.common

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.saltwater.common.utils.ScreenAdaptationUtil
import com.squareup.leakcanary.LeakCanary


/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2019/05/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
open class BaseApplication : Application() {

    companion object {
        lateinit var context: BaseApplication
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        ScreenAdaptationUtil.initAppDensity(this)

        if (BuildConfig.DEBUG) {
            LeakCanary.install(this)//初始化Leak内存泄露检测工具
        }

        //初始化日志打印工具
        Logger.addLogAdapter(AndroidLogAdapter())


    }

}