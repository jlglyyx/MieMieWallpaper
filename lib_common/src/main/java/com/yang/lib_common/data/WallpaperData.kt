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
    var imageUrl:String? = ""
    var imageName:String? = ""
    var tabId: String? = null
    var headType: Int? = null
    var isVip: Boolean = false
    var wallType: Int? = null
    var title: String? = null
    var likeNum: Int? = null
    var extra: String? = null
    var isEdit: Boolean = false
    var createTime: String? = null
    var updateTime: String? = null

}

data class WallpaperTabData(
    var id:String?,
    var name:String?,
    var type:String?,//类型
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

