package com.saltwater.baseproject;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

public class MyApp extends Application {
    private static MyApp mContext;
    public final static Boolean sIsDebug = true;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        init();
    }

    private void init() {
        //初始化Leak内存泄露检测工具
        if (sIsDebug) {
            LeakCanary.install(this);
        }

        //初始化日志打印工具
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return sIsDebug;
            }
        });
    }

    public static MyApp getInstance() {
        return mContext;
    }

}