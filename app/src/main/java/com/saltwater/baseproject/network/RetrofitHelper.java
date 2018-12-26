package com.saltwater.baseproject.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.orhanobut.logger.Logger;
import com.saltwater.baseproject.MyApp;
import com.saltwater.baseproject.network.api.UpdateApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/02/24
 *     desc   : Retrofit辅助类
 *     version: 1.0
 * </pre>
 */

public class RetrofitHelper {
    private final static String FILE_NAME = "userinfo";
    private final static String COOKIE = "cookie";

    public static UpdateApi getUpdateApi() {
        return createApi(UpdateApi.class, ApiConstants.INSTANCE.getBASE_URL());
    }


    /**
     * 由于retrofit底层的实现是通过okhttp实现的，所以可以通过okHttp来设置一些连接参数
     * 初始化OKHttpClient,设置缓存,设置超时时间,构造头部
     */
    private static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLogger());
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                //.addInterceptor(new HeaderInterceptor())
                .addInterceptor(new ReceivedCookiesInterceptor())
                .addInterceptor(new AddCookiesInterceptor())
                .addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        return httpClient;
    }


    public static class HttpLogger implements HttpLoggingInterceptor.Logger {
        @Override
        public void log(String message) {
            Logger.d("HttpLogInfo", message);
        }
    }

    /**
     * 添加Header
     */
    private static class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request;
            request = chain.request()
                    .newBuilder()
                    .header("USERNAME", "username")
                    .build();

            return chain.proceed(request);
        }
    }

    //保存获取到的cookie
    private static class ReceivedCookiesInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            Response originalResponse = chain.proceed(chain.request());
            //这里获取请求返回的cookie
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                final StringBuffer cookieBuffer = new StringBuffer();
                Observable.fromArray(originalResponse.headers("Set-Cookie")
                        .toArray(new String[originalResponse.headers("Set-Cookie").size()]))
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String cookie) throws Exception {
                                cookieBuffer.append(cookie).append(";");
                            }
                        });

                SharedPreferences sp = MyApp.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(COOKIE, cookieBuffer.toString());
                editor.commit();//提交修改
            }
            return originalResponse;
        }
    }

    //给请求加上cookie
    private static class AddCookiesInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            final Request.Builder builder = chain.request().newBuilder();
            SharedPreferences sp = MyApp.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            String cookie = sp.getString(COOKIE, "");
            Observable.just(cookie)
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String cookie) throws Exception {
                            //添加cookie
                            builder.addHeader("Cookie", cookie);
                        }
                    });
            return chain.proceed(builder.build());
        }
    }


    /**
     * 根据传入的baseUrl，和api创建retrofit
     */
    private static <T> T createApi(Class<T> clazz, String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(clazz);//实现接口,返回一个clazz接口的实例
    }

}
