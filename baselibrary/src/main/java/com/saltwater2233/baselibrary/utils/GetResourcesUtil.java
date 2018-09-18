package com.saltwater2233.baselibrary.utils;

import android.content.Context;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/07/11
 *     desc   : 通过id获取资源
 *     version: 1.0
 * </pre>
 */
public class GetResourcesUtil {
    private GetResourcesUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    public static String getStringById(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static int getColorById(Context context, int id) {
        return context.getResources().getColor(id);
    }

    /**
     * @param context
     * @param id
     * @return
     */
    public static String getStringReplace(Context context, int id, String replaceString) {
        String format = context.getResources().getString(id);
        return String.format(format, replaceString);
    }

    public static String getStringReplace(Context context, int id, int replaceInt) {
        String format = context.getResources().getString(id);
        return String.format(format, replaceInt);
    }
}
