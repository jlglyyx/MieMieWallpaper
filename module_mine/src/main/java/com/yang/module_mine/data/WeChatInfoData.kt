package com.yang.module_mine.data

/**
 * @ClassName: WeChatInfoData
 * @Description:
 * @Author: yxy
 * @Date: 2022/11/17 15:54
 */
data class WeChatInfoData(
    var access_token: String?,
    var expires_in: Int?,
    var openid: String?,
    var refresh_token: String?,
    var scope: String?,
    var unionid: String?
)
