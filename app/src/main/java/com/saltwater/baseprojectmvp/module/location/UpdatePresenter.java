package com.saltwater.baseprojectmvp.module.location;

import com.saltwater.baseprojectmvp.network.RetrofitHelper;
import com.saltwater.baseprojectmvp.network.entity.UpdateEntity;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import javax.annotation.Nonnull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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

public class UpdatePresenter implements UpdateContract.presenter {
    @Nonnull
    private UpdateContract.View mView;
    @Nonnull
    private UpdateEntity mUpdateEntity;
    @Nonnull
    private LifecycleProvider mProvider;

    public UpdatePresenter(UpdateContract.View view ,LifecycleProvider provider) {
        mView = view;
        mProvider = provider;
    }


    @Override
    public void loadUpdate() {
        RetrofitHelper.getUpdateApi()
                .getUpdate()
                .compose(mProvider.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateEntity>() {
                    @Override
                    public void accept(UpdateEntity updateEntity) throws Exception {
                        mUpdateEntity = updateEntity;
                        mView.showMsg(mUpdateEntity.getMsg());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }


    @Override
    public void start() {

    }
}
