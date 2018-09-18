package com.saltwater.baseproject;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;

public class MyApp extends Application {
    private static MyApp mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        String name = "sas";
        init();
    }

    private void init() {
        //初始化Leak内存泄露检测工具
        LeakCanary.install(this);

    }

    public static MyApp getInstance() {
        return mContext;
    }

}