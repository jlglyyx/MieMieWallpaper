package com.yang.lib_common.data

import android.os.Parcel
import android.os.Parcelable
import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.qq.e.comm.pi.NUADI

/**
 * @ClassName: WallpaperData
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/13 9:44
 */
class WallpaperData() {
    var id: String? = null
    var userId: String? = null
    var userAttr: String? = null
    var isAttention: Boolean = false
    var isCollection: Boolean = false
    var wallUrl: String? = ""
    var imageName: String? = ""
    var tabId: String? = null
    var isVip: Boolean = false
    var wallType: Int? = null
    var wallName: String? = null
    var wallDesc: String? = null
    var likeNum: Int? = null
    var extra: String? = null
    var isEdit: Boolean = false
    var createTime: String? = null
    var updateTime: String? = null

}

data class WallpaperTabData(
    var id: String?,
    var name: String?,
    var wallType: String?,//动态 静态
    var extra: String?,
    var createTime: String?,
    var updateTime: String?
)

data class WallpaperTypeData(
    var id: String?,
    var index: Int?,
    var name: String?,//类型
)

data class WallpaperDynamicData(
    var id: String?,
    var userId: String? = null,
    var userName: String?,
    var userVipLevel: Int = 0,
    var isAttention: Boolean = false,
    var commentNum: Int?,
    var content: String?,
    var createTime: String?,
    var forwardNum: Int? = 0,
    var likeNum: Int? = 0,
    var resourceUrls: String?,
    var seeNum: Int? = 0,
    var sendLocation: String?,
    var updateTime: String?,
    var userAttr: String?,
)


data class WallpaperDynamicCommentData(
    var children: List<WallpaperDynamicCommentData>?,
    var content: String?,
    var createTime: String?,
    var userAttr: String? ,
    var replyUserName: String? ,
    var replyUserAttr: String? ,
    var userName: String? ,
    var id: String?,
    var parentId: String?,
    var likeNum: Int?,
    var replyUserId: String?,
    var resourceId: String?,
    var unLikeNum: Int?,
    var updateTime: String?,
    var userId: String?,
)



