@file:JvmName("GlideUtil")

package com.yang.lib_common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatDrawableManager.preload
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.shuyu.gsyvideoplayer.utils.CommonUtil.dip2px
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.interceptor.UrlInterceptor.Companion.url


/**
 * @ClassName: GlideUtil
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/14 15:53
 */



fun preload(mContext: Context, url: String?) {
    Glide.with(mContext)
        .load(getRealUrl(url))
        .preload(
            getScreenPx(mContext)[0],
            getScreenPx(mContext)[1]
        )
}
fun loadImage(mContext: Context, url: String?, imageView: ImageView) {
    Glide.with(mContext).asBitmap()
        .load(getRealUrl(url))
        .into(imageView)
}

fun loadCircle(mContext: Context, url: String?, imageView: ImageView) {
    val options = RequestOptions.circleCropTransform()
    Glide.with(mContext).asBitmap()
        .load(getRealUrl(url))
        .apply(options)
        .into(imageView)
}

fun loadRadius(mContext: Context, url: String?,radius:Float, imageView: ImageView) {

    val options = RequestOptions.bitmapTransform(RoundedCorners(radius.toInt()))
    val shapeDrawable = ShapeDrawable()
    shapeDrawable.apply {
        shape = RoundRectShape(FloatArray(8){radius.dip2px(mContext).toFloat()}, null, null)
        paint.color = getRandomColor()
    }
    var width = 0
    var height = 0
    Glide.with(mContext).asBitmap()
        .load(getRealUrl(url))
        .into(object : CustomTarget<Bitmap>() {
            override fun onLoadCleared(placeholder: Drawable?) {
            }
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                width = resource.width
                height = resource.height
            }
        })
    imageView.background = shapeDrawable
    Glide.with(mContext).asBitmap()
        .load(getRealUrl(url))
        .dontAnimate()
        .placeholder(shapeDrawable)
        .error(shapeDrawable)
        .fitCenter()
        .apply(options)
        .override(width,height)
        .load(getRealUrl(url))
        .into(imageView)
}
fun loadSpaceRadius(mContext: Context, url: String?,radius:Float, imageView: ImageView,count:Int = 1,space:Float = 0f) {
    val options = RequestOptions.bitmapTransform(RoundedCorners(radius.toInt()))
    val shapeDrawable = ShapeDrawable()
    shapeDrawable.apply {
        shape = RoundRectShape(FloatArray(8){radius}, null, null)
        paint.color = getRandomColor()
    }
    var width = 0
    var height = 0
    Glide.with(mContext).asBitmap()
        .load(getRealUrl(url))
        .into(object : CustomTarget<Bitmap>() {
            override fun onLoadCleared(placeholder: Drawable?) {
            }
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                width = getScreenPx(mContext)[0]/count - space.dip2px(mContext)
                height = (((getScreenPx(mContext)[0]/count -space.dip2px(mContext))/resource.width.toFloat())*resource.height).toInt()

//                val layoutParams = imageView.layoutParams
//                layoutParams.width = width
//                layoutParams.height = height
//                imageView.layoutParams = layoutParams
//                shapeDrawable.setBounds(0,0,width,height)
            }
        })
//    imageView.background = shapeDrawable
    Glide.with(mContext).asBitmap()
        .load(getRealUrl(url))
        .dontAnimate()
        .placeholder(shapeDrawable)
        .error(shapeDrawable)
        .fitCenter()
        .apply(options)
        .override(width,height)
        .into(imageView)
}

fun loadBgImage(mContext: Context, url: String?,imageView: ImageView){
    val colorDrawable = ColorDrawable(getRandomColor())
    Glide.with(mContext)
        .load(getRealUrl(url))
        .error(colorDrawable)
        .centerCrop()
        .dontAnimate()
        .placeholder(colorDrawable)
        .into(imageView)
}

fun ImageView.loadImage(mContext: Context, url: String?){
    loadImage(mContext,url,this)
}
fun ImageView.loadCircle(mContext: Context, url: String?){
    loadCircle(mContext,url,this)
}
fun ImageView.loadRadius(mContext: Context,radius:Float, url: String?){
    loadRadius(mContext,url,radius,this)
}
fun ImageView.loadBgImage(mContext: Context, url: String?){
    loadBgImage(mContext,url,this)
}

fun getRealUrl(url: String?):String?{
    var realUrl = url
    if (realUrl != null) {
        if (!realUrl.contains("http")){
            realUrl = AppConstant.ClientInfo.IMAGE_MODULE+realUrl
        }
    }
    return realUrl
}