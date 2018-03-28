package com.saltwater.baseprojectmvp.network.api;



import com.saltwater.baseprojectmvp.network.entity.UpdateEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;

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

    @GET("api/4/version/android/2.3.0")
    Observable<UpdateEntity> getUpdate();
}
