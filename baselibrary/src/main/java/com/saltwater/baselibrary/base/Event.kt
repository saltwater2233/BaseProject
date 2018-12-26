package com.saltwater.baseproject.base

/**
 *
 * EventBus订阅和发布的事件
 */
class Event<T> {
    var code: Int = 0
    var data: T? = null

    constructor(code: Int) {
        this.code = code
    }

    constructor(code: Int, data: T) {
        this.code = code
        this.data = data
    }
}
