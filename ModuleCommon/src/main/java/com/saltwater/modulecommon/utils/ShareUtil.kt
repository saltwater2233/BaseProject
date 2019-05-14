package com.saltwater.modulecommon.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2019/05/07
 *     desc   : 分享工具类
 *     version: 1.0
 * </pre>
 */
object ShareUtil {

    
    /**
     * 启动隐式Activity,会先判断是否存在响应目标的Activity
     *
     * @return
     */
    fun startTargetActivity(context: Context, intent: Intent) {
        val packageManager = context.packageManager
        val componentName = intent.resolveActivity(packageManager)
        if (componentName == null) {
            //如果没有，那么就去应用市场去找找看
            val marketUri = Uri.parse("market://search?q=需要打开的应用名")//打开应用市场，搜索应用
            val marketIntent = Intent(Intent.ACTION_VIEW).setData(marketUri)
            if (marketIntent.resolveActivity(packageManager) != null) {
                context.startActivity(marketIntent)
            } else {
                Log.d("Error", "Market client not available.")
            }
        } else {
            context.startActivity(intent)
        }
    }

}