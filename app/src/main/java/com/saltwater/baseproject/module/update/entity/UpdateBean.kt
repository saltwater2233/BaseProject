package com.saltwater.baseproject.module.update.entity

import java.io.Serializable

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/03/22
 * desc   :
 * version: 1.0
</pre> *
 */

class UpdateBean : Serializable {

    /**
     * status : 1
     * msg : 【更新】
     *
     * - 极大提升性能及稳定性
     * - 部分用户无法使用新浪微博登录
     * - 部分用户无图模式无法分享至微信及朋友圈
     * url : http://zhstatic.zhihu.com/pkg/store/daily/zhihu-daily-zhihu-2.6.0(744)-release.apk
     * latest : 2.6.0
     */

    var status: Int = 0
    var msg: String? = null
    var url: String? = null
    var latest: String? = null
}
