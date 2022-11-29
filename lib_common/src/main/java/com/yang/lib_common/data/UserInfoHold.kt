package com.yang.lib_common.data

import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.room.entity.UserInfoData
import com.yang.lib_common.util.fromJson
import com.yang.lib_common.util.getMMKVValue

/**
 * @ClassName: UserInfoHandle
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/27 14:51
 */
object UserInfoHold {


    val userInfo: UserInfoData?
        get() = getMMKVValue(AppConstant.Constant.USER_INFO, "")?.fromJson<UserInfoData>()


    val userId: String?
        get() = getMMKVValue(AppConstant.Constant.USER_INFO, "")
            .fromJson<UserInfoData>().id
    val userIsSign: Boolean?
        get() = getMMKVValue(AppConstant.Constant.USER_INFO, "")
            .fromJson<UserInfoData>().userIsSign


    val userName: String?
        get() = getMMKVValue(AppConstant.Constant.USER_INFO, "")
            .fromJson<UserInfoData>().userName

    val userWeChatCode: String?
        get() = getMMKVValue(AppConstant.WeChatConstant.CODE, "")

    val userWeChatToken: String?
        get() = getMMKVValue(AppConstant.WeChatConstant.WECHAT_TOKEN, "")

}