package com.saltwater.baseproject.network.api


import com.saltwater.baseproject.network.entity.BaseResponse
import com.saltwater.baseproject.module.update.entity.UpdateBean

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/02/24
 * desc   :
 * version: 1.0
</pre> *
 */

interface UpdateApi {

    @GET("api/4/version/android/2.3.0")
    fun getUpdateInfo(): Observable<UpdateBean>

    @GET("Login/getVerify")
    fun getVerify(@Query("login_name") phoneNumber: String, @Query("isFindPwd") type: Int): Observable<BaseResponse<Any>>

    /*上传文件*/
    @Multipart
    @POST("uploadImg")
    fun upload(@Part img: MultipartBody.Part, @PartMap args: Map<String, RequestBody>): Observable<ResponseBody>

}
