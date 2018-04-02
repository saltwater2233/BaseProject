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
    private CompositeDisposable mCompositeDisposable;

    public UpdatePresenter(UpdateContract.View view ) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }



    @Override
    public void loadUpdate() {
        mCompositeDisposable.clear();//防止内存占用过大，每次请求前先清空一下
        Disposable disposable = RetrofitHelper.getUpdateApi()
                .getUpdate()
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
        mCompositeDisposable.add(disposable);
    }


}
