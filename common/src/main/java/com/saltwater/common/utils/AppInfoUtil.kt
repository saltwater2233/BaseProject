package com.saltwater.common.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils


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
    fun getVersionName(context: Context): String {
        val packageManager = context.packageManager
        val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionName
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


    /**
     * 版本号比较
     *
     * @param version1
     * @param version2
     * @return 0代表相等，1代表version1大于version2，-1代表version1小于version2
     */
    fun compareVersion(version1: String, version2: String): Int {
        if (TextUtils.isEmpty(version1) || version1 == version2) {
            return 0
        }
        val version1Array = version1.split(".")
        val version2Array = version2.split(".")
        var index = 0
        // 获取最小长度值
        val minLen = Math.min(version1Array.size, version2Array.size)
        var diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])
        // 循环判断每位的大小
        while (index < minLen && diff == 0) {
            diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])
            index++
        }

        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (i in index until version1Array.size) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1
                }
            }

            for (i in index until version2Array.size) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1
                }
            }
            return 0
        } else {
            return if (diff > 0) 1 else -1
        }
    }
}
