package com.saltwater.baseprojectmvp.common.utils;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/03/26
 *     desc   : 让RxJava&RxAndroid的Schedulers.io()和AndroidSchedulers.mainThread()
 *              转换成Schedulers.immediate()，从而让Obserable从异步变同步。
 *     version: 1.0
 * </pre>
 */

public class RxUnitTestUtil {

    public static void asyncToSync() {
        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
    }
}
