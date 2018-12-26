package com.saltwater.baseproject.module.update.presenter

import android.content.Context

import com.saltwater.baseproject.base.BasePresenter
import com.saltwater.baseproject.module.update.view.UpdateView
import com.saltwater.baseproject.network.RetrofitHelper

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/03/22
 * desc   :
 * version: 1.0
</pre> *
 */

class UpdatePresenter(context: Context) : BasePresenter<UpdateView>(context) {

    fun loadUpdate() {
        mCompositeDisposable.add(
                RetrofitHelper.getUpdateApi()
                        .getUpdateInfo()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ updateBean ->
                            getView().setUpdateInfo(updateBean.msg.toString())
                        }, { throwable ->
                            getView().showToast(throwable.message.toString())
                        })
        )
    }


}
