package com.yang.lib_common.data

import android.os.Parcel
import android.os.Parcelable

/**
 * @ClassName: WallpaperData
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/13 9:44
 */
class WallpaperData(){
    var id:String? = null
    var userId:String? = null
    var wallUrl:String? = ""
    var imageName:String? = ""
    var tabId: String? = null
    var isVip: Boolean = false
    var wallType: Int? = null
    var wallName: String? = null
    var likeNum: Int? = null
    var extra: String? = null
    var isEdit: Boolean = false
    var createTime: String? = null
    var updateTime: String? = null

}

data class WallpaperTabData(
    var id:String?,
    var name:String?,
    var wallType:String?,//动态 静态
    var extra:String?,
    var createTime:String?,
    var updateTime:String?
)
data class WallpaperTypeData(
    var id:String?,
    var index:Int?,
    var name:String?,//类型
)

data class WallpaperDynamicData(
    var id:String?,
    var userId:String? = null,
    var userName: String?,
    var userImage: String?,
    var userVipLevel: Int = 0 ,
    var isAttention: Boolean = false,
    var dynamicContent:String?,
    var dynamicLikeNum:Int = 0,
    var dynamicCommentNum:Int = 0,
    var dynamicForwardNum:Int = 0,
    var wallList:MutableList<WallpaperData>
)

