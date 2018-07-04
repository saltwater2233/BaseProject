package com.saltwater.baseproject.module.location;

import com.saltwater.baseproject.network.RetrofitHelper;
import com.saltwater.baseproject.network.entity.UpdateEntity;
import com.trello.rxlifecycle2.LifecycleProvider;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class UpdateModel {

    public void update(UpdateContract.View view, LifecycleProvider provider){
        RetrofitHelper.getUpdateApi()
                .getUpdate()
                .compose(provider.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateEntity>() {
                    @Override
                    public void accept(UpdateEntity updateEntity) throws Exception {
                        view.showMsg(updateEntity.getMsg());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }
}
