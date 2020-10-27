package com.saltwater.common.network

import com.saltwater.common.BuildConfig
import com.saltwater.common.utils.SharedPreferencesUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/02/24
 * desc   : Retrofit辅助类
 * version: 2.0
</pre> *
 */

object RetrofitConfig {
    private const val COOKIE = "cookie"

    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
                .addInterceptor(ReceivedCookiesInterceptor())
                .addInterceptor(AddCookiesInterceptor())
                .connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }
        return@lazy builder.build()
    }

    /**
     * 根据传入的baseUrl，和api创建retrofit
     */
    fun <T> createApi(clazz: Class<T>, baseUrl: String): T {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        return retrofit.create(clazz)//实现接口,返回一个clazz接口的实例
    }


    //接受Cookie的拦截器
    class ReceivedCookiesInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse = chain.proceed(chain.request())
            //这里获取请求返回的cookie
            val cookies = originalResponse.headers("Set-Cookie")

            if (cookies.isNotEmpty()) {
                val cookieBuffer = StringBuffer()
                for (cookie in cookies) {
                    cookieBuffer.append(cookie)
                    cookieBuffer.append(";")
                }
                SharedPreferencesUtil.put(COOKIE, cookieBuffer.toString())
            }
            return originalResponse
        }
    }

    //添加Cookie的拦截器
    class AddCookiesInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
            val cookieString = SharedPreferencesUtil[COOKIE, ""]
            val cookies = cookieString?.split(";")
            cookies?.let {
                for (cookie in it) {
                    builder.addHeader("Cookie", cookie)
                }
            }
            return chain.proceed(builder.build())

        }
    }

}
