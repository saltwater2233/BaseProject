package com.saltwater.modulecommon.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/02/26
 * desc   : app信息相关
 * version: 1.0
</pre> *
 */

object AppInfoUtil {

    /**
     * 获取应用程序名称
     */
    fun getAppName(context: Context): String? {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(
                context.packageName, 0
            )
            val labelRes = packageInfo.applicationInfo.labelRes
            return context.resources.getString(labelRes)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 获取应用程序版本名称信息
     */
    fun getVersionName(context: Context): String? {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 设备硬件制造商+设备版本
     */
    val deviceInfo: String
        get() = Build.MANUFACTURER + " " + Build.MODEL

    /**
     * OS版本号
     */
    val osVersion: String
        get() = Build.VERSION.RELEASE
}
