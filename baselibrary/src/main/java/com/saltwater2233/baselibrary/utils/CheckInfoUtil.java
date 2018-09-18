package com.saltwater2233.baselibrary.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
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
public class CheckInfoUtil {
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


    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    public static boolean isPhoneNumber(String phoneNums) {
        if (isMatchLength(phoneNums, 11) && isMobile(phoneNums)) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    private static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    private static boolean isMobile(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);

    }
}
