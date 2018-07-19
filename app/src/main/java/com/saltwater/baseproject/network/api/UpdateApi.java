package com.saltwater.baseproject.network.api;



import com.saltwater.baseproject.network.entity.ResponseEntity;
import com.saltwater.baseproject.network.entity.UpdateEntity;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/02/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface UpdateApi {

    @GET("Login/getVerify")
    Observable<ResponseEntity<Object>> getVerify(@Query("login_name") String phoneNumber, @Query("isFindPwd") int type);

    @GET("api/4/version/android/2.3.0")
    Observable<UpdateEntity> getUpdate();


    @GET("api/4/version/android/2.3.0")
    Observable<ResponseEntity<UpdateEntity>> getUpdate2();

    /*上传文件*/
    @Multipart
    @POST("uploadImg")
    Observable<ResponseBody> upload(@Part MultipartBody.Part img, @PartMap Map<String, RequestBody> args);

}
