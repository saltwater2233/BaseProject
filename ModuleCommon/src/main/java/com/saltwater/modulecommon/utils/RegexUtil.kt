package com.saltwater.modulecommon.utils

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/07/04
 * desc   : 正则工具类
 * version: 1.0
</pre> *
 */
object RegexUtil {


    /**
     * 判断手机号码是否合理
     *
     * @param phone
     */
    fun isPhoneNumber(phone: String): Boolean {
        /*
       * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
       * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
       * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
       */
        val telRegex = "[1][34578]\\d{9}"// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return phone.length == 11 && phone.matches(telRegex.toRegex())
    }

}
