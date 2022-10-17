package com.yang.module_main.data

/**
 * @ClassName: WallpaperData
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/13 9:44
 */
class WallpaperData {
    var id:String? = null
    var imageUrl:String = ""
    var imageName:String = "${System.currentTimeMillis()}.jpg"
}

data class WallpaperTabData(
    var title:String
)