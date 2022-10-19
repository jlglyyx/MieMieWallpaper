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

    val options = RequestOptions.bitmapTransform(RoundedCorners(dip2px(mContext, radius)))
    val shapeDrawable = ShapeDrawable()
    shapeDrawable.apply {
        shape = RoundRectShape(FloatArray(8){radius.dip2px(mContext).toFloat()}, null, null)
        paint.color = getRandomColor()
    }
    imageView.background = shapeDrawable


    Glide.with(mContext).asBitmap()
        .load(getRealUrl(url))
        .dontAnimate()
        .placeholder(shapeDrawable)
        .error(shapeDrawable)
        .apply(options)
        .into(imageView)

//    val colorDrawable = ColorDrawable(getRandomColor())
//    Glide.with(mContext).asBitmap()
//        .load(url)
//        .dontAnimate()
//        .placeholder(colorDrawable)
//        .error(colorDrawable)
//        .apply(options)
//        .into(imageView)


//    val options = RequestOptions.bitmapTransform(RoundedCorners(dip2px(mContext, radius)))
//    val shapeDrawable = ShapeDrawable(RoundRectShape(floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius), null, null)).apply {
//        paint.color = getRandomColor()
//        bounds = Rect(0, 0, imageView.width, imageView.height)
//    }
//    imageView.setBackground(shapeDrawable)
//    Glide.with(mContext).asBitmap()
//        .load(url)
//        .dontAnimate()
//        .placeholder(shapeDrawable)
//        .error(shapeDrawable)
//        .apply(options)
//        .into(imageView)
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