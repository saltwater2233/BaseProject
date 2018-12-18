package com.saltwater.baseproject.module.update.contract;

import android.content.Context;

import com.saltwater.baseproject.base.BasePresenter;
import com.saltwater.baseproject.base.BaseView;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/03/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public interface UpdateContract {

    interface View extends BaseView {
        void setUpdateInfo(String msg);

        void showToast(String msg);
    }


    abstract class Presenter extends BasePresenter<View> {
        public Presenter(Context context) {
            super(context);
        }

        public abstract void loadUpdate();
    }

    abstract class Model {

    }


}
