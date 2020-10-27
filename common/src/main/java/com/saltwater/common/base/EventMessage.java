package com.saltwater.common.base;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2019/12/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class EventMessage<T> {
    private int code;
    private T data;

    public EventMessage(int code) {
        this.code = code;
    }

    public EventMessage(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EventMessage{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }

}
