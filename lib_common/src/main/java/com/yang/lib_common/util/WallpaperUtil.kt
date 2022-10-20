package com.yang.lib_common.util

import android.Manifest
import android.app.WallpaperManager
import android.app.WallpaperManager.FLAG_LOCK
import android.app.WallpaperManager.FLAG_SYSTEM
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.RomUtils
import com.blankj.utilcode.util.Utils
import com.lxj.xpopup.XPopup
import com.yang.lib_common.service.CustomWallpaperService
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.OutputStream


/**
 * @ClassName: WallpaperUtil
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/12 16:24
 */
class WallpaperUtil {

    companion object {
        private const val TAG = "WallpaperUtil"






        fun setWallpaper(context: Context, path: String) {

            if (path.endsWith(".mp4",true)||TextUtils.isEmpty(path)){
                setDynamicWallpaper(context,path)
            }else{
                setStaticWallpaper(context,path)

            }
        }


        fun setStaticWallpaper(context: Context, path: String) {
            try {
                val uri = getUriWithPath(context, path)
                val intent: Intent
                val componentName: ComponentName
                when {
                    RomUtils.isHuawei() -> {
                        componentName = ComponentName(
                            "com.android.gallery3d",
                            "com.android.gallery3d.app.Wallpaper"
                        )
                        intent = Intent(Intent.ACTION_VIEW)
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    }
                    RomUtils.isXiaomi() -> {
                        componentName = ComponentName(
                            "com.android.thememanager",
                            "com.android.thememanager.activity.WallpaperDetailActivity"
                        )
                        intent = Intent("miui.intent.action.START_WALLPAPER_DETAIL")
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    RomUtils.isVivo() -> {
                        componentName =
                            ComponentName("com.vivo.gallery", "com.android.gallery3d.app.Wallpaper")
                        intent = Intent(Intent.ACTION_VIEW)
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    RomUtils.isOppo() -> {
                        componentName =
                            ComponentName(
                                "com.coloros.gallery3d",
                                "com.oppo.gallery3d.app.Wallpaper"
                            )
                        intent = Intent(Intent.ACTION_VIEW)
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)


                    }
                    else -> {
                        intent =
                            WallpaperManager.getInstance(context).getCropAndSetWallpaperIntent(uri)
                        context.startActivity(intent)
                        return
                    }
                }
                intent.setDataAndType(uri, "image/*")
                intent.putExtra("mimeType", "image/*")
                intent.component = componentName
                context.startActivity(intent)


            } catch (e: Exception) {
                e.printStackTrace()
                setWallpaperDefault(context, path)
            }
        }



        fun setDynamicWallpaper(context: Context, path: String){
            CustomWallpaperService.setWallpaper(context,path)
        }




        /**
         * 默认方法
         */
        private fun setWallpaperDefault(context: Context, path: String) {
            try {
                XPopup.Builder(context).asBottomList(
                    "", arrayOf("锁屏", "主屏幕", "同时设置")
                ) { position, text ->
                    val wallpaperManager = WallpaperManager.getInstance(context)
                    when (position) {
                        0 -> {
                            wallpaperManager.setStream(FileInputStream(path), null, true, FLAG_LOCK)
                            showShort("设置成功,如果未设置成功请手动设置下~")
                        }
                        1 -> {
                            wallpaperManager.setStream(
                                FileInputStream(path),
                                null,
                                true,
                                FLAG_SYSTEM
                            )
                            showShort("设置成功")
                        }
                        else -> {
                            wallpaperManager.setStream(FileInputStream(path))
                            showShort("设置成功")
                        }
                    }
                }.show()

            } catch (e: IOException) {
                e.printStackTrace()
                showShort(e.message.toString())
            }
        }


    }
}