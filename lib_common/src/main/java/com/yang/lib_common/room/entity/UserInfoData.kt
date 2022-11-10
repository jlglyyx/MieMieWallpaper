package com.yang.lib_common.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class UserInfoData(
    @PrimaryKey
    var id: String,
    var token: String?,
    var userName: String?,
    var userSex: Int = 0,
    var userAge: Int?,
    var userBirthDay: String?,
    var userDescribe: String?,
    var userAccount: String,
    var userPassword: String,
    var userAddress: String?,
    var userIntegral: Int = 0 ,
    var userAttention: Int = 0 ,
    var userFan: Int = 0 ,
    var userLocationAddress: String?,
    var userLeftBackgroundImage: String?,
    var userInfoBackgroundImage: String?,
    var userMineBackgroundImage: String?,
    var userPhone: String?,
    var userImage: String?,
    var userVipLevel: Int = 0 ,
    var userCredit: Int = 0 ,
    var userVipExpired: Boolean = false ,
    var userType: Int = 0 ,
    var userObtain: String?,
    var userSign: Int?,
    var userIsSign: Boolean = false ,
    var userExtension: String?,
    var updateTime: String?,
    var createTime: String?,
    var userExtraInfo: String?
)