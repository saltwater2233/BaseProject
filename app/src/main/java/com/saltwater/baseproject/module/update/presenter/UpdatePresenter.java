package com.saltwater.baseproject.module.update.presenter;

import android.content.Context;

import com.saltwater.baseproject.base.BasePresenter;
import com.saltwater.baseproject.module.update.contract.UpdateContract;
import com.saltwater.baseproject.module.update.entity.UpdateBean;
import com.saltwater.baseproject.module.update.model.UpdateModel;
import com.trello.rxlifecycle2.LifecycleProvider;


/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/03/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class UpdatePresenter extends BasePresenter<UpdateContract.View> {

    public UpdatePresenter(Context context, LifecycleProvider provider) {
        super(context, provider);
    }

    public void loadUpdate() {

        UpdateModel.update(this);
    }

    public void updateSuccess(UpdateBean info) {
        getView().setUpdateInfo(info.getMsg());
    }

}
