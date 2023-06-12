@file:JvmName("MMKVUtil")
package com.yang.lib_common.util

import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.room.entity.UserInfoData

/**
 * @ClassName: MMKVUtil
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/2 10:06
 */

/**
 * @return 获取getDefaultMMKV
 */
fun getDefaultMMKV(): MMKV {
    return MMKV.defaultMMKV()
}



/**
 * @return 更新用户缓存
 */
fun updateLocalUserInfo(userInfoData: UserInfoData) {
    setMMKVValue(AppConstant.Constant.USER_INFO, userInfoData.toJson())
}


/**
 * 设置缓存信息
 * @param key
 */
fun setMMKVValue(key:String,value : Any){
    when (value ){
        is Boolean ->
        getDefaultMMKV().encode(key,value)
        is String ->
        getDefaultMMKV().encode(key,value)
        is Long ->
        getDefaultMMKV().encode(key,value)
        is Double ->
        getDefaultMMKV().encode(key,value)
        is Float ->
        getDefaultMMKV().encode(key,value)
        is Int ->
        getDefaultMMKV().encode(key,value)
        else ->{
            getDefaultMMKV().encode(key,value.toJson())
        }
    }
}

/**
 * 获取缓存信息
 * @param key
 * @defaultValue key
 */
fun <T> getMMKVValue(key:String,defaultValue:T):T{

    when (defaultValue ){
        is Boolean ->
            return getDefaultMMKV().decodeBool(key,defaultValue) as T
        is String ->
            return getDefaultMMKV().decodeString(key,defaultValue) as T
        is Long ->
            return getDefaultMMKV().decodeLong(key,defaultValue) as T
        is Double ->
            return getDefaultMMKV().decodeDouble(key,defaultValue) as T
        is Float ->
            return getDefaultMMKV().decodeFloat(key,defaultValue) as T
        is Int ->
            return getDefaultMMKV().decodeInt(key,defaultValue) as T
        else ->{
            return getDefaultMMKV().decodeString(key,defaultValue?.toJson()) as T
        }
    }
}

/**
 * 清除全部缓存
 */
fun clearAllMMKV(){
    getDefaultMMKV().clearAll()
}

/**
 * 清除缓存
 */
fun clearMMKV(key:String){
    getDefaultMMKV().removeValueForKey(key)
}
