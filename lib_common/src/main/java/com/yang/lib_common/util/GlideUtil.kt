@file:JvmName("GlideUtil")

package com.yang.lib_common.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.shuyu.gsyvideoplayer.utils.CommonUtil.dip2px


/**
 * @ClassName: GlideUtil
 * @Description:
 * @Author: yxy
 * @Date: 2022/10/14 15:53
 */


fun loadImage(mContext: Context, url: String?, imageView: ImageView) {
    Glide.with(mContext).asBitmap()
        .load(url)
        .into(imageView)
}

fun loadCircle(mContext: Context, url: String?, imageView: ImageView) {
    val options = RequestOptions.circleCropTransform()
    Glide.with(mContext).asBitmap()
        .load(url)
        .apply(options)
        .into(imageView)
}
fun loadRadius(mContext: Context, url: String?,radius:Float, imageView: ImageView) {
    val options = RequestOptions.bitmapTransform(RoundedCorners(dip2px(mContext, radius)))
    Glide.with(mContext).asBitmap()
        .load(url)
        .apply(options)
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