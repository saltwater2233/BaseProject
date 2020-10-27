package com.saltwater.baseproject.ui.main

import androidx.lifecycle.MutableLiveData
import com.saltwater.baseproject.bean.main.FriendBean
import com.saltwater.baseproject.newwork.RetrofitHelper
import com.saltwater.common.base.BaseViewModel
import com.saltwater.common.network.ThrowableDispose
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *  日期 : 2020/10/27
 *  描述 :
 *  @author : hg
 */
class MainViewModel :BaseViewModel() {

    var friend =MutableLiveData<List<FriendBean>>()

    fun getData(){
        launch({
            val response = withContext(Dispatchers.IO) {
                RetrofitHelper.api.friend()
            }
            if (response.getStatue()==0){
                friend.value = response.getData()
            }

        },{
            ThrowableDispose.requestFailed(it)
        })
    }
}