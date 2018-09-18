package com.saltwater.baseproject.module.update.contract;

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
    }

}
