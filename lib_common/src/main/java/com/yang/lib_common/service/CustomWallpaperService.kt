package com.yang.lib_common.service

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.hardware.Camera
import android.media.MediaPlayer
import android.os.Bundle
import android.service.wallpaper.WallpaperService
import android.text.TextUtils
import android.util.Log
import android.view.SurfaceHolder
import java.io.IOException


/**
 * @ClassName: CustomWallpaperService
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/12 13:44
 */
class CustomWallpaperService  : WallpaperService(){


    override fun onCreateEngine(): Engine {

        return CustomEngine()
    }

    companion object{
        lateinit var  context: Context
        lateinit var  path:String
        fun setWallpaper(mContext: Context,mPath:String){
            try {
                context = mContext
                path = mPath
                WallpaperManager.getInstance(context).clearWallpaper()
                val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
                val componentName = ComponentName(context, CustomWallpaperService::class.java)
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, componentName)
                context.startActivity(intent)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
    }



    inner class CustomEngine : WallpaperService.Engine() {
        private var mediaPlayer: MediaPlayer? = null
        lateinit var camera: Camera

        override fun onVisibilityChanged(visible: Boolean) {

            if (TextUtils.isEmpty(path)){
                if (visible) {
                    startPreview()
                } else {
                    stopPreview()
                }
            }else{
                if (visible) {
                    mediaPlayer?.start()
                } else {
                    mediaPlayer?.pause()
                }
            }

        }

        override fun onSurfaceCreated(holder: SurfaceHolder?) {
            super.onSurfaceCreated(holder)
            if (!TextUtils.isEmpty(path)){
                mediaPlayer = MediaPlayer()
                mediaPlayer?.apply {
                    setDataSource(path)
                    isLooping = true
                    setSurface(holder!!.surface)
                    prepare()
                }
            }

        }
        override fun onDestroy() {
            super.onDestroy()
            if (TextUtils.isEmpty(path)){
                stopPreview()
            }else{
                mediaPlayer?.apply {
                    if (isPlaying){
                        stop()
                    }
                    release()
                }
            }

        }


        private fun startPreview() {
            camera = Camera.open()
            camera.setDisplayOrientation(90)
            try {
                camera.setPreviewDisplay(surfaceHolder)
                camera.startPreview()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        private fun stopPreview() {
            try {
                camera.stopPreview()
                camera.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}