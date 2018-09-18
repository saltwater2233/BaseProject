package com.saltwater.baseproject.module.update.model;

import com.saltwater.baseproject.module.update.contract.UpdateContract;
import com.saltwater.baseproject.module.update.presenter.UpdatePresenter;
import com.saltwater.baseproject.network.RetrofitHelper;
import com.saltwater.baseproject.module.update.entity.UpdateBean;
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

    public static void update(UpdatePresenter presenter) {
        RetrofitHelper.getUpdateApi()
                .getUpdate()
                .compose(presenter.getProvider().bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateBean>() {
                    @Override
                    public void accept(UpdateBean updateBean) throws Exception {
                        presenter.updateSuccess(updateBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        presenter.requestFail(throwable);
                    }
                });
    }
}
