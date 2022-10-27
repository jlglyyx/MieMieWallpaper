package com.yang.lib_common.data

import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.lib_common.util.fromJson
import com.yang.lib_common.util.getDefaultMMKV

/**
 * @ClassName: UserInfoHandle
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/27 14:51
 */
object UserInfoHold {

    val userInfo: UserInfoData?
        get() = getDefaultMMKV().getString(AppConstant.Constant.USER_INFO, "")
            ?.fromJson<UserInfoData>()


    val userId: String?
        get() = getDefaultMMKV().getString(AppConstant.Constant.USER_INFO, "")
            ?.fromJson<UserInfoData>()?.id


    val userName: String?
        get() = getDefaultMMKV().getString(AppConstant.Constant.USER_INFO, "")
            ?.fromJson<UserInfoData>()?.userName
}