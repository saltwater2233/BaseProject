package com.saltwater.modulecommon.utils

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/07/11
 * desc   : 简单的屏幕适配方案,通过把density设置为统一进行适配
 * version: 1.0
</pre> *
 */
object ScreenAdaptationUtil {
    private var density = 0f
    private var scaledDensity = 0f
    private var densityDpi = 0

    private const val width = 375//375 设计图的宽度dp 根据宽度适配
    private const val height = 667//667 设计图的高度dp 根据高度适配


    /**
     * 默认按照宽度适配
     *
     * @param activity
     * @param application
     * @param isWidth     是否按照宽度适配，false按照高度适配
     */
    fun setCustomDensity(activity: Activity, application: Application, isWidth: Boolean) {
        val airplayMetrics = application.resources.displayMetrics
        if (density == 0f) {
            density = airplayMetrics.density
            scaledDensity = airplayMetrics.scaledDensity
            densityDpi = airplayMetrics.densityDpi
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(configuration: Configuration?) {
                    if (configuration != null && configuration.fontScale > 0) {
                        scaledDensity = application.resources.displayMetrics.scaledDensity
                    }
                }

                override fun onLowMemory() {

                }
            })
        }
        val targetDeity: Float
        if (isWidth) {
            targetDeity = airplayMetrics.widthPixels.toFloat() / width
        } else {
            targetDeity = airplayMetrics.heightPixels.toFloat() / height
        }
        val targetScaleDeity = targetDeity * (scaledDensity / density)
        val targetDensityDpi = (160 * targetDeity).toInt()

        airplayMetrics.density = targetDeity
        airplayMetrics.scaledDensity = targetScaleDeity
        airplayMetrics.densityDpi = targetDensityDpi

        val activityDisplayMetrics = activity.resources.displayMetrics
        activityDisplayMetrics.density = targetDeity
        activityDisplayMetrics.scaledDensity = targetScaleDeity
        activityDisplayMetrics.densityDpi = targetDensityDpi
    }

    //重设DisplayMetrics
    fun resetDensity(activity: Activity, application: Application) {
        val displayMetrics = application.resources.displayMetrics
        displayMetrics.density = density
        displayMetrics.scaledDensity = scaledDensity
        displayMetrics.densityDpi = densityDpi

        val activityDisplayMetrics = activity.resources.displayMetrics
        activityDisplayMetrics.density = density
        activityDisplayMetrics.scaledDensity = scaledDensity
        activityDisplayMetrics.densityDpi = densityDpi
    }
}
