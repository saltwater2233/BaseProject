package com.saltwater.baseproject.module.update.presenter;

import android.content.Context;

import com.saltwater.baseproject.base.BasePresenter;
import com.saltwater.baseproject.module.update.contract.UpdateContract;
import com.saltwater.baseproject.module.update.entity.UpdateBean;
import com.saltwater.baseproject.network.RetrofitHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/03/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class UpdatePresenter extends UpdateContract.Presenter {


    public UpdatePresenter(Context context) {
        super(context);
    }

    @Override
    public void loadUpdate() {
        mCompositeDisposable.add(RetrofitHelper.getUpdateApi()
                .getUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateBean>() {
                    @Override
                    public void accept(UpdateBean updateBean) throws Exception {
                        getView().setUpdateInfo(updateBean.getMsg());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().showToast(throwable.getMessage());
                    }
                }));
    }


}
