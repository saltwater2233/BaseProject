package com.saltwater.common.utils

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2019/07/04
 * desc   : 今日头条屏幕适配方案，参考：https://juejin.im/post/5b7a29736fb9a019d53e7ee2
 * version: 1.0
</pre> *
 */
object ScreenAdaptationUtil {

    private val WIDTH = 1
    private val HEIGHT = 2
    private val DEFAULT_WIDTH = 375f //默认宽度
    private val DEFAULT_HEIGHT = 667f //默认高度
    private var appDensity: Float = 0f

    /**
     * 字体的缩放因子，正常情况下和density相等，但是调节系统字体大小后会改变这个值
     */
    private var appScaledDensity: Float = 0.toFloat()

    /**
     * 状态栏高度
     */
    private var barHeight: Int = 0
    private lateinit var appDisplayMetrics: DisplayMetrics
    /**
     * 屏幕密度缩放系数
     *
     * @return 屏幕密度缩放系数
     */
    var densityScale = 1.0f
        private set

    /**
     * application 层调用，存储默认屏幕密度
     *
     * @param application application
     */
    fun initAppDensity(application: Application) {
        //获取application的DisplayMetrics
        appDisplayMetrics = application.resources.displayMetrics
        //获取状态栏高度
        barHeight = getStatusBarHeight(application)
        if (appDensity == 0f) {
            //初始化的时候赋值
            appDensity = appDisplayMetrics.density
            appScaledDensity = appDisplayMetrics.scaledDensity

            //添加字体变化的监听
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(newConfig: Configuration) {
                    //字体改变后,将appScaledDensity重新赋值
                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaledDensity = application.resources.displayMetrics.scaledDensity
                    }
                }

                override fun onLowMemory() {}
            })
        }
    }

    /**
     * 此方法在BaseActivity中做初始化(如果不封装BaseActivity的话,直接用下面那个方法就好了)
     *
     * @param activity activity
     */
    fun setDefaultScreenDensity(activity: Activity) {
        setAppScreenDensity(activity, WIDTH)
    }


    /**
     * 比如页面是上下滑动的，只需要保证在所有设备中宽的维度上显示一致即可，
     * 再比如一个不支持上下滑动的页面，那么需要保证在高这个维度上都显示一致
     *
     * @param activity    activity
     * @param orientation WIDTH HEIGHT
     */
    fun setOrientation(activity: Activity, orientation: Int) {
        setAppScreenDensity(activity, orientation)
    }

    /**
     * 重设屏幕密度
     *
     * @param activity    activity
     * @param orientation WIDTH 宽，HEIGHT 高
     */
    private fun setAppScreenDensity(activity: Activity, orientation: Int) {

        val targetDensity: Float

        if (orientation == HEIGHT) {
            targetDensity = (appDisplayMetrics.heightPixels - barHeight) / DEFAULT_HEIGHT
        } else {
            targetDensity = appDisplayMetrics.widthPixels / DEFAULT_WIDTH
        }

        val targetScaledDensity = targetDensity * (appScaledDensity / appDensity)
        val targetDensityDpi = (160 * targetDensity).toInt()
        // 最后在这里将修改过后的值赋给系统参数,只修改Activity的density值
        val activityDisplayMetrics = activity.resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.scaledDensity = targetScaledDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi

        densityScale = appDensity / targetDensity
        setBitmapDefaultDensity(activityDisplayMetrics.densityDpi)
    }


    /**
     * 重置屏幕密度
     *
     * @param activity activity
     */
    fun resetScreenDensity(activity: Activity) {
        val activityDisplayMetrics = activity.resources.displayMetrics
        activityDisplayMetrics.density = appDensity
        activityDisplayMetrics.scaledDensity = appScaledDensity
        activityDisplayMetrics.densityDpi = (appDensity * 160).toInt()
        densityScale = 1.0f
        setBitmapDefaultDensity(activityDisplayMetrics.densityDpi)
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    private fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 设置 Bitmap 的默认屏幕密度
     * 由于 Bitmap 的屏幕密度是读取配置的，导致修改未被启用
     * 所有，放射方式强行修改
     *
     * @param defaultDensity 屏幕密度
     */
    private fun setBitmapDefaultDensity(defaultDensity: Int) {
        //获取单个变量的值
        val clazz: Class<*>
        try {
            clazz = Class.forName("android.graphics.Bitmap")
            val field = clazz.getDeclaredField("sDefaultDensity")
            field.isAccessible = true
            field.set(null, defaultDensity)
            field.isAccessible = false
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
