package com;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/03/26
 *     desc   : 在单元测试中使用RxJava2
 *     version: 1.0
 * </pre>
 */

public class TrampolineSchedulerRule implements TestRule {
    @Override
    public Statement apply(Statement base, Description description) {
        return new MyStatement(base);
    }

    public class MyStatement extends Statement {
        private final Statement base;

        @Override
        public void evaluate() throws Throwable {
            try {
                RxJavaPlugins.setComputationSchedulerHandler(scheduler -> Schedulers.trampoline());
                RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
                RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
                RxJavaPlugins.setSingleSchedulerHandler(scheduler -> Schedulers.trampoline());
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
                base.evaluate();
            } finally {
                RxJavaPlugins.reset();
                RxAndroidPlugins.reset();
            }
        }

        public MyStatement(Statement base) {
            this.base = base;
        }
    }
}
