package com.saltwater.modulecommon.utils

import com.orhanobut.logger.Logger


/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2019/05/08
 *     desc   : 日志打印工具类
 *     version: 1.0
 * </pre>
 */
object LogUtil {

    //打印对象,支持Collections
    fun d(any: Any) {
        Logger.d(any)
    }

    fun d(message: String) {
        Logger.d(message)
    }

    fun e(message: String) {
        Logger.e(message)
    }

    fun w(message: String) {
        Logger.w(message)
    }

    fun v(message: String) {
        Logger.v(message)
    }

    fun i(message: String) {
        Logger.i(message)
    }

    //用于记录异常情况,打印报错
    fun wtf(message: String) {
        Logger.wtf(message)
    }

    //打印json
    fun json(json: String) {
        Logger.json(json)
    }

    //打印xml
    fun xml(xml: String) {
        Logger.xml(xml)
    }


}