package com.saltwater.baseproject.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/07/11
 *     desc   : 简单的屏幕适配方案
 *     version: 1.0
 * </pre>
 */
public class ScreenAdaptationUtil {
    private static float sNoncompatDensity;
    private static float sNoncompatScaledDensity;
    private static int sNoncompatDensityDpi;

    private static final int sWidth = 375;//375 设计图的宽度dp 根据宽度适配
    private static final int sHeight = 667;//667 设计图的高度dp 根据高度适配


    /**
     * 默认按照宽度适配
     *
     * @param activity
     * @param application
     * @param isWidth     是否按照宽度适配，false按照高度适配
     */
    public static void setCustomDensity(@NonNull Activity activity, @NonNull final Application application, boolean isWidth) {
        final DisplayMetrics appdisplayMetrics = application.getResources().getDisplayMetrics();
        if (sNoncompatDensity == 0) {
            sNoncompatDensity = appdisplayMetrics.density;
            sNoncompatScaledDensity = appdisplayMetrics.scaledDensity;
            sNoncompatDensityDpi = appdisplayMetrics.densityDpi;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration configuration) {
                    if (configuration != null && configuration.fontScale > 0) {
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        final float targetDesity;
        if (isWidth) {
            targetDesity = (float) appdisplayMetrics.widthPixels / sWidth;
        } else {
            targetDesity = (float) appdisplayMetrics.heightPixels / sHeight;
        }
        final float targetScaleDesity = targetDesity * (sNoncompatScaledDensity / sNoncompatDensity);
        final int targetDesityDpi = (int) (160 * targetDesity);

        appdisplayMetrics.density = targetDesity;
        appdisplayMetrics.scaledDensity = targetScaleDesity;
        appdisplayMetrics.densityDpi = targetDesityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDesity;
        activityDisplayMetrics.scaledDensity = targetScaleDesity;
        activityDisplayMetrics.densityDpi = targetDesityDpi;
    }

    public static void resetDensity(@NonNull Activity activity, @NonNull final Application application) {
        DisplayMetrics appdisplayMetrics = application.getResources().getDisplayMetrics();
        appdisplayMetrics.density = sNoncompatDensity;
        appdisplayMetrics.scaledDensity = sNoncompatScaledDensity;
        appdisplayMetrics.densityDpi = sNoncompatDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = sNoncompatDensity;
        activityDisplayMetrics.scaledDensity = sNoncompatScaledDensity;
        activityDisplayMetrics.densityDpi = sNoncompatDensityDpi;
    }
}
