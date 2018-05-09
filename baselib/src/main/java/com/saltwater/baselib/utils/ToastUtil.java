package com.saltwater.baselib.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/02/26
 *     desc   : Toast统一管理类
 *     version: 1.0
 * </pre>
 */

public class ToastUtil {
    public static boolean isShow = true;

    private ToastUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (isShow) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (isShow) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow) {
            Toast.makeText(context, message, duration).show();
        }

    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (isShow) {
            Toast.makeText(context, message, duration).show();
        }

    }

    /**
     * 显示SnackBar
     *
     * @param view
     * @param listener
     */
    public static void showSnackBarShort(View view, CharSequence text, View.OnClickListener listener) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).setAction("撤销", listener).show();
    }

    public static void showSnackBarLong(View view, CharSequence text, View.OnClickListener listener) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).setAction("撤销", listener).show();
    }
}
