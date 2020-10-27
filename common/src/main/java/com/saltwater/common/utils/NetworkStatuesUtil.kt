package com.saltwater.common.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/02/26
 * desc   : 网络相关的工具类
 * version: 1.0
</pre> *
 */

object NetworkStatuesUtil {

    /**MOBILE网络是否可用
     * @param context
     * @return
     */
    fun isMobileConnected(context: Context): Boolean {
        if (context != null) {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mMobileNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable
            }
        }
        return false
    }

    /**获取当前网络连接的类型信息
     * @param context
     * @return
     */
    fun getConnectedType(context: Context?): Int {
        if (context != null) {
            val cm = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = cm.activeNetworkInfo
            if (mNetworkInfo != null && mNetworkInfo.isAvailable) {
                return mNetworkInfo.type
            }
        }
        return -1
    }

    /**
     * 判断网络是否连接
     * @param context
     * @return
     */
    fun isConnected(context: Context): Boolean {

        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (null != connectivity) {

            val info = connectivity.activeNetworkInfo
            if (null != info && info.isConnected) {
                if (info.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 判断是否是wifi连接
     */
    fun isWifi(context: Context): Boolean {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return false

        return cm.activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI

    }

    /**
     * 打开网络设置界面
     */
    fun openSetting(activity: Activity) {
        val intent = Intent("/")
        val cm = ComponentName(
            "com.android.settings",
            "com.android.settings.WirelessSettings"
        )
        intent.component = cm
        intent.action = "android.intent.action.VIEW"
        activity.startActivityForResult(intent, 0)
    }


}
