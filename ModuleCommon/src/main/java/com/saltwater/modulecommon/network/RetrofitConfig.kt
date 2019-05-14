package com.saltwater.modulecommon.network

import android.annotation.SuppressLint
import android.content.Context
import com.saltwater.modulecommon.BaseApplication
import com.saltwater.modulecommon.BuildConfig
import com.saltwater.modulecommon.utils.SharedPreferencesUtil
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
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
            .addInterceptor(ReceivedCookiesInterceptor(BaseApplication.context))
            .addInterceptor(AddCookiesInterceptor(BaseApplication.context))
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
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(clazz)//实现接口,返回一个clazz接口的实例
    }


    //接受Cookie的拦截器
    class ReceivedCookiesInterceptor(private val context: Context) : Interceptor {
        @SuppressLint("CheckResult")
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse = chain.proceed(chain.request())
            //这里获取请求返回的cookie
            if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
                val cookieBuffer = StringBuffer()
                Observable.fromArray(*originalResponse.headers("Set-Cookie").toTypedArray())
                    .subscribe { cookie ->
                        cookieBuffer.append(cookie)
                            .append(";")
                    }
                SharedPreferencesUtil.put(context, COOKIE, cookieBuffer.toString())
            }
            return originalResponse
        }
    }

    //添加Cookie的拦截器
    class AddCookiesInterceptor(private val context: Context) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
            val cookieString = SharedPreferencesUtil.get(context, COOKIE, "")
            Observable.just(cookieString)
                .subscribe { cookie ->
                    //添加cookie
                    builder.addHeader("Cookie", cookie)
                }
            return chain.proceed(builder.build())
        }
    }

}
