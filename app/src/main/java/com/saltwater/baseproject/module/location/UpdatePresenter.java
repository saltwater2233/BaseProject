package com.saltwater.baseproject.module.location;

import com.saltwater.baseproject.network.RetrofitHelper;
import com.saltwater.baseproject.network.entity.UpdateEntity;
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
    private LifecycleProvider mProvider;
    private UpdateModel mModel;

    public UpdatePresenter(UpdateContract.View view ,LifecycleProvider provider) {
        mView = view;
        mProvider = provider;
        mModel = new UpdateModel();
    }

    @Override
    public void loadUpdate() {
        mModel.update(mView,mProvider);
    }


    @Override
    public void start() {

    }
}
