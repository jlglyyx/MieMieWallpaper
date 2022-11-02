@file:JvmName("TimeUtil")
package com.yang.lib_common.util

import android.util.Log
import com.yang.lib_common.constant.AppConstant
import java.util.*

/**
 * @ClassName: TimeUtil
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/2 10:01
 */

private const val TIME_UTIL_TAG = "TimeUtil"

fun isNextDay():Boolean{
//    val lastTime = getMMKVValue(AppConstant.Constant.LAST_TIME, System.currentTimeMillis())
//    Log.i(TIME_UTIL_TAG, "isNextDay_: ${formatDate_YYYY_MMM_DD_HHMMSS.format(lastTime)}")
//    val lastCalendar = Calendar.getInstance()
//    lastCalendar.time = Date(lastTime!!)
//    val format1 = formatDate_YYYY_MMM_DD_HHMMSS.format(lastCalendar.time)
//    Log.i(TIME_UTIL_TAG, "isNextDayformat1: $format1 ")
//    val tomorrowCalendar = Calendar.getInstance()
//    tomorrowCalendar.get(Calendar.DAY_OF_YEAR)
//    tomorrowCalendar.set(Calendar.HOUR_OF_DAY,23)
//    tomorrowCalendar.set(Calendar.MINUTE,59)
//    tomorrowCalendar.set(Calendar.SECOND,59)
//    val after = lastCalendar.before(tomorrowCalendar)
//    val format = formatDate_YYYY_MMM_DD_HHMMSS.format(tomorrowCalendar.time)
//    Log.i(TIME_UTIL_TAG, "isNextDay: $format   $after ")
//    return lastCalendar.before(tomorrowCalendar)
    return false
}