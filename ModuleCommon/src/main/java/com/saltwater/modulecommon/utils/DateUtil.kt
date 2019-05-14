package com.saltwater.modulecommon.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * <pre>
 * author : wenxin
 * e-mail : wenxin2233@outlook.com
 * time   : 2018/02/26
 * desc   : 日期转换
 * version: 1.0
</pre> *
 */

object DateUtil {
    const val FORMAT_YEAR = "yyyy"
    const val FORMAT_MONTH = "MM"
    const val FORMAT_DAY = "dd"
    const val FORMAT_MONTH_DAY = "MM月dd日"
    const val FORMAT_DATE = "yyyy-MM-dd"
    const val FORMAT_TIME = "HH:mm"
    const val FORMAT_MONTH_DAY_TIME = "MM月dd日  hh:mm"
    const val FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm"
    const val FORMAT_DATE1_TIME = "yyyy/MM/dd HH:mm"
    const val FORMAT_DATE_TIME_SECOND = "yyyy/MM/dd HH:mm:ss"

    private val dateFormat = SimpleDateFormat()

    private const val YEAR = 365 * 24 * 60 * 60// 年
    private const val MONTH = 30 * 24 * 60 * 60// 月
    private const val DAY = 24 * 60 * 60// 天
    private const val HOUR = 60 * 60// 小时
    private const val MINUTE = 60// 分钟


    /**
     * 获取年
     */
    val year: Int
        get() {
            val cd = Calendar.getInstance()
            return cd.get(Calendar.YEAR)
        }

    /**
     * 获取月
     */
    val month: Int
        get() {
            val cd = Calendar.getInstance()
            return cd.get(Calendar.MONTH) + 1
        }

    /**
     * 获取日
     */
    val day: Int
        get() {
            val cd = Calendar.getInstance()
            return cd.get(Calendar.DATE)
        }

    /**
     * 获取时
     */
    val hour: Int
        get() {
            val cd = Calendar.getInstance()
            return cd.get(Calendar.HOUR)
        }


    /**
     * 根据时间戳获取描述性时间，如3分钟前，1天前
     *
     * @param timestamp 时间戳 单位为毫秒
     * @return 时间字符串
     */

    fun getTimeDescription(timestamp: Long): String {
        val currentTime = System.currentTimeMillis()
        // 与现在时间相差秒数
        val timeGap = (currentTime - timestamp) / 1000
        val timeStr: String
        if (timeGap > YEAR) {
            timeStr = (timeGap / YEAR).toString() + "年前"
        } else if (timeGap > MONTH) {
            timeStr = (timeGap / MONTH).toString() + "个月前"
        } else if (timeGap > DAY) {// 1天以上
            timeStr = (timeGap / DAY).toString() + "天前"
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeStr = (timeGap / HOUR).toString() + "小时前"
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeStr = (timeGap / MINUTE).toString() + "分钟前"
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚"
        }
        return timeStr
    }

    /**
     * 获取当前日期,格式"yyyy-MM-dd HH:MM"
     */
    fun getCurrentTime(): String {
        dateFormat.applyPattern(FORMAT_DATE_TIME)
        return dateFormat.format(Date())
    }

    /**
     * 获取当前日期的指定格式的字符串
     *
     * @param format 指定的日期时间格式
     */
    fun getCurrentTime(format: String): String {
        dateFormat.applyPattern(format)
        return dateFormat.format(Date())
    }


    /**
     * date类型转换为String类型
     * formatType格式为yyyy-MM-dd HH:mm:ss
     */
    fun dateToString(data: Date, formatType: String): String {
        return SimpleDateFormat(formatType).format(data)
    }

    /**
     * date类型转换为long类型
     */
    fun dateToLong(date: Date): Long {
        return date.time
    }

    /**
     * string类型转换为date类型
     * strTime的时间格式必须要与formatType的时间格式相同
     */

    fun stringToDate(strTime: String, formatType: String): Date {
        val formatter = SimpleDateFormat(formatType)
        return formatter.parse(strTime)
    }

    /**
     * string类型转换为long类型
     * strTime要转换的String类型的时间
     * formatType时间格式
     * strTime的时间格式和formatType的时间格式必须相同
     */

    fun stringToLong(strTime: String, formatType: String): Long {
        // String类型转成date类型
        val date = stringToDate(strTime, formatType)
        return dateToLong(date)
    }


    /**
     * long转换为Date类型
     */
    fun longToDate(currentTime: Long, formatType: String): Date {
        // 根据long类型的毫秒数生命一个date类型的时间
        val dateOld = Date(currentTime)
        // 把date类型的时间转换为string
        val sDateTime = dateToString(dateOld, formatType)
        // 把String类型转换为Date类型
        return stringToDate(sDateTime, formatType)
    }

    /**
     * long类型转换为String类型
     */
    fun longToString(currentTime: Long, formatType: String): String {
        val strTime: String
        // long类型转成Date类型
        val date = longToDate(currentTime, formatType)
        // date类型转成String
        strTime = dateToString(date, formatType)
        return strTime
    }





    //计算生日
    fun getBirthday(from: Date?): String {
        var age = ""
        var year = 0
        var month = 0
        var day = 0
        if (from == null) {
            age = age + 0
        } else {
            val c1 = Calendar.getInstance()
            val c2 = Calendar.getInstance()
            c1.time = from
            c2.time = Date()
            if (c1.after(c2)) {
                throw IllegalArgumentException("生日不能超过当前日期")
            }
            val from_year = c1.get(Calendar.YEAR)
            val from_month = c1.get(Calendar.MONTH) + 1
            val from_day = c1.get(Calendar.DAY_OF_MONTH)
            println("以前：$from_year-$from_month-$from_day")
            val MaxDayOfMonth = c1.getActualMaximum(Calendar.DAY_OF_MONTH)
            //System.out.println(MaxDayOfMonth);
            val to_year = c2.get(Calendar.YEAR)
            val to_month = c2.get(Calendar.MONTH) + 1
            val to_day = c2.get(Calendar.DAY_OF_MONTH)
            println("现在：$to_year-$to_month-$to_day")

            year = to_year - from_year
            if (c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR) < 0) {
                year = year - 1
            }
            if (year < 1) {// 小于一岁要精确到月份和天数
                println("--------")
                if (to_month - from_month > 0) {
                    month = to_month - from_month
                    if (to_day - from_day < 0) {
                        month = month - 1
                        day = to_day - from_day + MaxDayOfMonth
                    } else {
                        day = to_day - from_day
                    }
                } else if (to_month - from_month == 0) {
                    if (to_day - from_day > 0) {
                        day = to_day - from_day
                    }
                }
            }
            if (year > 1) {
                age = age + year + "岁"
            } else if (month > 0) {
                age = age + month + "个月" + day + "天"
            } else {
                age = age + day + "天"
            }
        }
        return age
    }
}
