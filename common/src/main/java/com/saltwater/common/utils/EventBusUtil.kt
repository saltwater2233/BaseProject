package com.saltwater.common.utils

import com.saltwater.common.base.EventMessage
import org.greenrobot.eventbus.EventBus

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2019/12/24
 * desc   : EventBus工具类
 * version: 1.0
</pre> *
 */
object EventBusUtil {
    /**
     * 注册 EventBus
     *
     * @param subscriber
     */
    fun register(subscriber: Any?) {
        val eventBus = EventBus.getDefault()
        if (!eventBus.isRegistered(subscriber)) {
            eventBus.register(subscriber)
        }
    }

    /**
     * 解除注册 EventBus
     *
     * @param subscriber
     */
    fun unregister(subscriber: Any?) {
        val eventBus = EventBus.getDefault()
        if (eventBus.isRegistered(subscriber)) {
            eventBus.unregister(subscriber)
        }
    }

    /**
     * 发送事件消息
     *
     * @param event
     */
    fun post(code: Int, data: Any?) {
        EventBus.getDefault().post(EventMessage(code, data))
    }

    /**
     * 发送粘性事件消息
     *
     * @param event
     */
    fun postSticky(code: Int, data: Any?) {
        EventBus.getDefault().postSticky(EventMessage(code, data))
    }
}