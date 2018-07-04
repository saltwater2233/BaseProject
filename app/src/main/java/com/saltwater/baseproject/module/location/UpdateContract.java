package com.saltwater.baseproject.module.location;

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

    interface presenter extends BasePresenter{
        void loadUpdate();
    }

    interface View extends BaseView{
        void showMsg(String msg);
    }

}
