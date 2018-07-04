package com.saltwater2233.baselibrary.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/07/04
 *     desc   : 各种检查方法
 *     version: 1.0
 * </pre>
 */
public class CheckUtil {

    /**启动隐式Activity,会先判断是否存在响应目标的Activity
     * @return
     */
    public static void startTargetActivity(Context context, Intent intent){
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = intent.resolveActivity(packageManager);
        if (componentName==null){
            //如果没有，那么就去应用市场去找找看
            Uri marketUri = Uri.parse("market://search?q=需要打开的应用名");//打开应用市场，搜索应用
            Intent marketIntent = new Intent(Intent.ACTION_VIEW).setData(marketUri);
            if (marketIntent.resolveActivity(packageManager) != null) {
                context.startActivity(marketIntent);
            } else {
                Log.d("Error", "Market client not available.");
            }
        }else {
            context.startActivity(intent);
        }
    }
}
