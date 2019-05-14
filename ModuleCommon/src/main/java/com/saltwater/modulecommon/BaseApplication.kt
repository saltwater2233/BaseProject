package com.saltwater.modulecommon

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
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


        if (BuildConfig.DEBUG) {
            LeakCanary.install(this)//初始化Leak内存泄露检测工具
        }

        //初始化日志打印工具
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })


        if (BuildConfig.DEBUG) {   // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()     // 打印日志
            ARouter.openDebug();  // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this) // 尽可能早，推荐在Application中初始化
    }

}