package com.saltwater.common.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context

import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent


/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/07/23
 * desc   :
 * version: 1.0
</pre> *
 */
class ActivityStackManager {

    private val activityStack: ArrayList<Activity> = ArrayList()

    companion object {
        val instance: ActivityStackManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ActivityStackManager()
        }
    }

    /**
     * 压栈
     *
     * @param activity
     */
    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    /**
     * 移除
     *
     * @param activity
     */
    fun removeActivity(activity: Activity) {
        activityStack.remove(activity);
    }

    /**
     * 是否存在栈
     *
     * @param activity
     * @return
     */
    operator fun contains(activity: Activity): Boolean {
        return activityStack.contains(activity)
    }

    //判断某一个类是否存在任务栈里面
     fun isExitActivity(context: Context, cls: Class<*>): Boolean {
        val intent = Intent(context, cls)
        val cmpName = intent.resolveActivity(context.packageManager)
        var flag = false
        if (cmpName != null) { // 说明系统中存在这个activity
            val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager?
            val taskInfoList = am!!.getRunningTasks(10)
            for (taskInfo in taskInfoList) {
                if (taskInfo.baseActivity == cmpName) { // 说明它已经启动了
                    flag = true
                    break  //跳出循环，优化效率
                }
            }
        }
        return flag
    }


    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        for (activity in activityStack) {
            activity.finish()
        }
        activityStack.clear()
    }
}

