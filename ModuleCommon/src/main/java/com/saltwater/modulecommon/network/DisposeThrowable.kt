package com.saltwater.modulecommon.network

import com.orhanobut.logger.Logger
import com.saltwater.modulecommon.utils.ToastUtil
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException


/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2019/01/09
 * desc   : 处理网络请求错误
 * version: 1.0
</pre> *
 */
object DisposeThrowable {
    private const val UNAUTHORIZED = 401
    private const val FORBIDDEN = 403
    private const val NOT_FOUND = 404
    private const val REQUEST_TIMEOUT = 408
    private const val INTERNAL_SERVER_ERROR = 500
    private const val BAD_GATEWAY = 502
    private const val SERVICE_UNAVAILABLE = 503
    private const val GATEWAY_TIMEOUT = 504


    fun requestFailed(throwable: Throwable) {
        val responseThrowable = retrofitException(throwable)
        // 此处可以通过判断错误代码来实现根据不同的错误代码做出相应的反应
        when (responseThrowable.code) {
            -1 -> {
                // 跳转到登陆页面
                //mContext.startActivity(new Intent(mContext, LoginActivity.class));
                // 结束除LoginActivity之外的所有Activity
                //AppManager.finishAllActivity(LoginActivity.class);
            }
            else -> {
                Logger.d(DisposeThrowable::class.java.simpleName, throwable)
                ToastUtil.showError(responseThrowable.messages)
            }
        }
    }


    private fun retrofitException(e: Throwable): ResponseThrowable {
        val responseThrowable = ResponseThrowable(e, "网络错误", 0)
        when (e) {
            is HttpException -> {
                when (e.code()) {
                    UNAUTHORIZED -> responseThrowable.messages = "error401:未经授权访问"
                    FORBIDDEN -> responseThrowable.messages = "error403:资源不可用"
                    NOT_FOUND -> responseThrowable.messages = "error404:无法找到指定位置的资源"
                    REQUEST_TIMEOUT -> responseThrowable.messages = "error408:请求超时"
                    INTERNAL_SERVER_ERROR -> responseThrowable.messages = "error500:服务器发生错误"
                    BAD_GATEWAY -> responseThrowable.messages = "error502:服务器返回应答非法"
                    SERVICE_UNAVAILABLE -> responseThrowable.messages = "error503:网络错误"
                    GATEWAY_TIMEOUT -> responseThrowable.messages = "error504:返回超时"
                    else -> responseThrowable.messages = "error0:网络错误"
                }
                return responseThrowable
            }
            is SocketTimeoutException -> {
                responseThrowable.messages = "连接超时"
                return responseThrowable
            }
            is ConnectException -> {
                responseThrowable.messages = "无法连接到服务器，请检查网络连接是否正常"
                return responseThrowable
            }
            is IOException -> {
                responseThrowable.messages = "读写错误: " + e.message.toString()
                return responseThrowable
            }
            else -> {
                responseThrowable.messages = e.message.toString()
                return responseThrowable
            }
        }
    }


    class ResponseThrowable(throwable: Throwable, var messages: String, var code: Int) : Exception(throwable)

}

