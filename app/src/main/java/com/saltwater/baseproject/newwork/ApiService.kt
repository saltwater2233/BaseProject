package com.saltwater.baseproject.newwork

import com.saltwater.baseproject.bean.main.FriendBean
import com.saltwater.common.network.BaseResponse
import retrofit2.http.GET

interface ApiService {

    @GET("friend/json")
    suspend fun friend(): BaseResponse<List<FriendBean>>

}